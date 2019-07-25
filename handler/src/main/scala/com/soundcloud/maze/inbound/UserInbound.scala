package com.soundcloud.maze.inbound

import java.io.{File, PrintWriter}

import scala.collection.mutable.ListBuffer

import akka.actor.{ActorRef, PoisonPill, Props}
import akka.io.Tcp.Close

import com.soundcloud.maze.action.events._
import com.soundcloud.maze.registry.ActiveUsersRegistry
import com.soundcloud.maze.util.UserSocketBase

trait InboundEventHandlerProps {
  def props(connection : ActorRef) : Props
}

object UserInbound{
  case class UnOrderedEvents(events : List[Event])
  case object StartDispatch
  case object Done
  case class StatusBroadcast(status : String) //Broadcasted status message
  def props(connection : ActorRef) = Props(new UserInbound(connection))
}
class UserInbound (
  val connection : ActorRef
  ) extends UserSocketBase{
  private[this] var followers = Map[String,Option[ActorRef]]()
  private[this] val eventsStore : ListBuffer[Event] = ListBuffer()

  context watch connection

  import UserInbound._
  override protected def eventHandler: Receive = {
    case req : UnOrderedEvents =>
      eventsStore.append(req.events : _*)



    case StartDispatch =>
      val events = eventsStore.toList

      val eventsOrdered =
          (events.collect { case x: Follow  => x }++
          events.collect { case x: UnFollow => x } ++
          events.collect { case x: Broadcast  => x } ++
          events.collect { case x: PrivateMessage  => x } ++
          events.collect { case x: StatusUpdate  => x }).sortBy(_.seqId)

      log.info(s"Complete  Set of events for user $userId is ${eventsOrdered.mkString(",")}")
      if(events.isEmpty){
        shutConnection
       // self ! PoisonPill
      }else{
        eventsOrdered.foreach( self ! _)
      }

    case Done =>
      shutConnection

    case req : Follow         =>

     // log.info(s"user $userId has received a follower request from ${req.from}")

      if(userId.get.contains("578")) println(s"following from user ${req.from} :[${ActiveUsersRegistry.findById(req.from)}]")

      followers += req.from -> getUserIfExists(req.from)
      //if(getUserIfExists(req.from).isDefined)
        sendMsg(req.payload)

      //log.info(s"Follower list should now include ${req.from} in ${followers.keys.mkString("[",",","]")}")

    case req : UnFollow       =>
     // log.info(s"User $userId received Unfollow ")
      followers -= req.from
//      getUserIfExists(req.from) match {
//        case Some(_) =>
//          followers -= req.from
//        case None    =>
//          followers -= req.from
//      }

      /**
        * Only the user with the status Update will receive this message as an Event
        * But all the user's followers we see the status update as a StatusBroadcast */
    case req : StatusUpdate   =>
      log.info(s"user ${userId.get} received status Update message")
      //log.info(s"Processing request $req")
     // log.info(s"User ${userId} received Status Update message from ${req.from} followers are ${followers.keys.mkString(",")}")
      if(userId.get.contains("365")) println(s"followers :[${followers.keys.mkString(",")} -> ${followers.values.mkString(",")}]")
     // println(ActiveUsersRegistry.findById(followers.keys.head))
      followers.values.filter(_.isDefined).foreach( _.get ! StatusBroadcast(req.payload))
     // self ! Done
      //shutConnection
      //sendMsg(req.payload)

      self ! Done

    case req : Broadcast      =>
      //log.info(s"user $userId received broadcast")
      sendMsg(req.payload)

    case req : PrivateMessage =>
     // log.info(s"user $userId received private Message ")
      sendMsg(req.payload)
//      getUserIfExists(req.from) match {
//        case Some(_) =>
//          log.info(s"Processing request $req")
//          sendMsg(req.payload)
//        case None    =>
//      }

    case bCast : StatusBroadcast =>

      sendMsg(bCast.status)

//    case Terminated(`self`) =>
//      // remove from Active Users
//      userId match {
//        case Some(id) =>  ActiveUsersRegistry.deleteById(id)
//        case None     =>
//      }
//
//      connection ! Close
//      self       ! PoisonPill
  }
  private def getUserIfExists(userId : String) = ActiveUsersRegistry.findById(userId)
}
