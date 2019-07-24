package com.soundcloud.maze.inbound

import scala.collection.mutable

import akka.actor.{ActorRef, Props}

import com.soundcloud.maze.action.events._
import com.soundcloud.maze.registry.ActiveUsersRegistry
import com.soundcloud.maze.util.UserSocketBase

trait InboundEventHandlerProps {
  def props(connection : ActorRef) : Props
}

object UserInbound{
  def props(connection : ActorRef) = Props(new UserInbound(connection))
}
class UserInbound (
  val connection : ActorRef
  ) extends UserSocketBase{
  private var followers = Map[String,ActorRef]()

  override protected def eventHandler: Receive = {
    case req : Follow         =>

      if(ActiveUsersRegistry.findById(req.to).isDefined) {
        log.info(s"Processing request $req")
        val followed = ActiveUsersRegistry.findById(req.to).get
        followers += req.to -> followed
        followed ! req.payload
      }

    case req : UnFollow       =>
      if(ActiveUsersRegistry.findById(req.to).isDefined) {
        log.info(s"Processing request $req")
        followers -= req.to
      }

    case req : StatusUpdate   =>
      log.info(s"Processing request $req")
      followers.values.foreach( _ ! req.payload)

    case req : PrivateMessage =>

      if(ActiveUsersRegistry.findById(req.to).isDefined) {
        log.info(s"Processing request $req")
        val friend = ActiveUsersRegistry.findById(req.to).get
        friend ! req.payload
      }

    case socketMsg : String =>
      sendMsg(socketMsg)

  }
}
