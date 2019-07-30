package com.soundcloud.maze.
package service.client

import java.io.PrintWriter

import service.TestServiceT
import service.client.Registerer.RegisterNewClient
import service.registry.UserRegistry

import org.scalatestplus.mockito.MockitoSugar.mock

class RegistererSpec extends TestServiceT {

  "The Registerer Actor " should "eventually process messages and register a new client " in {

    val testRegisterer  = system.execute(new Registerer)
    val mockprintWriter = mock[PrintWriter]
    testRegisterer ! RegisterNewClient(1, mockprintWriter)

    eventually {
      assert(UserRegistry.getAllUsers.get(1).isDefined) //client gets added to the map eventually

    }
  }
}
