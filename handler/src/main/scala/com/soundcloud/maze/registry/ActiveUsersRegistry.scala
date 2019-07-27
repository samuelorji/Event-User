package com.soundcloud.maze.registry

import java.util.concurrent.ConcurrentHashMap

import scala.collection.JavaConverters._
import akka.actor.ActorRef

import scala.collection.mutable

object ActiveUsersRegistry {
  private val usersActorMap = new ConcurrentHashMap[String/*User ID's*/,ActorRef/*InboundUserHandler*/]
  val mutableMaps = mutable.Map[String, ActorRef]()

  def addMutable(id : String, ref : ActorRef) = mutableMaps.put(id,ref)



  def add(id : String, ref : ActorRef) : Option[ActorRef] = {
    usersActorMap.put(id,ref) match {
      case null => None
      case x    => Some(x)
    }
  }

  def getAllValues : List[ActorRef] = usersActorMap.values().asScala.toList

  def getAllKeys : List[String]  = usersActorMap.keys().asScala.toList

  def findById(id : String) : Option[ActorRef] = {
    usersActorMap.get(id) match {
      case null => None
      case x    => Some(x)
    }
  }


  def deleteById(id : String) : Option[ActorRef] = {
    usersActorMap.remove(id) match {
      case null => None
      case x    => Some(x)
    }
  }

}
