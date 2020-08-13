package com.accorddesk.api.ndadesk.backend.ws

import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get

@Controller("/backend")
class BackendDefaultWS {

    @Get(produces = [MediaType.TEXT_PLAIN])
    fun index(): String {
        return "Hello Backend Default World!"
    }
}