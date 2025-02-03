package wyrd.sister.URLshortener.repositories

import org.springframework.data.jpa.repository.JpaRepository
import wyrd.sister.URLshortener.entities.ShortenedURL

interface ShortenedURLRepository : JpaRepository<ShortenedURL, Int> {
}