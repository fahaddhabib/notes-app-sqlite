package com.example.notessqlite.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.notessqlite.databinding.ListItemNoteBinding
import com.example.notessqlite.model.Note
import com.example.notessqlite.model.NoteDatabaseHelper


class NoteAdapter(private var notes: List<Note>, context: Context) : RecyclerView.Adapter<NoteViewHolder>() {

    private val db: NoteDatabaseHelper = NoteDatabaseHelper(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = ListItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]

        holder.title.text = note.title
        holder.content.text = note.content

        holder.edit.setOnClickListener {
            val intent = Intent(holder.itemView.context, UpdateNoteActivity::class.java).apply {
                putExtra("note_id", note.id)
            }
            holder.itemView.context.startActivity(intent)
        }

        holder.del.setOnClickListener {

            db.deleteNoteById(note.id)
            refreshData(db.getAllNotes())
            Toast.makeText(holder.itemView.context, "Note Deleted Successfully", Toast.LENGTH_SHORT)
                .show()
        }

        holder.shareNote.setOnClickListener {

            shareNote(holder.itemView.context, note)
        }
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun refreshData(newNotes: List<Note>){
        notes = newNotes
        notifyDataSetChanged()
    }

    //for swipe to delete
    fun getNoteAtPosition(position: Int): Note {
        return notes[position]
    }

    // share note
    private fun shareNote(context: Context, note: Note){
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        val shareMessage = "Note Title: ${note.title}\nNote Content: ${note.content}"
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
        context.startActivity(Intent.createChooser(shareIntent, "Share Note"))
    }
}