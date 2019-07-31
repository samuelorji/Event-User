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
/**
  * This Actor exposes a TCP connection for just a single Event Source to connect. Hence no need for a tail recursion as in the client Handler */
private[web] class EventSocketHandler(implicit  system : ActorSystemLike) extends ActorLike {

  implicit val ordering             = Events.eventOrdering
  var socket : Option[ServerSocket] = None

  import EventSocketHandler._

  private val router = createRouter
  def createRouter   = system.execute(Router.getRouterInstance)

  override protected def receive: PartialFunction[Any, Unit] = {
    case StartSocket =>
      socket                 = Some(createSocket) //accept single event connection
      val eventPriorityQueue = PriorityQueue()// Ensures elements are properly queued by priority
      var counter            = 1
      val hasNext = () =>     !eventPriorityQueue.isEmpty && eventPriorityQueue.head.seqId == counter
      val events             = new BufferedSource(socket.get.accept().getInputStream).getLines()


      events.flatMap(Events.parseEvent).foreach { event =>
        eventPriorityQueue enqueue  event
        while(hasNext()){
          router ! eventPriorityQueue.dequeue() // send message to the actorLike mailbox
          counter += 1
        }
      }
  }

  def createSocket = new ServerSocket(MazeConfig.mazeEventPort)

  override def shutdownActorLike() = {
    if(socket.isDefined) socket.get.close()
    super.shutdownActorLike()
  }
}
