package com.soundcloud.maze.dubs.actors

import java.io._
import java.net.ServerSocket
import java.nio.charset.Charset

import com.soundcloud.maze.dubs.actors.Router.RegisterNewClient

object ClientSocketHandler {
  case object AcceptConnections

}
class ClientSocketHandler(router : ActorLike, connectionPort : Int) extends ActorLike  {
  var socket : ServerSocket = _

  import ClientSocketHandler._

  override protected def receive: PartialFunction[Any, Unit] = {
    case AcceptConnections =>
      socket = new ServerSocket(connectionPort)

      //because we are accepting more than one connection on this socket
      //We tail recursively loop through for new incoming connections

    def acceptConnection() : Unit = {
      val currentSocket = socket.accept()
      val user = new BufferedReader(/*/* buffering characters so as to provide for the efficient reading of characters, lines and arrays. */*/
        new InputStreamReader(currentSocket.getInputStream))
        .readLine().toInt
      router ! RegisterNewClient(user,streamToPrintWriter(currentSocket.getOutputStream))
      acceptConnection()
    }
      acceptConnection()
  }

  def streamToPrintWriter(outputStream: OutputStream): PrintWriter =
    new PrintWriter(
      new BufferedWriter(/* buffering characters so as to provide for the efficient reading of characters, lines and arrays. */
        new OutputStreamWriter(outputStream, Charset.forName("UTF-8"))))
}