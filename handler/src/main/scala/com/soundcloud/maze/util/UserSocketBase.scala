package com.soundcloud.maze.util

import akka.actor.{Actor, ActorLogging, ActorRef}
import akka.io.Tcp
import akka.io.Tcp.Write
import akka.util.{ByteString, Timeout}

import com.soundcloud.maze.config.MazeConfig
import com.soundcloud.maze.registry.ActiveUsersRegistry

trait UserSocketBase extends Actor with ActorLogging {

  protected val connection  : ActorRef
  implicit val timeout = Timeout(MazeConfig.socketsTimeout)
  protected[this] var userId : Option[String] = None

  protected def eventHandler : Receive
  private def socketMessageHandler : Receive = {
    case Tcp.Received(data) =>
      val userStr = data.filter(_.toInt != 10).utf8String //removing '\n' from the data
      val user    = UserParser.parse(userStr.trim)

      ActiveUsersRegistry.findById(user.username) match {
        case Some(_) =>

        case None       =>


          userId = Some(user.username)
          log.info(s"user id ${userId.get} has registered")
          ActiveUsersRegistry.add(user.username, self)
      }


    case _ : Tcp.ConnectionClosed =>
     // log.info("Connection [User-Socket-Message-Handler] closed")
      connection ! Tcp.Close
      if(userId.isDefined) ActiveUsersRegistry.deleteById(userId.get)
      context stop self

  }


  protected def sendMsg(msg : String) = {
    log.info(s"Writing to Connection message $msg")
    //This method can be enhanced if the message may be modified
     writeToConnection(msg)
  }

  private def writeToConnection(msg : String): Unit = {
    connection ! Write(ByteString(msg +"\n"))
  }

  def shutConnection = connection ! Tcp.Close


  override def receive: Receive = eventHandler orElse socketMessageHandler


}
