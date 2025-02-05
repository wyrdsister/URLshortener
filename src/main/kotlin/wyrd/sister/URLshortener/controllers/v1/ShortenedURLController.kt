package wyrd.sister.URLshortener.controllers.v1

import io.swagger.v3.oas.annotations.Operation
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toKotlinInstant
import kotlinx.datetime.toLocalDateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.servlet.ModelAndView
import wyrd.sister.URLshortener.controllers.v1.dto.LongURLDto
import wyrd.sister.URLshortener.controllers.v1.dto.LongUrlInfoDto
import wyrd.sister.URLshortener.controllers.v1.dto.ShortUrlDto
import wyrd.sister.URLshortener.controllers.v1.dto.StatsInfoDto
import wyrd.sister.URLshortener.services.ShortenedURLService
import wyrd.sister.URLshortener.utils.isValidURL

@RestController
@RequestMapping
class ShortenedURLController(
    @Autowired val shortenedURLService: ShortenedURLService
) {
    private val basicShortUrl = "https://ka.pro/"


    @Operation(summary = "Shorten a URL",
        description = "Generates a short URL for a given long URL.")
    @PostMapping("/api/v1/shorten")
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
            shortCode = newShortenedURL.shortCode,
            expiredAt = newShortenedURL.expiredAt.toKotlinInstant().toLocalDateTime(TimeZone.UTC)
        )
    }

    @GetMapping("/{shortCode}")
    fun redirectToOriginalURL(@PathVariable("shortCode") shortCode: String, model: ModelMap) : ModelAndView {
        val longUrl = shortenedURLService.getLongUrl(shortCode)?.url
        if (longUrl == null) {
            throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "The full URL with such short code=$shortCode isn't found!"
            )
        }

        model.addAttribute("attribute", "redirectWithRedirectPrefix")
        return ModelAndView("redirect:$longUrl", model);
    }

    @Operation(summary = "Get original URL",
        description = "Returns the original URL without redirect.")
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
                dbEntity.createdAt.toKotlinInstant().toLocalDateTime(TimeZone.UTC),
                dbEntity.expiredAt.toKotlinInstant().toLocalDateTime(TimeZone.UTC),
                dbEntity.clickCount
            )
        }
    }

    @Operation(summary = "Get analytics for a shortened URL",
        description = "Returns click analytics.")
    @GetMapping("/api/v1/analytics/{shortCode}")
    fun getStats(@PathVariable("shortCode") shortCode: String): StatsInfoDto {
        try {
            val clickCounts = shortenedURLService.getStats(shortCode)
            return StatsInfoDto(shortCode, clickCounts)
        } catch (e: Exception) {
            throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                e.message
            )
        }

    }

}
