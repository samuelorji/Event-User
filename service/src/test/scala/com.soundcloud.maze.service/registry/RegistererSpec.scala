package com.soundcloud.maze.service.registry

import java.io.{File, PrintWriter}

import com.soundcloud.maze.core.config.{ActorLike, ActorSystemLike}
import com.soundcloud.maze.service.client.Registerer
import com.soundcloud.maze.service.client.Registerer.RegisterNewClient
import com.soundcloud.maze.service.writer.SocketConnectionWriter
import org.scalatest.{FlatSpec, Matchers}
import org.scalatest.concurrent.Eventually
import org.scalatest.concurrent.PatienceConfiguration.Timeout
import org.scalatest.time.Span.convertSpanToDuration

import scala.concurrent.duration.FiniteDuration




