package com.soundcloud.maze.dubs.actors

import java.net.ServerSocket

import com.soundcloud.maze.dubs.event.Events

import scala.language.postfixOps
import scala.collection.mutable.PriorityQueue
import scala.io.BufferedSource

object EventSocketHandler {
  case object StartSocket
}
class EventSocketHandler(router : ActorLike , connectionPort : Int) extends ActorLike {

  var socket : ServerSocket = _

  import EventSocketHandler._

  override protected def receive: PartialFunction[Any, Unit] = {
    case StartSocket =>
      socket = new ServerSocket(connectionPort) //accept single event connection
      var counter = 1
      val eventPriorityQueue = PriorityQueue()(Events.EventQueueOrdering).reverse // Ensures elements are properly sorted and reversed for first in first Out
    val events = new BufferedSource(socket.accept().getInputStream).getLines()
      //val hasAdjacentMessages = () => eventQueue.nonEmpty && eventQueue.head.seqId == messageCounter
      val hasNext = () =>  eventPriorityQueue.nonEmpty && eventPriorityQueue.head.seqId == counter


      events.flatMap(Events.Event.apply)
      .foreach { event =>
        eventPriorityQueue += event
        while(hasNext()){
          router ! eventPriorityQueue.dequeue() // send message to the actorLike mailbox
          counter += 1
        }
      }
  }

  override def onShutdown() = {
    socket.close()
    super.onShutdown()
  }
}
