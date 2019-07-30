package com.soundcloud.maze
package web.handlers

import java.net.ServerSocket

import scala.collection.mutable.PriorityQueue
import scala.io.BufferedSource
import scala.language.postfixOps

import core.action.events.Events
import core.config.{ ActorLike, ActorSystemLike, MazeConfig }

import service.router.Router

object EventSocketHandler {
  case object StartSocket
  def getEventSocketHandlerInstance(implicit system : ActorSystemLike) = new EventSocketHandler()
}
private[web] class EventSocketHandler(implicit  system : ActorSystemLike) extends ActorLike {

  implicit val ordering = Events.eventOrdering
  var socket : Option[ServerSocket] = None

  import EventSocketHandler._

  private val router = createRouter
  def createRouter   = system.execute(Router.getRouterInstance)

  override protected def receive: PartialFunction[Any, Unit] = {
    case StartSocket =>
      socket                 = Some(new ServerSocket(MazeConfig.mazeEventPort)) //accept single event connection
      var counter            = 1
      val eventPriorityQueue = PriorityQueue()// Ensures elements are properly queued by priority
      val events             = new BufferedSource(socket.get.accept().getInputStream).getLines()
      val hasNext = () =>     !eventPriorityQueue.isEmpty && eventPriorityQueue.head.seqId == counter

      events.flatMap(Events.parseEvent).foreach { event =>
        eventPriorityQueue enqueue  event
        while(hasNext()){
          router ! eventPriorityQueue.dequeue() // send message to the actorLike mailbox
          counter += 1
        }
      }
  }

  override def shutdownActorLike() = {
    if(socket.isDefined) socket.get.close()
    super.shutdownActorLike()
  }
}
