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

  import EventUserPipe._
  override def receive: Receive = {

    case StartDispatch =>
     // println(ActiveUsersRegistry.getAllKeys.mkString("*"))
      println(s"length of Active all registered is  ${ActiveUsersRegistry.getAllKeys.length}")
      ActiveUsersRegistry.getAllValues.foreach(_ ! UserInbound.StartDispatch)
    case req : Events =>
      //events.append(req.events : _*)
      val validEvents =
        req.events.collect{
        case x : Event if x.isValid => x
      }

    val eventsMap = validEvents.foldLeft(Map[String,List[Event]]()){
      case (map1,event) =>
        event match {

          case f@Follow(_,_, to, _) =>
            map1.updated(to,map1.getOrElse(to,Nil) :+ f)

          case u@UnFollow(_,_, to, _) =>
            map1.updated(to,map1.getOrElse(to,Nil) :+ u)

          case b@Broadcast(_,_) =>
            map1.map(x => x._1 ->  x._2.:+(b))

          case p@PrivateMessage(_,_, to, _) =>
            map1.updated(to,map1.getOrElse(to,Nil) :+ p)

          case s@StatusUpdate(_,from, _) =>
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
