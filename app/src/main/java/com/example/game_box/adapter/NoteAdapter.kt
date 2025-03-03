package com.example.game_box.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.game_box.R
import com.example.game_box.data.model.Note

class NoteAdapter(
    private var notes: List<Note>,
    private val currentUserId: String,
    private val onNoteClick: (Note) -> Unit,
    private val onFavoriteClick: (Note, Boolean) -> Unit,
    private val onDeleteClick: (Note) -> Unit
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    inner class NoteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val noteTitle: TextView = view.findViewById(R.id.noteTitle)
        val noteDescription: TextView = view.findViewById(R.id.noteDescription)
        val btnJoin: Button = view.findViewById(R.id.btnJoin)
        val btnFavorite: ImageView = view.findViewById(R.id.btnFavorite)
        val btnDelete: Button = view.findViewById(R.id.btnDeleteNote)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.noteTitle.text = note.title
        holder.noteDescription.text = note.description

        // Bouton supprimer visible seulement si la note appartient à l'utilisateur
        if (note.userId == currentUserId) {
            holder.btnDelete.visibility = View.VISIBLE
            holder.btnDelete.setOnClickListener { onDeleteClick(note) }
        } else {
            holder.btnDelete.visibility = View.GONE
        }

        // Met à jour l'icône de favoris selon l'état actuel
        holder.btnFavorite.setImageResource(
            if (note.isFavorite) R.drawable.ic_favorite_filled else R.drawable.ic_favorite_border
        )

        // Clic sur l'icône favoris
        holder.btnFavorite.setOnClickListener {
            val newFavState = !note.isFavorite
            onFavoriteClick(note, newFavState)
        }

        // Clic sur l'item pour afficher le détail
        holder.itemView.setOnClickListener { onNoteClick(note) }

        // Bouton Join pour ouvrir l'URL source, le cas échéant
        holder.btnJoin.setOnClickListener {
            if (!note.sourceUrl.isNullOrBlank()) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(note.sourceUrl))
                it.context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int = notes.size

    fun updateNotes(newNotes: List<Note>) {
        notes = newNotes
        notifyDataSetChanged()
    }
}
