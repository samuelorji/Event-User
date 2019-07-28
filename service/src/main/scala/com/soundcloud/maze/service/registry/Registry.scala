package com.soundcloud.maze.service.registry

import com.soundcloud.maze.core.config.ActorLike

import scala.collection.mutable

object UserRegistry {
  private val users                        = mutable.Map[Int, ActorLike]()
  def addUser(key : Int,value : ActorLike) = users.put(key,value)
  def findUser(user : Int)                 = users.get(user)
  def getAllUsers                          = users

}

object FollowerRegistry{
  //this class should not be accessed outside the package [registry] for proper encapsulation
  private[registry]class UserToFollowers extends mutable.HashMap[Int, mutable.Set[Int]] with mutable.MultiMap[Int, Int]

  private  val followers : UserToFollowers = new UserToFollowers
  def addFollow(to : Int ,from : Int)      = followers.addBinding(to,from)
  def removeFollow(to : Int ,from : Int)   = followers.removeBinding(to,from)
  def getFollow(from : Int)                = followers.get(from)

}
