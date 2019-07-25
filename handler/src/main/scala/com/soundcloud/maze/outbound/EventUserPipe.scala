package com.soundcloud.maze.outbound

import java.io.{File, PrintWriter}

import scala.collection.mutable.ListBuffer

import akka.actor.{Actor, ActorLogging, Props}

import com.soundcloud.maze.action.events._
import com.soundcloud.maze.inbound.UserInbound
import com.soundcloud.maze.inbound.UserInbound.UnOrderedEvents
import com.soundcloud.maze.registry.ActiveUsersRegistry

object EventUserPipe {
  case class Events(events : List[Event])
  case object StartDispatch
  def props : Props = Props[EventUserPipe]
}
private[outbound] class EventUserPipe extends Actor with ActorLogging {

  private var events : ListBuffer[Event] = ListBuffer()
  import EventUserPipe._
  override def receive: Receive = {

    case StartDispatch =>
     // println(ActiveUsersRegistry.getAllKeys.mkString("*"))
      println(s"length of Active all registered is  ${ActiveUsersRegistry.getAllKeys.length}")
     val printer=  new PrintWriter(new File("/var/tmp/log/maze/events.log"))
      printer.write(events.toList.mkString("\n"))
      printer.flush()
      printer.close()
      ActiveUsersRegistry.getAllValues.foreach(_ ! UserInbound.StartDispatch)
    case req : Events =>
      //events.append(req.events : _*)
      val validEvents =
        req.events.collect{
        case x : Event if x.isValid => x
      }

      events.append(validEvents : _*)
//      val validEventsOrdered =
//          validEventsUnOrdered.collect{case x : Event if x.isInstanceOf[UnFollow] => x} ++
//          validEventsUnOrdered.collect{case x : Event if x.isInstanceOf[PrivateMessage] => x} ++
//          validEventsUnOrdered.collect{case x : Event if x.isInstanceOf[StatusUpdate] => x} ++
//          validEventsUnOrdered.collect{case x : Event if x.isInstanceOf[Follow] => x} ++
//          validEventsUnOrdered.collect{case x : Event if x.isInstanceOf[Broadcast] => x}
//
//
//      validEventsOrdered.foreach{
//        case f @ Follow(_,to,_)           =>
//          getUserIfExists(to) match {
//            case Some(user) => user ! f
//            case None       =>
//          }
//        case u @ UnFollow(_,to,_)         =>
//          getUserIfExists(to) match {
//            case Some(user) => user ! u
//            case None       =>
//          }
//
//        case b @ Broadcast(_)             =>
//          ActiveUsersRegistry.getAll.foreach( _ ! b)
//
//        case p @ PrivateMessage(_, to ,_) =>
//          getUserIfExists(to) match {
//            case Some(user) => user ! p
//            case None       =>
//          }
//
//        case s @ StatusUpdate(from, _)    =>
//          getUserIfExists(from) match {
//            case Some(user) => user ! s
//            case None       =>
//          }
//      }

    val eventsMap = validEvents.foldLeft(Map[String,List[Event]]()){
      case (map1,event) =>
        event match {

          case f@Follow(_, to, _) =>
            map1.updated(to,map1.getOrElse(to,Nil) :+ f)

          case u@UnFollow(_, to, _) =>
            map1.updated(to,map1.getOrElse(to,Nil) :+ u)

          case b@Broadcast(_) =>
            map1.map(x => x._1 ->  x._2.:+(b))

          case p@PrivateMessage(_, to, _) =>
            map1.updated(to,map1.getOrElse(to,Nil) :+ p)

          case s@StatusUpdate(from, _) =>
            map1.updated(from,map1.getOrElse(from,Nil) :+ s)

        }
    }

      eventsMap.foreach{
        case (user, events) =>
          getUserIfExists(user) match {
            case Some(x) => x ! UnOrderedEvents(events)
            case None    =>
          }
      }


  }

 private def getUserIfExists(userId : String) = ActiveUsersRegistry.findById(userId)
}
