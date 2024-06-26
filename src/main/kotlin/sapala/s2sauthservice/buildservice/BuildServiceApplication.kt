package sapala.s2sauthservice.buildservice

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.event.EventListener
import sapala.s2sauthservice.buildservice.config.Env


@SpringBootApplication
@ComponentScan("sapala.s2sauthservice.buildservice", "sapala.s2sauthservice.api")
class BuildServiceApplication(private val env: Env) {
    companion object {
        val log: Logger = LoggerFactory.getLogger(BuildServiceApplication::class.java)
    }

    @EventListener(ApplicationReadyEvent::class)
    fun afterStartup() {
        log.info("Swagger UI available at: https://localhost:{}/swagger-ui/index.html", env.port())
    }
}

fun main(args: Array<String>) {
    runApplication<BuildServiceApplication>(*args)
}
