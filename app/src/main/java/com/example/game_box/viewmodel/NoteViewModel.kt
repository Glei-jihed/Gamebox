package com.example.game_box.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.game_box.data.model.Note
import com.example.game_box.data.repository.NoteRepository

class NoteViewModel(private val repository: NoteRepository) : ViewModel() {

    private val _notes = MutableLiveData<List<Note>>()
    val notes: LiveData<List<Note>> get() = _notes

    val favorites: LiveData<List<Note>> get() = repository.favorites

    init {
        repository.notes.observeForever { newList ->
            _notes.value = newList
        }
    }

    fun fetchAllNotes() {
        repository.fetchAllNotes()
    }

    fun fetchNotes(userId: String) {
        repository.fetchNotes(userId)
    }

    fun fetchFavorites(userId: String) {
        repository.fetchFavorites(userId)
    }

    fun createNote(note: Note, callback: (Boolean) -> Unit) {
        repository.createNote(note) { success ->
            if (success) {
                fetchNotes(note.userId)
            }
            callback(success)
        }
    }

    fun updateNote(noteId: Long, note: Note, callback: (Boolean) -> Unit) {
        repository.updateNote(noteId, note) { success ->
            if (success) {
                fetchNotes(note.userId)
            }
            callback(success)
        }
    }

    fun deleteNote(noteId: Long, callback: (Boolean) -> Unit) {
        repository.deleteNote(noteId, callback)
    }

    fun addFavorite(userId: String, noteId: Long, callback: (Boolean) -> Unit) {
        repository.addFavorite(userId, noteId) { success ->
            if (success) fetchFavorites(userId)
            callback(success)
        }
    }

    fun removeFavorite(userId: String, noteId: Long, callback: (Boolean) -> Unit) {
        repository.removeFavorite(userId, noteId) { success ->
            if (success) fetchFavorites(userId)
            callback(success)
        }
    }

    fun searchNotes(userId: String, tags: String) {
        repository.searchNotes(userId, tags) { notesList ->
            _notes.postValue(notesList)
        }
    }
}
