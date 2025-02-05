package wyrd.sister.URLshortener

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.servers.Server
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenAPIConfiguration {

    @Bean
    fun defineOpenAPI() : OpenAPI{
        val server = Server()
        server.url = "http://localhost:8080"

        val contact = Contact()
        contact.name = "Elizaveta Karpova"
        contact.email = "elizabethvalerivna97@gmail.com"

        val info = Info()
        info.contact = contact
        info.title = "URL shortener API"
        info.version = "1.0"
        info.description = "This API exposes endpoints to shorten URL."

        val openAPI = OpenAPI()
        openAPI.info = info
        openAPI.servers = listOf(server)

        return openAPI
    }
}