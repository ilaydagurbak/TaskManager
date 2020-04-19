package com.example.taskmanager

import androidx.lifecycle.LifecycleOwner
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


const val PATH = "todos"
const val TITLE_PATH = "title"
const val TITLE_DESCRIPTION = "titledescription"

class DbHelper {
    private val todosRef: DatabaseReference = FirebaseDatabase.getInstance().getReference(PATH)

    init {
        todosRef.keepSynced(true)
    }

    fun saveTodo(todo: Todo) {
        if (todo.id == "") createTodo(todo)
        else updateTodo(todo)
    }

    private fun createTodo(todo: Todo) {

        todo.id = todosRef.push().key!!
        todosRef.child(todo.id).setValue(todo)

    }

    private fun updateTodo(todo: Todo) {

        todosRef.child(todo.id).child(TITLE_PATH).setValue(todo.title)
        todosRef.child(todo.id).child(TITLE_DESCRIPTION).setValue(todo.description)
    }

    fun deleteTodo(todo: Todo) {
        todosRef.child(todo.id).setValue(null)
    }

    fun getRecyclerOptions(owner: LifecycleOwner) = FirebaseRecyclerOptions.Builder<Todo>()
        .setQuery(todosRef.orderByKey(), Todo::class.java)
        .setLifecycleOwner(owner)
        .build()

}