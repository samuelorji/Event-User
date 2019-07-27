//package com.soundcloud.maze.inbound
//
//import java.io.{File, PrintWriter}
//
//import scala.collection.mutable.ListBuffer
//
//import akka.actor.{ActorRef, PoisonPill, Props}
//import akka.io.Tcp.Close
//
//import com.soundcloud.maze.action.events._
//import com.soundcloud.maze.registry.ActiveUsersRegistry
//import com.soundcloud.maze.util.UserSocketBase
//
//trait InboundEventHandlerProps {
//  def props(connection : ActorRef) : Props
//}
//
//object UserInbound{
//  case class UnOrderedEvents(events : List[Event])
//  case object StartDispatch
//  case object Done
//  case class StatusBroadcast(status : String) //Broadcasted status message
//  def props(connection : ActorRef) = Props(new UserInbound(connection))
//}
//class UserInbound (
//  val connection : ActorRef
//  ) extends UserSocketBase{
//  private[this] var followers = Map[String,String]()
//  private[this] val eventsStore : ListBuffer[Event] = ListBuffer()
//
//  context watch connection
//
//  import UserInbound._
//  override protected def eventHandler: Receive = {
//    case req : UnOrderedEvents =>
//      eventsStore.append(req.events : _*)
//
//
//
//    case StartDispatch =>
//      val events = eventsStore.toList
//
//      val eventsOrdered =
//          events.sortBy(_.seqId)
//
//      log.info(s"Complete  Set of events for user $userId is ${eventsOrdered.mkString(",")}")
//      if(events.isEmpty){
//        shutConnection
//       // self ! PoisonPill
//      }else{
//        eventsOrdered.foreach( self ! _)
//      }
//
//    case Done =>
//      shutConnection
//
//    case req : Follow         =>
//
//      //if(userId.get.contains("578")) println(s"following from user ${req.from} :[${ActiveUsersRegistry.findById(req.from)}]")
//
//      followers += req.from -> req.from//getUserIfExists(req.from)
//
//        sendMsg(req.payload)
//
//
//payload
//    case req : UnFollow       =>
//
//      followers -= req.from
//
//
//      /**
//        * Only the user with the status Update will receive this message as an Event
//        * But all the user's followers we see the status update as a StatusBroadcast */
//    case req : StatusUpdate   =>
//     // log.info(s"user ${userId.get} received status Update message")
//
//     // if(userId.get.contains("365")) println(s"followers :[${followers.keys.mkString(",")} -> ${followers.values.mkString(",")}]")
//      followers.values.foreach(ActiveUsersRegistry.findById(_).get ! StatusBroadcast(req.payload))
//
//      //self ! Done
//
//    case req : Broadcast      =>
//
//      sendMsg(req.payload)
//
//    case req : PrivateMessage =>
//
//      sendMsg(req.payload)
//
//
//    case bCast : StatusBroadcast =>
//
//      sendMsg(bCast.status)
//
//
//  }
//  private def getUserIfExists(userId : String) = ActiveUsersRegistry.findById(userId)
//}
