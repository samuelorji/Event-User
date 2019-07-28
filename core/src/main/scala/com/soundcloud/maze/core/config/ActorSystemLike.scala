package com.soundcloud.maze.core.config

import java.util.concurrent.Executors

class ActorSystemLike /*More or less an executor Service*/ {

  private val pool = Executors.newCachedThreadPool() // Work is mostly IO

  def execute(actorRef: ActorLike): ActorLike = {
    pool.execute(actorRef)
    actorRef
  }

  def shutdownSystem(): Unit = {
    pool.shutdownNow()
  }
}

