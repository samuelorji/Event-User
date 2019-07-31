package com.soundcloud.maze
package service.writer

import java.io.PrintWriter

import core.config.ActorLike

object SocketConnectionWriter {
  case class WriteToSocket(msg : String)
  def apply(writer : PrintWriter) = new SocketConnectionWriter(writer)
}

/**
  * A socket Connection writer is used so as to maintain order when writing the messages
  * as each incoming message to be sent any client is queued in its mailbox.
  * This way, messages do not arrive out of order */
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