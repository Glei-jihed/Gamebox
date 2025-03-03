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

class MyNotesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NoteAdapter
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var userId: String
    private lateinit var progressBar: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_notes)

        userId = intent.getStringExtra("USER_ID") ?: ""
        if (userId.isBlank()) {
            Toast.makeText(this, "Erreur : UID manquant", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        recyclerView = findViewById(R.id.recyclerView)
        progressBar = findViewById(R.id.progressBar)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = NoteAdapter(
            notes = emptyList(),
            currentUserId = userId,
            onNoteClick = { note -> showNoteDetail(note) },
            onFavoriteClick = { note, isFavorite -> toggleFavorite(note, isFavorite) },
            onDeleteClick = { note -> deleteMyNote(note) }
        )
        recyclerView.adapter = adapter

        val repository = NoteRepository()
        val factory = NoteViewModelFactory(repository)
        noteViewModel = ViewModelProvider(this, factory).get(NoteViewModel::class.java)

        noteViewModel.notes.observe(this) { notes ->
            progressBar.visibility = View.GONE
            adapter.updateNotes(notes)
        }

        progressBar.visibility = View.VISIBLE
        noteViewModel.fetchNotes(userId)
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

    private fun toggleFavorite(note: Note, isFavorite: Boolean) {
        if (isFavorite) {
            noteViewModel.addFavorite(userId, note.id) {
                noteViewModel.fetchNotes(userId)
            }
        } else {
            noteViewModel.removeFavorite(userId, note.id) {
                noteViewModel.fetchNotes(userId)
            }
        }
    }

    private fun deleteMyNote(note: Note) {
        progressBar.visibility = View.VISIBLE
        noteViewModel.deleteNote(note.id) { success ->
            progressBar.visibility = View.GONE
            if (success) {
                Toast.makeText(this, "Note supprimée", Toast.LENGTH_SHORT).show()
                noteViewModel.fetchNotes(userId)
            } else {
                Toast.makeText(this, "Erreur lors de la suppression", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
