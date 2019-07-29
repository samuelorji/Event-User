package com.soundcloud.maze.service

import com.soundcloud.maze.core.config.ActorSystemLike
import org.scalatest.concurrent.Eventually
import org.scalatest.concurrent.PatienceConfiguration.Timeout
import org.scalatest.time.Span.convertSpanToDuration
import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.duration.FiniteDuration

private[service] trait TestServiceT extends FlatSpec with Matchers{

  implicit val system          = ActorSystemLike.getActorSystemLikeInstance
  val timeout  = Timeout(convertSpanToDuration(FiniteDuration(1,"seconds")))
}
