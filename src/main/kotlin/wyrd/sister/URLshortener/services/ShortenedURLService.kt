package wyrd.sister.URLshortener.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import wyrd.sister.URLshortener.entities.ShortenedUrls
import wyrd.sister.URLshortener.repositories.ShortenedUrlsRepository
import wyrd.sister.URLshortener.utils.generateString
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import kotlin.jvm.optionals.getOrNull

@Service
class ShortenedURLService(
    @Autowired val shortenedURLRepository: ShortenedUrlsRepository
) {

    private val length = 5


    fun shortenURL(url: String): ShortenedUrls {
        val shortCode = getUniqueShortCode()
        val expiredAt = LocalDateTime.now().plusDays(1)

        val shortenURL = ShortenedUrls(
            shortCode = shortCode,
            url = url,
            createdAt = Instant.now(),
            expiredAt = expiredAt.toInstant(ZoneOffset.UTC),
            clickCount = 0
        )

        return shortenedURLRepository.save(shortenURL)
    }

    private fun getUniqueShortCode(): String{
        var shortCode = generateString(length)
        while (shortenedURLRepository.findById(shortCode).getOrNull() != null){
            shortCode = generateString(length+1)
        }

        return shortCode
    }

}