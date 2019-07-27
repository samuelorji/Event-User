package com.soundcloud.maze.dubs.actors.registry

import com.soundcloud.maze.dubs.actors.{ActorLike, UserToFollowers}

import scala.collection.mutable
import scala.collection.mutable.Map

object UserRegistry {
  //val users : Map[String, ActorLike] = Map[String, ActorLike]()
  private val clientConnections: mutable.Map[Int, ActorLike] = mutable.Map[Int, ActorLike]()
  def addUser(key : Int,value : ActorLike) = clientConnections.put(key,value)
  def findUser(user : Int) = clientConnections.get(user)
  def getAllUsers = clientConnections.values


}
object FollowerRegistry{
  private  val followers : UserToFollowers = new UserToFollowers
  def addFollow(to : Int ,from : Int) = followers.addBinding(to,from)
  def removeFollow(to : Int ,from : Int) = followers.removeBinding(to,from)
  def getFollow(from : Int) = followers.get(from)

}
