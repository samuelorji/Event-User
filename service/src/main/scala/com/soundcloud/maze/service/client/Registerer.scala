package com.soundcloud.maze.service.client

import java.io.PrintWriter

import com.soundcloud.maze.core.config.{ActorLike, ActorSystemLike}
import com.soundcloud.maze.core.util.MazeLogger
import com.soundcloud.maze.service.registry.UserRegistry
import com.soundcloud.maze.service.writer.SocketConnectionWriter

object Registerer {
  case class RegisterNewClient(userId : Int, client : PrintWriter)
}
class Registerer(implicit system : ActorSystemLike) extends ActorLike with MazeLogger {
  import Registerer._
  override protected def receive: PartialFunction[Any, Unit] = {
    case RegisterNewClient(id, out) =>
      log.info(s"Registering New Client : {}",id)
      UserRegistry.addUser(id,system.execute(SocketConnectionWriter(out)))

  }
}
