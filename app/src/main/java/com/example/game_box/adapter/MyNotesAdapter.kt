package com.example.game_box.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.game_box.R
import com.example.game_box.data.model.Note

class MyNotesAdapter(
    private var notes: List<Note>,
    private val onDeleteClick: (Note) -> Unit,
    private val onNoteClick: (Note) -> Unit
) : RecyclerView.Adapter<MyNotesAdapter.MyNotesViewHolder>() {

    inner class MyNotesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val noteTitle: TextView = view.findViewById(R.id.noteTitle)
        val noteDescription: TextView = view.findViewById(R.id.noteDescription)
        val btnDelete: Button = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyNotesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_my_note, parent, false)
        return MyNotesViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyNotesViewHolder, position: Int) {
        val note = notes[position]
        holder.noteTitle.text = note.title
        holder.noteDescription.text = note.description
        holder.itemView.setOnClickListener { onNoteClick(note) }
        holder.btnDelete.setOnClickListener { onDeleteClick(note) }
    }

    override fun getItemCount(): Int = notes.size

    fun updateNotes(newNotes: List<Note>) {
        notes = newNotes
        notifyDataSetChanged()
    }
}
