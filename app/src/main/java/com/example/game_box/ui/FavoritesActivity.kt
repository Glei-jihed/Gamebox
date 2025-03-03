package com.example.game_box.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.game_box.R
import com.example.game_box.adapter.NoteAdapter
import com.example.game_box.data.model.Note
import com.example.game_box.data.repository.NoteRepository
import com.example.game_box.viewmodel.NoteViewModel
import com.example.game_box.viewmodel.NoteViewModelFactory

class FavoritesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NoteAdapter
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var userId: String
    private lateinit var progressBar: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        userId = intent.getStringExtra("USER_ID") ?: ""
        if (userId.isBlank()) {
            Toast.makeText(this, "Erreur : UID manquant", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        recyclerView = findViewById(R.id.recyclerView)
        progressBar = findViewById(R.id.progressBar)

        adapter = NoteAdapter(
            notes = emptyList(),
            currentUserId = userId,
            onNoteClick = { note -> showNoteDetail(note) },
            onFavoriteClick = { note, newFavState ->
                if (newFavState) {
                    noteViewModel.addFavorite(userId, note.id) { success ->
                        if (!success) {
                            Toast.makeText(this, "Erreur ajout favoris", Toast.LENGTH_SHORT).show()
                        } else {
                            noteViewModel.fetchFavorites(userId)
                        }
                    }
                } else {
                    noteViewModel.removeFavorite(userId, note.id) { success ->
                        if (!success) {
                            Toast.makeText(this, "Erreur retrait favoris", Toast.LENGTH_SHORT).show()
                        } else {
                            noteViewModel.fetchFavorites(userId)
                        }
                    }
                }
            },
            onDeleteClick = { } // Pas de suppression ici
        )
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        val repository = NoteRepository()
        val factory = NoteViewModelFactory(repository)
        noteViewModel = ViewModelProvider(this, factory).get(NoteViewModel::class.java)

        noteViewModel.favorites.observe(this) { favoriteNotes ->
            progressBar.visibility = View.GONE
            adapter.updateNotes(favoriteNotes)
        }

        progressBar.visibility = View.VISIBLE
        noteViewModel.fetchFavorites(userId)
    }

    override fun onResume() {
        super.onResume()
        progressBar.visibility = View.VISIBLE
        noteViewModel.fetchFavorites(userId)
    }

    private fun showNoteDetail(note: Note) {
        val intent = Intent(this, NoteDetailActivity::class.java).apply {
            putExtra("USER_ID", userId)
            putExtra("NOTE_ID", note.id)
            putExtra("NOTE_TITLE", note.title)
            putExtra("NOTE_DESCRIPTION", note.description)
            putExtra("NOTE_TAGS", ArrayList(note.tags))
            putExtra("NOTE_URL", note.sourceUrl)
        }
        startActivity(intent)
    }
}
