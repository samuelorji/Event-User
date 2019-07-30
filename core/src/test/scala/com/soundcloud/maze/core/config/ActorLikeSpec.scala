package com.soundcloud.maze
package core.config

import core.TestServiceT

import scala.collection.mutable.ListBuffer

class ActorLikeSpec extends TestServiceT {
  val wordToAppend = "Hello World"

  "The ActorLike Instance" should
    "properly process messages by appending to an internal buffer "  in {


    /*This actor simply appends a received string to a buffer */
    val testActor = TestActorLike()

    system.execute(testActor)
    testActor ! wordToAppend
    eventually(testActor.getElements.head should be(wordToAppend))
  }
}
object TestActorLike {
  def apply(): TestActorLike = new TestActorLike()
}
 class TestActorLike extends ActorLike{
  private val buffer = ListBuffer[String]()
  override protected def receive: PartialFunction[Any, Unit] = {
    case msg : String  => buffer.append(msg)
  }

  def getElements = buffer.toList

}
