package wyrd.sister.URLshortener.controllers.v1

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toKotlinInstant
import kotlinx.datetime.toLocalDateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import org.springframework.web.servlet.view.RedirectView
import wyrd.sister.URLshortener.controllers.v1.dao.LongURLDto
import wyrd.sister.URLshortener.controllers.v1.dao.LongUrlInfoDto
import wyrd.sister.URLshortener.controllers.v1.dao.ShortUrlDto
import wyrd.sister.URLshortener.controllers.v1.dao.StatsInfoDto
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

    @GetMapping("/url/{shortCode}")
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

    @GetMapping("/stats/{shortCode}")
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
