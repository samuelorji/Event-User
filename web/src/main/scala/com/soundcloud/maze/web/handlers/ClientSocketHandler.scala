package com.soundcloud.maze.web.handlers

import java.io._
import java.net.ServerSocket
import java.nio.charset.Charset

import com.soundcloud.maze.core.action.users.User
import com.soundcloud.maze.core.config.{ActorLike, ActorSystemLike, MazeConfig}
import com.soundcloud.maze.service.client.Registerer

object ClientSocketHandler {
  case object AcceptConnections

}
class ClientSocketHandler(implicit system : ActorSystemLike) extends ActorLike  {
  var socket : Option[ServerSocket] = None

  import ClientSocketHandler._

  private val registerer       = createClientRegisterer
  def createClientRegisterer   = system.execute(new Registerer)

  override protected def receive: PartialFunction[Any, Unit] = {
    case AcceptConnections =>
      socket = Some(new ServerSocket(MazeConfig.mazeUsersPort))

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
  }

  def streamToPrintWriter(outputStream: OutputStream): PrintWriter =
    new PrintWriter(
      new BufferedWriter(/* buffering characters so as to provide for the efficient reading of characters, lines and arrays. */
        new OutputStreamWriter(outputStream, Charset.forName("UTF-8"))))
}