package com.example.taskmanager

import android.view.LayoutInflater
import android.view.ViewGroup
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

class TodoAdapter(
    private val onClickCallback: (Todo) -> Unit,
    recyclerOptions: FirebaseRecyclerOptions<Todo>
) : FirebaseRecyclerAdapter<Todo, TodoViewHolder>(recyclerOptions) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)

        return TodoViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: TodoViewHolder, position: Int, todo: Todo) {
        viewHolder.bind(todo, onClickCallback)
    }

    fun getTodoAt(position: Int): Todo = getItem(position)

}
