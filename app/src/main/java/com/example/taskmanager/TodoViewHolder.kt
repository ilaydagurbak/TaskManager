package com.example.taskmanager

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.list_item.*


class TodoViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(
        todo: Todo,
        editClickListener: View.OnClickListener,
        deleteClickListener: View.OnClickListener
    ) {
        containerView.tag = todo.id
        title.text = todo.title
        titleTw.text = todo.description
        val resource = when (todo.taskType) {
            TaskType.TODO -> R.drawable.ic_assignment_black_24dp
            TaskType.CALL -> R.drawable.ic_phone_black_24dp
            TaskType.MEETING -> R.drawable.ic_people_black_24dp
            TaskType.EMAIL -> R.drawable.ic_email_black_24dp
        }
        imageView.setImageDrawable(containerView.context.getDrawable(resource))

        editBtn.setOnClickListener(editClickListener)
        delBtn.setOnClickListener(deleteClickListener)
    }
}
