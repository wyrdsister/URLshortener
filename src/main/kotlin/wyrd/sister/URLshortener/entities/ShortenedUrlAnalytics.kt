package wyrd.sister.URLshortener.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.ColumnDefault
import java.time.Instant

@Entity
@Table(name = "shortened_url_analytics", schema = "public")
data class ShortenedUrlAnalytics(
    @Id @Column(name = "id", nullable = false) val shortCode: String,
    @ColumnDefault("0") @Column(name = "click_count", nullable = false) val clickCount: Int
)