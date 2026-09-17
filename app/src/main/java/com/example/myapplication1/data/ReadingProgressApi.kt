package com.example.myapplication1.data

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface ReadingProgressApi {

    @GET("api/reading-progress/member/{memberId}")
    suspend fun getMemberProgress(
        @Path("memberId") memberId: Long
    ): List<ReadingProgress>

    @GET("api/reading-progress/{memberId}/{bookId}")
    suspend fun getProgress(
        @Path("memberId") memberId: Long,
        @Path("bookId") bookId: Long
    ): ReadingProgress

    @PUT("api/reading-progress/{memberId}/{bookId}")
    suspend fun updateProgress(
        @Path("memberId") memberId: Long,
        @Path("bookId") bookId: Long,
        @Body request: ProgressUpdateRequest
    ): ReadingProgress

    @PUT("api/reading-progress/{memberId}/{bookId}/read")
    suspend fun markAsRead(
        @Path("memberId") memberId: Long,
        @Path("bookId") bookId: Long
    ): ReadingProgress
}