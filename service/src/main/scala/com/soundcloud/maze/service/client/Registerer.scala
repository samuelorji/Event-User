package com.soundcloud.maze
package service.client

import java.io.PrintWriter

import core.config.{ ActorLike, ActorSystemLike }
import core.util.MazeLogger

import service.registry.UserRegistry
import service.writer.SocketConnectionWriter

object Registerer {
  case class RegisterNewClient(userId : Int, client : PrintWriter)
  def getRegistererInstance(implicit system : ActorSystemLike) = new Registerer()
}

private[service] class Registerer(implicit system : ActorSystemLike) extends ActorLike with MazeLogger {
  import Registerer._

  override protected def receive: PartialFunction[Any, Unit] = {
    case RegisterNewClient(id, out) =>
      log.info(s"Registering New Client : {}",id)
      UserRegistry.addUser(id,system.execute(SocketConnectionWriter(out)))

  }
}
