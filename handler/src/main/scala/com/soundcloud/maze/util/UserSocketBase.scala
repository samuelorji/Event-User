package com.soundcloud.maze.util

import java.util.concurrent.ConcurrentHashMap

import akka.actor.{Actor, ActorLogging, ActorRef}
import akka.io.Tcp
import akka.io.Tcp.Write
import akka.util.{ByteString, Timeout}

import com.soundcloud.maze.config.MazeConfig

trait UserSocketBase extends Actor with ActorLogging {

  protected val connection  : ActorRef

  private val users = new ConcurrentHashMap[String,ActorRef]()
  implicit val timeout = Timeout(MazeConfig.socketsTimeout)

  protected def eventHandler : Receive
  private def socketMessageHandler : Receive = {
    case Tcp.Received(data) =>
      val eventsStr = data.utf8String
      val event  = EventParser.parse(eventsStr)


    case _ : Tcp.ConnectionClosed =>
      log.info("Connection [Event-Socket-Message-Handler] closed")
      context stop self
  }

  /**
    * These methods are helper methods, if we may want to write to the Event TCP connection*/
  protected def sendMsg(msg : String) = {
    log.info(s"Writing to Connection message $msg")
    //This method can be enhanced if the message may be modified
     writeToConnection(msg)
  }

  private def writeToConnection(msg : String): Unit = {
    connection ! Write(ByteString(msg))
  }


  override def receive: Receive = eventHandler orElse socketMessageHandler


}
