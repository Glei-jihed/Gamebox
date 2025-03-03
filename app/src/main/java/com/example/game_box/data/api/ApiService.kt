package com.example.game_box.data.api

import com.example.game_box.data.model.FavoriteNote
import com.example.game_box.data.model.Note
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET("notes/all")
    fun getAllNotes(): Call<List<Note>>

    @GET("notes/{userId}")
    fun getUserNotes(@Path("userId") userId: String): Call<List<Note>>

    @GET("favorites/{userId}")
    fun getFavorites(@Path("userId") userId: String): Call<List<FavoriteNote>>

    @POST("notes")
    fun createNote(@Body note: Note): Call<Note>

    @PUT("notes/{id}")
    fun updateNote(@Path("id") id: Long, @Body note: Note): Call<Note>

    @DELETE("notes/{id}")
    fun deleteNote(@Path("id") id: Long): Call<Unit>

    @POST("favorites/{userId}/{noteId}")
    fun addFavorite(@Path("userId") userId: String, @Path("noteId") noteId: Long): Call<FavoriteNote>

    @DELETE("favorites/{userId}/{noteId}")
    fun removeFavorite(@Path("userId") userId: String, @Path("noteId") noteId: Long): Call<Unit>

    @GET("notes/search")
    fun searchNotes(
        @Query("userId") userId: String,
        @Query("tags") tags: String
    ): Call<List<Note>>
}
