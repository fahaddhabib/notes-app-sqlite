package com.example.notessqlite.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.notessqlite.databinding.ActivityAddNotesBinding
import com.example.notessqlite.model.Note
import com.example.notessqlite.model.NoteDatabaseHelper

class AddNotesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddNotesBinding
    private lateinit var db: NoteDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = NoteDatabaseHelper(this)

        //clicking save button to insert a note and then showing toast as message

        binding.savebutton.setOnClickListener {
            val title = binding.editTextTitle.text.toString()
            val content = binding.editTextDescription.text.toString()

            val note = Note(0, title, content)
            db.insertNote(note)
            finish()
            Toast.makeText(applicationContext, "Note Saved Successfully", Toast.LENGTH_SHORT).show()
        }
    }
}