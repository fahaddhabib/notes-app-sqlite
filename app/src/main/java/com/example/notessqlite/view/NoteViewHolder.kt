package com.example.notessqlite.view

import androidx.recyclerview.widget.RecyclerView
import com.example.notessqlite.databinding.ListItemNoteBinding

class NoteViewHolder(binding: ListItemNoteBinding) : RecyclerView.ViewHolder(binding.root) {
    val title = binding.textViewTitle
    val content = binding.textViewContent
    val edit = binding.imgEdit
    val del = binding.imgDel
    val shareNote = binding.imgShare
}