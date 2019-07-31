package com.soundcloud.maze
package web.handlers

import java.io._
import java.net.ServerSocket
import java.nio.charset.Charset

import core.action.users.User
import core.config.{ ActorLike, ActorSystemLike, MazeConfig }

import service.client.Registerer
import service.registry.UserRegistry

object ClientSocketHandler {
  case object AcceptConnections
  def getClientSocketHandlerInstance(implicit system : ActorSystemLike) = new ClientSocketHandler
}

/**
  * This Actor exposes a TCP connection for multiple users to connect to */
private[web] class ClientSocketHandler(implicit system : ActorSystemLike) extends ActorLike  {
  var socket : Option[ServerSocket] = None

  import ClientSocketHandler._

  private val registerer       = createClientRegisterer
  def createClientRegisterer   = system.execute(Registerer.getRegistererInstance)

  override protected def receive: PartialFunction[Any, Unit] = {
    case AcceptConnections =>
      socket = Some(createSocket)

      //because we are accepting more than one connection on this socket
      //We tail recursively loop through for new incoming connections

    def acceptConnection() : Unit = {
      val currentSocket = socket.get.accept()
      val user = User(new BufferedReader(/*/* buffering characters so as to provide for the efficient reading of characters, lines and arrays. */*/
        new InputStreamReader(currentSocket.getInputStream))
        .readLine().toInt)
      registerer ! Registerer.RegisterNewClient(user.userId,streamToPrintWriter(currentSocket.getOutputStream))
      acceptConnection()
    }
      acceptConnection()

    case ActorLike.Shutdown =>
      shutdownActorLike()
  }

  def createSocket = new ServerSocket(MazeConfig.mazeUsersPort)

  def streamToPrintWriter(outputStream: OutputStream): PrintWriter =
    new PrintWriter(
      new BufferedWriter(/* buffering characters so as to provide for the efficient reading of characters, lines and arrays. */
        new OutputStreamWriter(outputStream, Charset.forName("UTF-8"))))

  override protected def shutdownActorLike(): Unit = {
    UserRegistry.getAllUsers.foreach(_._2 ! ActorLike.Shutdown)
    super.shutdownActorLike()
  }
}