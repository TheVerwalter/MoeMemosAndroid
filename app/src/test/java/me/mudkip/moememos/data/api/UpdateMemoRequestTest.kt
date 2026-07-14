package me.mudkip.moememos.data.api

import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.http.Query
import java.time.Instant

class UpdateMemoRequestTest {
    @Test
    fun updateMemoSendsMaskAsUpdateMaskQueryParameter() {
        val updateMaskParameter = MemosV1Api::class.java
            .getDeclaredMethod(
                "updateMemo",
                String::class.java,
                String::class.java,
                UpdateMemoRequest::class.java,
                kotlin.coroutines.Continuation::class.java,
            )
            .parameterAnnotations[1]
            .filterIsInstance<Query>()
            .single()

        assertEquals("updateMask", updateMaskParameter.value)
    }

    @Test
    fun updateMaskContainsOnlyFieldsPresentInRequest() {
        val request = UpdateMemoRequest(
            content = "edited",
            visibility = MemosVisibility.PUBLIC,
            state = MemosV1State.ARCHIVED,
            pinned = false,
            updateTime = Instant.EPOCH,
            attachments = emptyList(),
        )

        assertEquals(
            "content,visibility,state,pinned,update_time,attachments",
            request.updateMask(),
        )
    }

    @Test
    fun updateMaskSupportsEachPartialUpdate() {
        assertEquals("content", UpdateMemoRequest(content = "edited").updateMask())
        assertEquals("visibility", UpdateMemoRequest(visibility = MemosVisibility.PRIVATE).updateMask())
        assertEquals("state", UpdateMemoRequest(state = MemosV1State.NORMAL).updateMask())
        assertEquals("pinned", UpdateMemoRequest(pinned = false).updateMask())
        assertEquals("update_time", UpdateMemoRequest(updateTime = Instant.EPOCH).updateMask())
        assertEquals("attachments", UpdateMemoRequest(attachments = emptyList()).updateMask())
    }
}
