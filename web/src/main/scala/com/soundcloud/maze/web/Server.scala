package com.soundcloud.maze.web

import com.soundcloud.maze.core.config.{ActorLike, ActorSystemLike}
import com.soundcloud.maze.core.util.MazeLogger
import com.soundcloud.maze.web.handlers.ClientSocketHandler.AcceptConnections
import com.soundcloud.maze.web.handlers.EventSocketHandler.StartSocket
import com.soundcloud.maze.web.handlers.{ClientSocketHandler, EventSocketHandler}

import scala.io.StdIn


object Server extends App with MazeLogger  {
  log.info("Starting server")

  implicit val system = new ActorSystemLike

  //At Startup ...start dequeueing the each actorLike's mailbox similar to actors
  val clientHandler = system.execute(new ClientSocketHandler)
  val eventHandler  = system.execute(new EventSocketHandler)

  clientHandler ! AcceptConnections
  eventHandler  ! StartSocket

  StdIn.readLine()

  clientHandler ! ActorLike.Shutdown
  eventHandler ! ActorLike.Shutdown

  system.shutdownSystem()

}