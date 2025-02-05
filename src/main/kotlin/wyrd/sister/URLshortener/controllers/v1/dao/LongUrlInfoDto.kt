package wyrd.sister.URLshortener.controllers.v1.dao

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class LongUrlInfoDto(
    val longUrl: String,
    val createdAt: LocalDateTime,
    val expiredAt: LocalDateTime,
    val clicksCount: Int
)