package wyrd.sister.URLshortener.controllers.v1.dto

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class LongUrlInfoDto(
    val longUrl: String,
    val createdAt: Instant,
    val expiredAt: Instant
)