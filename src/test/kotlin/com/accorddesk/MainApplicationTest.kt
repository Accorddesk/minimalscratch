package com.accorddesk

import io.micronaut.runtime.server.EmbeddedServer
import io.micronaut.test.annotation.MicronautTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import javax.inject.Inject

@MicronautTest
class MainApplicationTest {

    @Inject
    lateinit var embeddedServer: EmbeddedServer

    @Test
    fun testItWorks() {
        Assertions.assertTrue(embeddedServer.applicationContext.isRunning)
    }

}