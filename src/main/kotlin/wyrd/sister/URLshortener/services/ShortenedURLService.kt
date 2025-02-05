package wyrd.sister.URLshortener.services

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import wyrd.sister.URLshortener.entities.ShortenedUrlAnalytics
import wyrd.sister.URLshortener.entities.ShortenedUrls
import wyrd.sister.URLshortener.repositories.ShortenedUrlAnalyticsRepository
import wyrd.sister.URLshortener.repositories.ShortenedUrlsRepository
import wyrd.sister.URLshortener.utils.generateString
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import kotlin.jvm.optionals.getOrElse
import kotlin.jvm.optionals.getOrNull

@Service
class ShortenedURLService(
    @Autowired val shortenedURLRepository: ShortenedUrlsRepository,
    @Autowired val shortenedUrlAnalyticsRepository: ShortenedUrlAnalyticsRepository
) {

    private val logger = LoggerFactory.getLogger(this.javaClass.name)
    private val length = 5


    fun shortenURL(url: String): ShortenedUrls {
        val shortCode = getUniqueShortCode()
        val expiredAt = LocalDateTime.now().plusDays(1)

        val shortenURL = ShortenedUrls(
            shortCode = shortCode,
            url = url,
            createdAt = Instant.now(),
            expiredAt = expiredAt.toInstant(ZoneOffset.UTC)
        )
        val newShortenedUrl = shortenedURLRepository.save(shortenURL)

        saveAnalytics(shortCode, 0)

        return newShortenedUrl
    }

    fun updateAnalytics(shortCode: String){
        logger.debug("Start operation=\"updateAnalytics\" with parameters: shortCode=$shortCode")
        val analytics = shortenedUrlAnalyticsRepository.findById(shortCode).getOrNull()
        if (analytics == null){
            saveAnalytics(shortCode, 1)
        } else {
            saveAnalytics(shortCode, analytics.clickCount + 1)
        }
        logger.debug("Completed operation=\"updateAnalytics\" with parameters: shortCode=$shortCode")
    }

    fun getLongUrl(shortCode: String): ShortenedUrls? {
        return shortenedURLRepository.findByIdOrNull(shortCode)
    }

    fun getClickCount(shortCode: String): Int {
        return shortenedUrlAnalyticsRepository.findById(shortCode).getOrNull()?.clickCount
            ?: throw NullPointerException("The analytics for short code=$shortCode isn't found!")
    }

    private fun getUniqueShortCode(): String {
        var shortCode = generateString(length)
        while (shortenedURLRepository.findById(shortCode).getOrNull() != null) {
            shortCode = generateString(length + 1)
        }

        return shortCode
    }

    private fun saveAnalytics(shortCode: String, clickCount: Int){
        val shortenedUrlAnalytics = ShortenedUrlAnalytics(
            shortCode = shortCode,
            clickCount = clickCount
        )

        shortenedUrlAnalyticsRepository.save(shortenedUrlAnalytics)
    }

}