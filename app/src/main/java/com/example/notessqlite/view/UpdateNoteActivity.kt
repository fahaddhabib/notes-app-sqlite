package com.example.notessqlite.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.notessqlite.databinding.ActivityUpdateNoteBinding
import com.example.notessqlite.model.Note
import com.example.notessqlite.model.NoteDatabaseHelper

class UpdateNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateNoteBinding
    private lateinit var db: NoteDatabaseHelper
    private var noteId:Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = NoteDatabaseHelper(this)

        noteId = intent.getIntExtra("note_id", -1)
        if(noteId == -1)
        {
            finish()
            return
        }

        val note = db.getNoteById(noteId)
        binding.editTextUpdateTitle.setText(note.title)
        binding.editTextUpdateContent.setText(note.content)

        binding.updatebutton.setOnClickListener {
            val newTitle = binding.editTextUpdateTitle.text.toString()
            val newContent = binding.editTextUpdateContent.text.toString()

            val updateNote = Note(noteId, newTitle, newContent)
            db.updateNote(updateNote)
            finish()
            Toast.makeText(applicationContext, "Note Updated Successfully", Toast.LENGTH_SHORT).show()
        }

    }
}