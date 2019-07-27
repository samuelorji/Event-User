package com.soundcloud.maze.dubs.actors

import java.util.concurrent.Executors

class ActorSystemLike {

  private val pool = Executors.newCachedThreadPool() // Work is mostly IO

  def runTask (runnable: Runnable) ={
    pool.execute(runnable)
  }
  def execute(actorRef : ActorLike) : ActorLike = {
    pool.execute(actorRef)
    actorRef
  }

  def shutdownSystem() : Unit = {
    pool.shutdownNow()
  }
}

