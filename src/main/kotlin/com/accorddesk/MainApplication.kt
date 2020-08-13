package com.accorddesk

import io.micronaut.runtime.Micronaut

//fun main(args: Array<String>) {
//	build()
//	    .args(*args)
//	    .packages("com.accorddesk")
//	    .start()
//}

object MainApplication {
    @JvmStatic
    fun main(args: Array<String>) {
        val appContext = Micronaut.build(*args)
//                .environments("default")
//                .eagerInitSingletons(true) // for Db.CONNECT in PostConstruct()
                .mainClass(MainApplication.javaClass)
                .start()
    }
}