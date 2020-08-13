package com.accorddesk.api.ndadesk.frontend.ws

import io.micronaut.http.HttpRequest
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import javax.inject.Inject

@MicronautTest
class FrontendDefaultWSTest {
    @Inject
    @field:Client("/")
    var client: RxHttpClient? = null

    @Test
    fun testHello() {
        val request: HttpRequest<String> = HttpRequest.GET("/frontend")
        val body = client!!.toBlocking().retrieve(request)
        assertNotNull(body)
        assertEquals("Hello Frontend Default World!", body)
    }
}
