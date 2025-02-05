package wyrd.sister.URLshortener.controllers.v1.dto

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class ShortUrlDto(
    val shortUrl: String,
    val shortCode: String,
    val expiredAt: Instant
)