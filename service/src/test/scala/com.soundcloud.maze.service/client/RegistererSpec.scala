package com.soundcloud.maze.service.client

import java.io.{File, PrintWriter}

import com.soundcloud.maze.core.config.ActorSystemLike
import com.soundcloud.maze.service.TestServiceT
import com.soundcloud.maze.service.client.Registerer.RegisterNewClient
import com.soundcloud.maze.service.registry.UserRegistry
import org.scalatest.{FlatSpec, Matchers}
import org.scalatest.concurrent.Eventually
import org.scalatest.concurrent.PatienceConfiguration.Timeout
import org.scalatest.time.Span.convertSpanToDuration

import scala.concurrent.duration.FiniteDuration

class RegistererSpec extends TestServiceT with Eventually  {

  "The Registerer Actor " should "process messages and register a new client " in {
    //create the system here

    val x = system.execute(new Registerer)

    val file            = new File("/Users/samuel/Desktop/file.txt")
    val filePrintWriter = new PrintWriter(file)

    x ! RegisterNewClient(1, filePrintWriter)

    assert(UserRegistry.getAllUsers.get(1).isEmpty) //initially no client has been added

    eventually {
      UserRegistry.getAllUsers.get(1).isDefined //User gets added to the map eventually
    }
  }
}
