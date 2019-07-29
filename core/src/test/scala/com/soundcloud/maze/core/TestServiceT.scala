package com.soundcloud.maze.core

import com.soundcloud.maze.core.config.ActorSystemLike
import org.scalatest.{FlatSpec, Matchers}
import org.scalatest.concurrent.Eventually
import org.scalatest.concurrent.PatienceConfiguration.Timeout
import org.scalatest.time.Span.convertSpanToDuration

import scala.concurrent.duration.FiniteDuration

private[core] trait TestServiceT extends FlatSpec with Matchers with Eventually {

  implicit val system = ActorSystemLike.getActorSystemLikeInstance
  implicit val eventuallySpan  = Timeout(convertSpanToDuration(FiniteDuration(1,"second")))
}
