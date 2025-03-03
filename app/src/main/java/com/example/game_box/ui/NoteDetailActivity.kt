package com.example.game_box.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.game_box.R
import com.example.game_box.data.repository.NoteRepository
import com.example.game_box.viewmodel.NoteViewModel
import com.example.game_box.viewmodel.NoteViewModelFactory

class NoteDetailActivity : AppCompatActivity() {

    private lateinit var noteViewModel: NoteViewModel
    private lateinit var userId: String

    private var noteId: Long = 0
    private var noteUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_detail)

        val noteTitle = findViewById<TextView>(R.id.noteTitle)
        val noteDescription = findViewById<TextView>(R.id.noteDescription)
        val noteTags = findViewById<TextView>(R.id.noteTags)
        val btnJoin = findViewById<Button>(R.id.btnJoin)
        val btnAddFavorite = findViewById<Button>(R.id.btnAddFavorite)

        userId = intent.getStringExtra("USER_ID") ?: ""
        noteId = intent.getLongExtra("NOTE_ID", 0)

        val title = intent.getStringExtra("NOTE_TITLE") ?: "Sans titre"
        val description = intent.getStringExtra("NOTE_DESCRIPTION") ?: "Aucune description"
        val tags = intent.getStringArrayListExtra("NOTE_TAGS") ?: arrayListOf()
        noteUrl = intent.getStringExtra("NOTE_URL")

        noteTitle.text = title
        noteDescription.text = description
        noteTags.text = if (tags.isNotEmpty()) tags.joinToString(", ") else "Pas de tags"

        val repository = NoteRepository()
        val factory = NoteViewModelFactory(repository)
        noteViewModel = ViewModelProvider(this, factory).get(NoteViewModel::class.java)

        btnJoin.setOnClickListener {
            if (!noteUrl.isNullOrBlank()) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(noteUrl))
                startActivity(intent)
            } else {
                Toast.makeText(this, "Aucun lien disponible", Toast.LENGTH_SHORT).show()
            }
        }

        btnAddFavorite.setOnClickListener {
            noteViewModel.addFavorite(userId, noteId) { success ->
                if (success) {
                    Toast.makeText(this, "Note ajoutée aux favoris", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Erreur lors de l'ajout aux favoris", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
