package wyrd.sister.URLshortener.controllers.v1

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toKotlinInstant
import kotlinx.datetime.toKotlinLocalDateTime
import kotlinx.datetime.toLocalDateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.HttpStatusCodeException
import org.springframework.web.server.ResponseStatusException
import wyrd.sister.URLshortener.controllers.v1.dao.LongURLDto
import wyrd.sister.URLshortener.controllers.v1.dao.ShortUrlDto
import wyrd.sister.URLshortener.services.ShortenedURLService
import wyrd.sister.URLshortener.utils.isValidURL

@RestController
@RequestMapping("/api/v1")
class ShortenedURLController(
    @Autowired val shortenedURLService: ShortenedURLService
) {
    private val basicShortUrl = "https://ka.pro/"


    @PostMapping("/shorten")
    fun storten(@RequestBody body: LongURLDto): ShortUrlDto {
        if (!body.longUrl.isValidURL()) {
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Given property \"longUrl\"=${body.longUrl} isn't URL!"
            )
        }

        val newShortenedURL = shortenedURLService.shortenURL(body.longUrl)
        return ShortUrlDto(
            shortUrl = "$basicShortUrl/${newShortenedURL.shortCode}",
            expiredAt = newShortenedURL.expiredAt.toKotlinInstant().toLocalDateTime(TimeZone.UTC)
        )
    }

    @GetMapping("/{shortCode}")
    fun redirectToOriginalURL(@PathVariable("shortCode") shortCode: String) {

    }

    @GetMapping("/url/{shortCode}")
    fun getOriginalURL(@PathVariable("shortCode") shortCode: String) {

    }

    @GetMapping("/stats/{shortCode}")
    fun getStats(@PathVariable("shortCode") shortCode: String) {

    }

}
