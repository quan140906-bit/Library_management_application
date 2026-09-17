package com.example.myapplication1.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ReadingProgressRepository {

    private val api: ReadingProgressApi

    init {

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8082/")
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()

        api = retrofit.create(
            ReadingProgressApi::class.java
        )
    }


    suspend fun getMemberProgress(
        memberId: Long
    ): List<ReadingProgress> {

        return api.getMemberProgress(
            memberId
        )
    }


    suspend fun getProgress(
        memberId: Long,
        bookId: Long
    ): ReadingProgress {

        return api.getProgress(
            memberId,
            bookId
        )
    }


    suspend fun updateProgress(
        memberId: Long,
        bookId: Long,
        progress: Int
    ): ReadingProgress {

        return api.updateProgress(
            memberId,
            bookId,
            ProgressUpdateRequest(
                progress = progress
            )
        )
    }


    suspend fun markAsRead(
        memberId: Long,
        bookId: Long
    ): ReadingProgress {

        return api.markAsRead(
            memberId,
            bookId
        )
    }
}


