package com.example.game_box.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
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

class DevNotesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NoteAdapter
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var userId: String

    private lateinit var btnAddNote: Button
    private lateinit var btnFavorites: Button
    private lateinit var btnMyNotes: Button
    private lateinit var searchView: EditText
    private lateinit var btnSearch: Button
    private lateinit var progressBar: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // On utilise le layout modifié activity_dev_notes.xml
        setContentView(R.layout.activity_dev_notes)

        // Récupération de l'ID utilisateur transmis via l'intent
        userId = intent.getStringExtra("USER_ID") ?: ""
        if (userId.isBlank()) {
            Toast.makeText(this, "Erreur : UID manquant", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        // Liaison des vues
        progressBar = findViewById(R.id.progressBar)
        searchView = findViewById(R.id.searchView)
        btnSearch = findViewById(R.id.btnSearch)
        btnAddNote = findViewById(R.id.btnAddNote)
        btnFavorites = findViewById(R.id.btnFavorites)
        btnMyNotes = findViewById(R.id.btnMyNotes)
        recyclerView = findViewById(R.id.recyclerView)

        // Configuration de la RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = NoteAdapter(
            notes = emptyList(),
            currentUserId = userId,
            onNoteClick = { note -> showNoteDetail(note) },
            onFavoriteClick = { note, isFavorite -> toggleFavorite(note, isFavorite) },
            onDeleteClick = { } // Dans la vue globale, la suppression n'est pas proposée
        )
        recyclerView.adapter = adapter

        // Initialisation du ViewModel
        val repository = NoteRepository()
        val factory = NoteViewModelFactory(repository)
        noteViewModel = ViewModelProvider(this, factory).get(NoteViewModel::class.java)

        // Observation des mises à jour des notes
        noteViewModel.notes.observe(this) { notes ->
            progressBar.visibility = View.GONE
            adapter.updateNotes(notes)
        }

        // Bouton "Ajouter une note" : ouvre le formulaire de création
        btnAddNote.setOnClickListener {
            val intent = Intent(this, NoteFormActivity::class.java)
            intent.putExtra("USER_ID", userId)
            startActivity(intent)
        }

        // Bouton "Mes Favoris" : ouvre l'activité des favoris
        btnFavorites.setOnClickListener {
            val intent = Intent(this, FavoritesActivity::class.java)
            intent.putExtra("USER_ID", userId)
            startActivity(intent)
        }

        // Bouton "Mes Notes" : ouvre l'activité affichant uniquement vos notes
        btnMyNotes.setOnClickListener {
            val intent = Intent(this, MyNotesActivity::class.java)
            intent.putExtra("USER_ID", userId)
            startActivity(intent)
        }

        // Bouton "Rechercher" : lance une recherche par tag
        btnSearch.setOnClickListener {
            val tags = searchView.text.toString().trim()
            progressBar.visibility = View.VISIBLE
            if (tags.isNotEmpty()) {
                // Pour une recherche globale, on passe une chaîne vide pour userId
                noteViewModel.searchNotes("", tags)
            } else {
                noteViewModel.fetchAllNotes()
            }
        }

        // Chargement initial de toutes les notes (globales)
        progressBar.visibility = View.VISIBLE
        noteViewModel.fetchAllNotes()
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
            noteViewModel.addFavorite(userId, note.id) { success ->
                if (success) {
                    Toast.makeText(this, "Note ajoutée aux favoris", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Erreur lors de l'ajout aux favoris", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            noteViewModel.removeFavorite(userId, note.id) { success ->
                if (success) {
                    Toast.makeText(this, "Note retirée des favoris", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Erreur lors du retrait des favoris", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
