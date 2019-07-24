package com.soundcloud.maze.util

import akka.actor.Actor.Receive
import akka.actor.{Actor, ActorLogging, ActorRef}
import akka.io.Tcp.Write
import akka.util.Timeout
import akka.io.{IO, Tcp}
import akka.util.ByteString

import com.soundcloud.maze.config.MazeConfig

trait EventSocketBase extends Actor with ActorLogging {

  protected val connection  : Option[ActorRef] /**In case we may want to write to the event socket in future*/
  protected val userHandler : ActorRef

  implicit val timeout = Timeout(MazeConfig.socketsTimeout)

  protected def eventHandler : Receive
  private def socketMessageHandler : Receive = {
    case Tcp.Received(data) =>
      val eventsStr = data.utf8String
      val event  = EventParser.parse(eventsStr)
      userHandler ! event

    case _ : Tcp.ConnectionClosed =>
      log.info("Connection [Event-Socket-Message-Handler] closed")
      context stop self
  }

  /**
    * These methods are helper methods, if we may want to write to the Event TCP connection*/
  protected def sendMsg(msg : String) = {
    log.info(s"Writing to Connection message $msg")
    //This method can be enhanced if the message may be modified
    if(connection.isDefined) writeToConnection(msg)
  }

  private def writeToConnection(msg : String): Unit = {
    connection.get ! Write(ByteString(msg))
  }


  override def receive: Receive = eventHandler orElse socketMessageHandler



}
