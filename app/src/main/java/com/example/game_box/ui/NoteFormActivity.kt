package com.example.game_box.ui

import android.os.Bundle
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.example.game_box.R
import com.example.game_box.data.model.Note
import com.example.game_box.data.repository.NoteRepository
import com.example.game_box.viewmodel.NoteViewModel
import com.example.game_box.viewmodel.NoteViewModelFactory

class NoteFormActivity : AppCompatActivity() {

    private lateinit var noteViewModel: NoteViewModel

    private lateinit var etTitle: EditText
    private lateinit var etDescription: EditText
    private lateinit var etSourceUrl: EditText

    private lateinit var spinnerTags: Spinner
    private lateinit var tvSelectedTags: TextView
    private lateinit var btnAddTag: Button

    private lateinit var btnCreateNote: Button
    private lateinit var btnCancelNote: Button

    private val availableTags = listOf(
        "Java", "Kotlin", "Python", "C++",
        "DevOps", "Cloud", "SQL", "NoSQL",
        "Docker", "Spring", "Android", "Angular"
    )

    private val selectedTags = mutableListOf<String>()

    private var noteId: Long = 0
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_form)

        // Configuration de la Toolbar avec flèche "up"
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Formulaire de note"

        etTitle = findViewById(R.id.etTitle)
        etDescription = findViewById(R.id.etDescription)
        etSourceUrl = findViewById(R.id.etSourceUrl)

        spinnerTags = findViewById(R.id.spinnerTags)
        tvSelectedTags = findViewById(R.id.tvSelectedTags)
        btnAddTag = findViewById(R.id.btnAddTag)

        btnCreateNote = findViewById(R.id.btnCreateNote)
        btnCancelNote = findViewById(R.id.btnCancelNote)

        userId = intent.getStringExtra("USER_ID") ?: ""
        noteId = intent.getLongExtra("NOTE_ID", 0)

        // Configuration du spinner
        val adapterSpinner = android.widget.ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            availableTags
        )
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTags.adapter = adapterSpinner

        val repository = NoteRepository()
        val factory = NoteViewModelFactory(repository)
        noteViewModel = ViewModelProvider(this, factory).get(NoteViewModel::class.java)

        btnAddTag.setOnClickListener {
            val chosenTag = spinnerTags.selectedItem.toString()
            if (!selectedTags.contains(chosenTag)) {
                selectedTags.add(chosenTag)
                tvSelectedTags.text = selectedTags.joinToString(", ")
            } else {
                Toast.makeText(this, "Tag déjà ajouté", Toast.LENGTH_SHORT).show()
            }
        }

        btnCreateNote.setOnClickListener { saveNote() }
        btnCancelNote.setOnClickListener { finish() }
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        return when(item.itemId) {
            android.R.id.home -> { finish(); true }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun saveNote() {
        val title = etTitle.text.toString().trim()
        val description = etDescription.text.toString().trim()
        val sourceUrl = etSourceUrl.text.toString().trim()

        if (title.isEmpty() || description.isEmpty() || sourceUrl.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir Titre, Description et URL obligatoires", Toast.LENGTH_SHORT).show()
            return
        }

        if (selectedTags.isEmpty()) {
            Toast.makeText(this, "Veuillez sélectionner au moins un tag", Toast.LENGTH_SHORT).show()
            return
        }

        val note = Note(
            id = noteId,
            title = title,
            description = description,
            tags = selectedTags,
            userId = userId,
            sourceUrl = sourceUrl
        )

        if (noteId == 0L) {
            noteViewModel.createNote(note) { success ->
                if (success) {
                    Toast.makeText(this, "Note créée avec succès", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Erreur lors de la création de la note", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            noteViewModel.updateNote(noteId, note) { success ->
                if (success) {
                    Toast.makeText(this, "Note mise à jour avec succès", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Erreur lors de la mise à jour", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
