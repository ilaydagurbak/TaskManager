package com.example.taskmanager

import androidx.lifecycle.LifecycleOwner
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


const val PATH = "todos"
const val TITLE = "title"
const val DESCRIPTION = "description"
const val TASK_TYPE = "taskType"
const val TASK_STATUS = "taskStatus"
const val DUE_DATE_TIMESTAMP = "dueDateTimestamp"

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
        todosRef.child(todo.id).apply {
            child(TITLE).setValue(todo.title)
            child(DESCRIPTION).setValue(todo.description)
            child(DUE_DATE_TIMESTAMP).setValue(todo.dueDateTimestamp)
            child(TASK_TYPE).setValue(todo.taskType)
            child(TASK_STATUS).setValue(todo.taskStatus)
        }
    }

    fun deleteTodo(todo: Todo) {
        todosRef.child(todo.id).setValue(null)
    }

    fun getRecyclerOptions(owner: LifecycleOwner) = FirebaseRecyclerOptions.Builder<Todo>()
        .setQuery(todosRef.orderByKey(), Todo::class.java)
        .setLifecycleOwner(owner)
        .build()

}