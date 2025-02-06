package wyrd.sister.URLshortener.controllers.v1

import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import wyrd.sister.URLshortener.entities.ShortenedUrls
import wyrd.sister.URLshortener.services.ShortenedUrlService
import java.time.Instant

@SpringBootTest
@AutoConfigureMockMvc
class ShortenedUrlControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var shortenedUrlService: ShortenedUrlService

    @Test
    fun testGenerateShorterUrlSuccess() {
        val longUrl = "https://example.com/long_url/long_url"
        val shortCode = "test"
        val json = "{\"longUrl\": \"$longUrl\", \"customAlias\" : \"$shortCode\"}"
        `when`(shortenedUrlService.shortenURL(longUrl, shortCode)).thenReturn(
            ShortenedUrls(
                shortCode,
                longUrl,
                Instant.now(),
                Instant.now()
            )
        )

        val result = mockMvc.perform(
            post("/api/v1/shorten")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        )

        result.andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.shortUrl").exists())
            .andExpect(jsonPath("$.shortCode").value(shortCode))
            .andExpect(jsonPath("$.expiredAt").exists())
    }

    @Test
    fun testGenerateShorterUrlError400() {
        val json = "{\"longUrl\": \"example.com\"}"

        val result = mockMvc.perform(
            post("/api/v1/shorten")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        )

        result.andExpect(status().isBadRequest())
    }

    @Test
    fun testRedirectToOriginalUrlSuccess() {
        val longUrl = "https://example.com/long_url"
        val shortCode = "test"
        `when`(shortenedUrlService.getLongUrl(shortCode)).thenReturn(
            ShortenedUrls(
                shortCode,
                longUrl,
                Instant.now(),
                Instant.now()
            )
        )

        val result = mockMvc.perform(get("/$shortCode"))

        result.andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("$longUrl?attribute=redirectWithRedirectPrefix"))
    }

    @Test
    fun testRedirectToOriginalUrlError404() {
        val shortCode = "test404"
        `when`(shortenedUrlService.getLongUrl(shortCode)).thenReturn(null)

        val result = mockMvc.perform(
            get("/$shortCode")
        )

        result.andExpect(status().isNotFound())
    }

    @Test
    fun testGetOriginalUrlSuccess() {
        val longUrl = "https://example.com/long_url123"
        val shortCode = "test123"
        `when`(shortenedUrlService.getLongUrl(shortCode)).thenReturn(
            ShortenedUrls(
                shortCode,
                longUrl,
                Instant.now(),
                Instant.now()
            )
        )


        val result = mockMvc.perform(
            get("/api/v1/url/$shortCode")
        )

        result.andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("\$.longUrl").value(longUrl))
    }

    @Test
    fun testGetOriginalUrlError404() {
        val shortCode = "test404"
        `when`(shortenedUrlService.getLongUrl(shortCode)).thenReturn(null)

        val result = mockMvc.perform(
            get("/api/v1/url/$shortCode")
        )

        result.andExpect(status().isNotFound())
    }

    @Test
    fun testGetAnalyticsSuccess() {
        val shortCode = "test_analytics456"
        val clicksCount = 10
        `when`(shortenedUrlService.getClickCount(shortCode)).thenReturn(clicksCount)

        val result = mockMvc.perform(
            get("/api/v1/analytics/$shortCode")
        )

        result.andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("\$.shortCode").value(shortCode))
            .andExpect(jsonPath("\$.clicksCount").value(clicksCount))
    }
}