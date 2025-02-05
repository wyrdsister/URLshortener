package wyrd.sister.URLshortener.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import wyrd.sister.URLshortener.entities.ShortenedUrlAnalytics

@Repository
interface ShortenedUrlAnalyticsRepository : JpaRepository<ShortenedUrlAnalytics, String>