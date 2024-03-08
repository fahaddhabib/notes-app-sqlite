package com.example.notessqlite.model

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class NoteDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{

        private const val DATABASE_NAME = "notesapp.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "allnotes"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_CONTENT = "content"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        // Create your table with the necessary columns
        val createTableQuery = "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_TITLE TEXT, $COLUMN_CONTENT TEXT)"
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTableQuery = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropTableQuery)
        onCreate(db)
    }

    // Function to insert a new note
    fun insertNote(note: Note) {
        // Gets the data repository in write mode
        val db = writableDatabase

        // Create a new map of values, where column names are the keys
        val values = ContentValues().apply {
            put(COLUMN_TITLE, note.title)
            put(COLUMN_CONTENT, note.content)
            // Add more columns if needed
        }
        db.insert(TABLE_NAME, null, values)

        // Closing the database connection
        db.close()
    }

    // Function to retrieve all notes from the database
    fun getAllNotes(): List<Note> {
        val notes = mutableListOf<Note>()
        val db = readableDatabase

        // Define the columns you want to retrieve
        val columns = arrayOf(COLUMN_ID, COLUMN_TITLE, COLUMN_CONTENT)

        // Perform the query to get all notes, ordering by id in descending order
        val cursor: Cursor = db.query(
            TABLE_NAME, columns, null, null, null, null, "$COLUMN_ID DESC"
        // Order by id in descending order
        )

        // Check if the cursor is valid and move to the first row
        if (cursor.moveToFirst()) {
            do {
                val idIndex = cursor.getColumnIndex(COLUMN_ID)
                val titleIndex = cursor.getColumnIndex(COLUMN_TITLE)
                val contentIndex = cursor.getColumnIndex(COLUMN_CONTENT)

                // Check if the column index is valid
                if (idIndex != -1 && titleIndex != -1 && contentIndex != -1) {
                    val id = cursor.getInt(idIndex)
                    val title = cursor.getString(titleIndex)
                    val content = cursor.getString(contentIndex)
                    notes.add(Note(id, title, content))
                } else {
                    // Handle the case where a column index is not found
                    // Log an error, throw an exception, or handle it as appropriate for your app
                }
            } while (cursor.moveToNext())
        }

        // Close the cursor and database connection
        cursor.close()
        db.close()

        return notes
    }

    fun updateNote(note: Note) {
        val db = writableDatabase

        val values = ContentValues().apply {
            put(COLUMN_TITLE, note.title)
            put(COLUMN_CONTENT, note.content)
        }

        db.update(TABLE_NAME, values,
            "$COLUMN_ID = ?",
            arrayOf(note.id.toString()))

        db.close()
    }

    fun getNoteById(noteId: Int): Note{
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = $noteId"
        val cursor = db.rawQuery(query,null)
        cursor.moveToFirst()

        val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
        val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
        val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT))

        cursor.close()
        db.close()

        return Note(id,title,content)
    }

    fun deleteNoteById(noteId:Int){
        val db = writableDatabase
        db.delete(TABLE_NAME, "$COLUMN_ID=?", arrayOf(noteId.toString()))
        db.close()
    }

    //search note
    fun searchNotes(query: String): List<Note> {
        val notes = mutableListOf<Note>()
        val db = readableDatabase

        // Define the columns you want to retrieve
        val columns = arrayOf(COLUMN_ID, COLUMN_TITLE, COLUMN_CONTENT)

        // Perform the query to search notes, ordering by id in descending order
        val cursor: Cursor = db.query(
            TABLE_NAME, columns, "$COLUMN_TITLE LIKE ? OR $COLUMN_CONTENT LIKE ?",
            arrayOf("%$query%", "%$query%"), null, null, "$COLUMN_ID DESC"
        )

        // Check if the cursor is valid and move to the first row
        if (cursor.moveToFirst()) {
            do {
                val idIndex = cursor.getColumnIndex(COLUMN_ID)
                val titleIndex = cursor.getColumnIndex(COLUMN_TITLE)
                val contentIndex = cursor.getColumnIndex(COLUMN_CONTENT)

                // Check if the column index is valid
                if (idIndex != -1 && titleIndex != -1 && contentIndex != -1) {
                    val id = cursor.getInt(idIndex)
                    val title = cursor.getString(titleIndex)
                    val content = cursor.getString(contentIndex)
                    notes.add(Note(id, title, content))
                } else {
                    // Handle the case where a column index is not found
                    // Log an error, throw an exception, or handle it as appropriate for your app
                }
            } while (cursor.moveToNext())
        }

        // Close the cursor and database connection
        cursor.close()
        db.close()

        return notes
    }


}