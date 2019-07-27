package com.soundcloud.maze.dubs.actors

import java.io.PrintWriter

object SocketConnectionWriter {
  case class WriteToSocket(msg : String)
  def apply(writer : PrintWriter) = new SocketConnectionWriter(writer)
}
class SocketConnectionWriter(socketWriter : PrintWriter) extends ActorLike {
  import SocketConnectionWriter._
  override protected def receive: PartialFunction[Any, Unit] = {
    case req : WriteToSocket =>
      socketWriter.print(req.msg )
      socketWriter.write("\r\n")
      socketWriter.flush() //to avoid buffer issues

  }

  override protected def onShutdown(): Unit = {
    socketWriter.close() // To avoid resource wastage
    super.onShutdown()
  }
}