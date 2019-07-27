package com.soundcloud.maze.dubs.actors

import java.util.concurrent.LinkedBlockingQueue


object ActorLike{
  case object Shutdown
}
trait ActorLike extends Runnable {
  protected def receive: PartialFunction[Any, Unit]

  private[this] val mailbox = new LinkedBlockingQueue[Any] // to avoid race condition when adding to the queue

  import ActorLike._

  final def ! : Any => Unit = {
    case Shutdown => onShutdown()
    case req : Any =>
      mailbox.add(req)
  }

  protected def onShutdown(): Unit = println(s"Shutting down actor")

  override def run(): Unit = {

    def dequeMailbox() : Unit =
      mailbox.take() match {
        case msg if receive.isDefinedAt(msg) =>
          receive(msg)
          dequeMailbox()
        case unexpected =>
          println(s"Received unexpected message [$unexpected]")
          dequeMailbox()
      }
    dequeMailbox()
  }
}
