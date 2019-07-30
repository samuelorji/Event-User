package com.soundcloud.maze
package web

import core.config.{ ActorLike, ActorSystemLike }
import core.util.MazeLogger

import web.handlers.{ ClientSocketHandler, EventSocketHandler }

import scala.io.StdIn

object Server extends App with MazeLogger  {
  log.info("Starting server")

  implicit val system = new ActorSystemLike

  //At Startup ...start dequeueing the each actorLike's mailbox similar to actors
  val clientHandler = system.execute(ClientSocketHandler.getClientSocketHandlerInstance)
  val eventHandler  = system.execute(EventSocketHandler.getEventSocketHandlerInstance)

  clientHandler ! ClientSocketHandler.AcceptConnections
  eventHandler  ! EventSocketHandler.StartSocket

  StdIn.readLine()

  clientHandler ! ActorLike.Shutdown
  eventHandler  ! ActorLike.Shutdown

  system.shutdownSystem()

}