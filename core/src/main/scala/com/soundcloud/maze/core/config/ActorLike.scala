package com.soundcloud.maze.core.config

import java.util.concurrent.LinkedBlockingQueue


/**
  * Crude Implementation of An Actor
  * The mailbox must be unique to each individual actor and must not be accessible from outside the actor itself*/
object ActorLike {
  case object Shutdown
}

trait ActorLike extends Runnable {
  protected def receive: PartialFunction[Any, Unit]

  private[this] val mailbox = new LinkedBlockingQueue[Any] // to avoid race condition when adding to the queue


  import ActorLike._

  final def ! : Any => Unit = {
    case Shutdown => shutdownActorLike()
    case req: Any =>
      mailbox.add(req)
  }

  protected def shutdownActorLike(): Unit =
    println(s"Shutting down actor")

  override def run(): Unit = {
    def dequeMailbox(): Unit =
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
