package com.example.game_box.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
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

class NotesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NoteAdapter
    private lateinit var noteViewModel: NoteViewModel

    private lateinit var userId: String
    private lateinit var searchView: EditText
    private lateinit var btnSearch: Button
    private lateinit var btnAddNote: Button
    private lateinit var progressBar: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes)

        userId = intent.getStringExtra("USER_ID") ?: ""
        if (userId.isBlank()) {
            Toast.makeText(this, "Erreur : UID manquant", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        recyclerView = findViewById(R.id.recyclerView)
        searchView = findViewById(R.id.searchView)
        btnSearch = findViewById(R.id.btnSearch)
        btnAddNote = findViewById(R.id.btnAddNote)
        progressBar = findViewById(R.id.progressBar)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = NoteAdapter(
            notes = emptyList(),
            currentUserId = userId,
            onNoteClick = { note -> showNoteDetail(note) },
            onFavoriteClick = { note, isFavorite -> toggleFavorite(note, isFavorite) },
            onDeleteClick = { note -> deleteNoteFromAllUsers(note) }
        )
        recyclerView.adapter = adapter

        val repository = NoteRepository()
        val factory = NoteViewModelFactory(repository)
        noteViewModel = ViewModelProvider(this, factory).get(NoteViewModel::class.java)

        noteViewModel.notes.observe(this) { notes ->
            progressBar.visibility = View.GONE
            adapter.updateNotes(notes)
        }

        btnSearch.setOnClickListener {
            val tags = searchView.text.toString().trim()
            progressBar.visibility = View.VISIBLE
            if (tags.isNotEmpty()) {
                noteViewModel.searchNotes(userId, tags)
            } else {
                noteViewModel.fetchNotes(userId)
            }
        }

        btnAddNote.setOnClickListener {
            val intent = Intent(this, NoteFormActivity::class.java)
            intent.putExtra("USER_ID", userId)
            startActivity(intent)
        }

        progressBar.visibility = View.VISIBLE
        noteViewModel.fetchNotes(userId)
    }

    override fun onResume() {
        super.onResume()
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
            noteViewModel.addFavorite(userId, note.id) {}
        } else {
            noteViewModel.removeFavorite(userId, note.id) {}
        }
    }

    private fun deleteNoteFromAllUsers(note: Note) {
        progressBar.visibility = View.VISIBLE
        noteViewModel.deleteNote(note.id) { success ->
            progressBar.visibility = View.GONE
            if (success) {
                Toast.makeText(this, "Note supprimée (globalement)", Toast.LENGTH_SHORT).show()
                noteViewModel.fetchNotes(userId)
            } else {
                Toast.makeText(this, "Erreur lors de la suppression", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
