package wyrd.sister.URLshortener.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.ColumnDefault
import java.time.Instant

@Entity
@Table(name = "shortened_urls", schema = "public")
data class ShortenedUrls(
    @Id @Column(name = "id", nullable = false) val shortCode: String,
    @Column(name = "url", nullable = false) val url: String,
    @ColumnDefault("now()") @Column(name = "created_at", nullable = false) val createdAt: Instant,
    @Column(name = "expired_at", nullable = false) val expiredAt: Instant,
    @ColumnDefault("0") @Column(name = "click_count", nullable = false) val clickCount: Int
)