package com.example.taskmanager

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.list_item.*
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class TodoViewHolder(
    override val containerView: View
) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    private val dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)

    fun bind(todo: Todo, onClickCallback: (Todo) -> Unit) {
        containerView.tag = todo.id
        containerView.setOnClickListener { onClickCallback(todo) }

        title.text = todo.title
        description.text = todo.description
        dueDate.text = dateTimeFormatter.format(todo.dueDate())
        imageView.setImageDrawable(containerView.context.getDrawable(getIconId(todo.taskType)))
    }

    private fun getIconId(taskType: TaskType): Int {
        return when (taskType) {
            TaskType.TODO -> R.drawable.ic_assignment_black_24dp
            TaskType.CALL -> R.drawable.ic_phone_black_24dp
            TaskType.MEETING -> R.drawable.ic_people_black_24dp
            TaskType.EMAIL -> R.drawable.ic_email_black_24dp
        }
    }

}
