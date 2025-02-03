package wyrd.sister.URLshortener.entities

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "shortened_urls")
data class ShortenedURL(
    @Id val id: Int,
    val shortCode: String,
    val url: String,
    val createdAt: LocalDateTime,
    val expiredAt: LocalDateTime,
    val clickCount: Int
)