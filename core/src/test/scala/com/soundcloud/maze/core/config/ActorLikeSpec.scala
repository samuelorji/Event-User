package com.soundcloud.maze.core.config

import com.soundcloud.maze.core.TestServiceT
import org.scalatest.concurrent.PatienceConfiguration.Timeout
import org.scalatest.time.Span.convertSpanToDuration

import scala.collection.mutable.ListBuffer
import scala.concurrent.duration.FiniteDuration

class ActorLikeSpec extends TestServiceT {
  val wordToAppend = "Hello World"

  "The ActorLike Instance" should
    "properly process messages by appending to buffer "  in {

    val buffer = ListBuffer[String]() //Initial empty buffer


    /*This actor simply appends a received string to a buffer */
    val testActorLike  = new ActorLike {
      override protected def receive: PartialFunction[Any, Unit] = {
        case msg : String  => buffer.append(msg)
      }
    }

    val testActor = system.execute(testActorLike)
    testActor ! wordToAppend
    eventually(buffer.head should be(wordToAppend))
  }

  it should "Terminate accordingly when it receives a shutdown message and not process any message thereafter " in {

    val expectedErrorMessage = "Actor Cannot receive any message after ShutDown"

    val buffer = ListBuffer[String]() //Initial empty buffer
    val testActorLike  = new ActorLike {
      override protected def receive: PartialFunction[Any, Unit] = {
        case msg : String  => buffer.append(msg)
      }
    }
    //try {
      val testActor = system.execute(testActorLike)

      testActor ! ActorLike.Shutdown
    testActor   ! wordToAppend

//
//    }catch {
//      case ex : IllegalStateException =>
//        assert(ex.getMessage == expectedErrorMessage)
//    }expectedErrorMessage

  }


}
