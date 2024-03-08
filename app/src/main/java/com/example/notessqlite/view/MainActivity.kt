package com.example.notessqlite.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notessqlite.databinding.ActivityMainBinding
import com.example.notessqlite.model.NoteDatabaseHelper

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NoteAdapter
    private lateinit var db: NoteDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //retrieve data from table
        retrieveData()
        //button click to create a new note
        buttonClick()
        // Swipe to delete functionality
        swipeToDelete()
        // Set up search functionality
        setupSearch()
    }

    private fun setupSearch() {
        val searchView = binding.searchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Perform search when the user enters text
                newText?.let {
                    val searchResults = db.searchNotes(it)
                    adapter.refreshData(searchResults)
                }
                return true
            }
        })
    }

    private fun swipeToDelete() {
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val note = adapter.getNoteAtPosition(position)
                db.deleteNoteById(note.id)
                adapter.refreshData(db.getAllNotes())
                Toast.makeText(
                    this@MainActivity,
                    "Note Deleted Successfully",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
    }

    private fun retrieveData() {
        recyclerView = binding.recyclerView
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        db = NoteDatabaseHelper(this)

        // Fetch data from the database and set it to the RecyclerView
        val notes = db.getAllNotes()
        adapter = NoteAdapter(notes, this)
        recyclerView.adapter = adapter
    }

    private fun buttonClick() {
        binding.addButton.setOnClickListener {
            val intent = Intent(this, AddNotesActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        // Fetch updated data from the database and refresh the RecyclerView
        val updatedNotes = db.getAllNotes()
        adapter.refreshData(updatedNotes)
    }
}