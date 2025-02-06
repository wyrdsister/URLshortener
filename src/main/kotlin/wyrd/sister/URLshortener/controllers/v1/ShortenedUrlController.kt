package wyrd.sister.URLshortener.controllers.v1

import io.swagger.v3.oas.annotations.Operation
import kotlinx.datetime.toKotlinInstant
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.servlet.ModelAndView
import wyrd.sister.URLshortener.controllers.v1.dto.LongUrlDto
import wyrd.sister.URLshortener.controllers.v1.dto.LongUrlInfoDto
import wyrd.sister.URLshortener.controllers.v1.dto.ShortUrlDto
import wyrd.sister.URLshortener.controllers.v1.dto.ShortUrlAnalyticsDto
import wyrd.sister.URLshortener.services.ShortenedUrlService
import wyrd.sister.URLshortener.utils.isValidURL
import kotlin.concurrent.thread

@RestController
@RequestMapping
class ShortenedUrlController(
    @Autowired val shortenedURLService: ShortenedUrlService
) {
    private val basicShortUrl = "http://localhost:8080"

    @Operation(
        summary = "Shorten a URL",
        description = "Generates a short URL for a given long URL."
    )
    @PostMapping("/api/v1/shorten")
    fun storten(@RequestBody body: LongUrlDto): ShortUrlDto {
        if (!body.longUrl.isValidURL()) {
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Given property \"longUrl\"=${body.longUrl} isn't URL!"
            )
        }

        val newShortenedURL = shortenedURLService.shortenURL(body.longUrl)
        return ShortUrlDto(
            shortUrl = "$basicShortUrl/${newShortenedURL.shortCode}",
            shortCode = newShortenedURL.shortCode,
            expiredAt = newShortenedURL.expiredAt.toKotlinInstant()
        )
    }

    @GetMapping("/{shortCode}")
    fun redirectToOriginalURL(@PathVariable("shortCode") shortCode: String, model: ModelMap): ModelAndView {
        val longUrl = shortenedURLService.getLongUrl(shortCode)?.url
            ?: throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "The full URL with such short code=$shortCode isn't found!"
            )

        thread(start = true) {
            shortenedURLService.updateAnalytics(shortCode)
        }

        model.addAttribute("attribute", "redirectWithRedirectPrefix")
        return ModelAndView("redirect:$longUrl", model);
    }

    @Operation(
        summary = "Get original URL",
        description = "Returns the original URL without redirect."
    )
    @GetMapping("/api/v1/url/{shortCode}")
    fun getOriginalURL(@PathVariable("shortCode") shortCode: String): LongUrlInfoDto {
        val dbEntity = shortenedURLService.getLongUrl(shortCode)

        if (dbEntity == null) {
            throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "The full URL with such short code=$shortCode isn't found!"
            )
        } else {
            return LongUrlInfoDto(
                dbEntity.url,
                dbEntity.createdAt.toKotlinInstant(),
                dbEntity.expiredAt.toKotlinInstant()
            )
        }
    }

    @Operation(
        summary = "Get analytics for a shortened URL",
        description = "Returns click analytics."
    )
    @GetMapping("/api/v1/analytics/{shortCode}")
    fun getAnalytics(@PathVariable("shortCode") shortCode: String): ShortUrlAnalyticsDto {
        try {
            val clickCounts = shortenedURLService.getClickCount(shortCode)
            return ShortUrlAnalyticsDto(shortCode, clickCounts)
        } catch (e: Exception) {
            throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                e.message
            )
        }
    }

}
