package com.soundcloud.maze
package service.registry

import core.config.ActorLike

import scala.collection.mutable
import scala.collection.mutable.{HashMap, Set}

/**
  * Holds clients details as an Id and Its ActorLike (TCP connection)*/
object UserRegistry {
  private var users                        = Map[Int, ActorLike]()
  def addUser(key : Int,value : ActorLike) = users += key -> value
  def findUser(user : Int)                 = users.get(user)
  def getAllUsers                          = users

}

/**
  * Holds follow details for each user , a multi map was used so as not to replace values when adding to the map
  * Instead, a set is created for the extra value when an initial value was found*/
object FollowerRegistry{
  //this class should not be accessed outside the package [registry] for proper encapsulation
  private[registry]class UserToFollowers extends HashMap[Int, Set[Int]] with mutable.MultiMap[Int, Int]

  private  val followers : UserToFollowers = new UserToFollowers
  def addFollow(to : Int ,from : Int)      = followers.addBinding(to,from)
  def removeFollow(to : Int ,from : Int)   = followers.removeBinding(to,from)
  def getFollow(from : Int)                = followers.get(from)

}
