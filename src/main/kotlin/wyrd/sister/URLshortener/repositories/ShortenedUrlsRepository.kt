package wyrd.sister.URLshortener.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import wyrd.sister.URLshortener.entities.ShortenedUrls

@Repository
interface ShortenedUrlsRepository : JpaRepository<ShortenedUrls, String> {
}