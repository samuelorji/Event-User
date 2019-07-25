package com.soundcloud.maze.util

import akka.actor.{Actor, ActorLogging, ActorRef}
import akka.io.Tcp
import akka.io.Tcp.Write
import akka.util.{ByteString, Timeout}

import com.soundcloud.maze.config.MazeConfig
import com.soundcloud.maze.outbound.EventUserPipe

trait EventSocketBase extends Actor with ActorLogging {

  protected val connection  : ActorRef  /**In case we may want to write to the event socket in future*/
  protected val userHandler : ActorRef

  implicit val timeout = Timeout(MazeConfig.socketsTimeout)

  protected def eventHandler : Receive
  private def socketMessageHandler : Receive = {
    case Tcp.Received(data) =>
      val eventsStr = data.utf8String
      val event     = EventParser.parse(eventsStr)
      userHandler ! EventUserPipe.Events(event)

    case _ : Tcp.ConnectionClosed =>
      userHandler ! EventUserPipe.StartDispatch
      context stop self

  }

  /**
    * These methods are helper methods, if we may want to write to the Event TCP connection*/
  protected def sendMsg(msg : String) = {
    //This method can be enhanced if the message may be modified
    writeToConnection(msg)
  }

  private def writeToConnection(msg : String): Unit = {
    connection ! Write(ByteString(msg))
  }


  override def receive: Receive = eventHandler orElse socketMessageHandler



}
