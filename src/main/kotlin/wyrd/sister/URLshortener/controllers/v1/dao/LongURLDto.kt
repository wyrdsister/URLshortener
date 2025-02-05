package wyrd.sister.URLshortener.controllers.v1.dao

import kotlinx.serialization.Serializable

@Serializable
data class LongURLDto(val longUrl: String)
