package com.accorddesk.api.ndadesk.frontend.ws

import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get

@Controller("/frontend")
class FrontendDefaultWS {

    @Get(produces = [MediaType.TEXT_PLAIN])
    fun index(): String {
        return "Hello Frontend Default World!"
    }
}