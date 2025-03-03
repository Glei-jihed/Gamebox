package com.example.game_box.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.game_box.data.api.ApiService
import com.example.game_box.data.model.FavoriteNote
import com.example.game_box.data.model.Note
import com.example.game_box.network.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NoteRepository {
    private val apiService: ApiService = RetrofitInstance.api

    private val _notes = MutableLiveData<List<Note>>()
    val notes: LiveData<List<Note>> get() = _notes

    private val _favorites = MutableLiveData<List<Note>>()
    val favorites: LiveData<List<Note>> get() = _favorites

    fun fetchAllNotes() {
        apiService.getAllNotes().enqueue(object : Callback<List<Note>> {
            override fun onResponse(call: Call<List<Note>>, response: Response<List<Note>>) {
                if (response.isSuccessful) {
                    _notes.postValue(response.body() ?: emptyList())
                } else {
                    Log.e("NoteRepository", "Erreur fetchAllNotes: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<List<Note>>, t: Throwable) {
                Log.e("NoteRepository", "Erreur réseau fetchAllNotes: ${t.message}")
            }
        })
    }

    fun fetchNotes(userId: String) {
        apiService.getUserNotes(userId).enqueue(object : Callback<List<Note>> {
            override fun onResponse(call: Call<List<Note>>, response: Response<List<Note>>) {
                if (response.isSuccessful) {
                    _notes.postValue(response.body() ?: emptyList())
                } else {
                    Log.e("NoteRepository", "Erreur fetchNotes: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<List<Note>>, t: Throwable) {
                Log.e("NoteRepository", "Erreur réseau fetchNotes: ${t.message}")
            }
        })
    }

    fun fetchFavorites(userId: String) {
        apiService.getFavorites(userId).enqueue(object : Callback<List<FavoriteNote>> {
            override fun onResponse(call: Call<List<FavoriteNote>>, response: Response<List<FavoriteNote>>) {
                if (response.isSuccessful) {
                    val favs = response.body()?.map { it.note } ?: emptyList()
                    _favorites.postValue(favs)
                } else {
                    Log.e("NoteRepository", "Erreur fetchFavorites: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<List<FavoriteNote>>, t: Throwable) {
                Log.e("NoteRepository", "Erreur réseau fetchFavorites: ${t.message}")
            }
        })
    }

    fun createNote(note: Note, callback: (Boolean) -> Unit) {
        apiService.createNote(note).enqueue(object : Callback<Note> {
            override fun onResponse(call: Call<Note>, response: Response<Note>) {
                callback(response.isSuccessful)
            }
            override fun onFailure(call: Call<Note>, t: Throwable) {
                callback(false)
            }
        })
    }

    fun updateNote(noteId: Long, note: Note, callback: (Boolean) -> Unit) {
        apiService.updateNote(noteId, note).enqueue(object : Callback<Note> {
            override fun onResponse(call: Call<Note>, response: Response<Note>) {
                callback(response.isSuccessful)
            }
            override fun onFailure(call: Call<Note>, t: Throwable) {
                callback(false)
            }
        })
    }

    fun deleteNote(noteId: Long, callback: (Boolean) -> Unit) {
        apiService.deleteNote(noteId).enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                callback(response.isSuccessful)
            }
            override fun onFailure(call: Call<Unit>, t: Throwable) {
                callback(false)
            }
        })
    }

    fun addFavorite(userId: String, noteId: Long, callback: (Boolean) -> Unit) {
        apiService.addFavorite(userId, noteId).enqueue(object : Callback<FavoriteNote> {
            override fun onResponse(call: Call<FavoriteNote>, response: Response<FavoriteNote>) {
                if (response.isSuccessful) {
                    Log.d("NoteRepository", "addFavorite OK pour note $noteId")
                    callback(true)
                } else {
                    // Affiche le contenu de l'erreur pour débogage
                    val errorMsg = response.errorBody()?.string() ?: response.message()
                    Log.e("NoteRepository", "addFavorite failed: $errorMsg")
                    callback(false)
                }
            }
            override fun onFailure(call: Call<FavoriteNote>, t: Throwable) {
                Log.e("NoteRepository", "addFavorite onFailure: ${t.message}")
                callback(false)
            }
        })
    }

    fun removeFavorite(userId: String, noteId: Long, callback: (Boolean) -> Unit) {
        apiService.removeFavorite(userId, noteId).enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (!response.isSuccessful) {
                    Log.e("NoteRepository", "removeFavorite failed: ${response.message()}")
                }
                callback(response.isSuccessful)
            }
            override fun onFailure(call: Call<Unit>, t: Throwable) {
                Log.e("NoteRepository", "removeFavorite onFailure: ${t.message}")
                callback(false)
            }
        })
    }

    fun searchNotes(userId: String, tags: String, callback: (List<Note>) -> Unit) {
        apiService.searchNotes(userId, tags).enqueue(object : Callback<List<Note>> {
            override fun onResponse(call: Call<List<Note>>, response: Response<List<Note>>) {
                if (response.isSuccessful) {
                    callback(response.body() ?: emptyList())
                } else {
                    callback(emptyList())
                }
            }
            override fun onFailure(call: Call<List<Note>>, t: Throwable) {
                callback(emptyList())
            }
        })
    }
}
