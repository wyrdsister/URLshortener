package wyrd.sister.URLshortener.controllers.v1.dto

import kotlinx.serialization.Serializable

@Serializable
data class LongUrlDto(val longUrl: String, val customAlias: String? = "")
