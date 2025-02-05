package wyrd.sister.URLshortener.controllers.v1.dao

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class ShortUrlDto (val shortUrl: String, val expiredAt: LocalDateTime)