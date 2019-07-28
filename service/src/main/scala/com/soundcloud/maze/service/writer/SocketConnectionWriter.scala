package com.soundcloud.maze.service.writer

import java.io.PrintWriter

import com.soundcloud.maze.core.config.ActorLike

object SocketConnectionWriter {
  case class WriteToSocket(msg : String)
  def apply(writer : PrintWriter) = new SocketConnectionWriter(writer)
}
class SocketConnectionWriter(socketWriter : PrintWriter) extends ActorLike {
  import SocketConnectionWriter._
  override protected def receive: PartialFunction[Any, Unit] = {
    case req : WriteToSocket =>
      socketWriter.print(req.msg +"\n")
      socketWriter.flush() //to avoid buffer issues

  }

  override protected def shutdownActorLike(): Unit = {
    socketWriter.close() // To avoid resource wastage
    super.shutdownActorLike()
  }
}