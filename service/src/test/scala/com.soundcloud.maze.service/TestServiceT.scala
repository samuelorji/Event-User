package com.soundcloud.maze.service

import com.soundcloud.maze.core.config.ActorSystemLike
import org.scalatest.concurrent.Eventually
import org.scalatest.concurrent.PatienceConfiguration.Timeout
import org.scalatest.time.Span.convertSpanToDuration
import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.duration.FiniteDuration

private[service] trait TestServiceT extends FlatSpec with Matchers with Eventually  {

  implicit val system  = ActorSystemLike.getActorSystemLikeInstance
 implicit val timeout  = Timeout(convertSpanToDuration(FiniteDuration(10,"millis")))
}
