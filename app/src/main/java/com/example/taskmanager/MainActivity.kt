package com.example.taskmanager

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.callbacks.onDismiss
import com.afollestad.materialdialogs.callbacks.onShow
import com.afollestad.materialdialogs.input.getInputField
import com.afollestad.materialdialogs.input.input
import com.firebase.ui.database.FirebaseRecyclerAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.jetbrains.anko.AnkoLogger

class MainActivity : AppCompatActivity(), AnkoLogger {


    companion object {
        const val TODO_KEY = "TODO_KEY"
    }

    private val dbHelper = DbHelper()
    private var activeTodo: Todo? = null


    override fun onCreate(inState: Bundle?) {
        super.onCreate(inState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val recyclerOptions = dbHelper.getRecyclerOptions(this)

        recyclerView.adapter = object : FirebaseRecyclerAdapter<Todo, TodoViewHolder>(recyclerOptions) {

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item, parent, false)

                return TodoViewHolder(view)
            }

            override fun onBindViewHolder(
                viewHolder: TodoViewHolder, position: Int, todo: Todo
            ) {
                val onEditClickListener = View.OnClickListener { startAddEditActivity(todo) }
                val onDeleteClickListener = View.OnClickListener { dbHelper.deleteTodo(todo) }

                viewHolder.bind(todo, onEditClickListener, onDeleteClickListener)
            }
        }

        fab.setOnClickListener { this.startAddEditActivity(Todo()) }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState.apply {
            putParcelable(TODO_KEY, activeTodo)
        })
    }

    private fun startAddEditActivity(todo: Todo) {
        val intent = Intent(this, AddEditTaskActivity::class.java)
        intent.putExtra("TODO", todo)
        startActivity(intent)


        /*MaterialDialog(this).show {
            title(
                if (todo.id == "") R.string.dialog_new_entry_title
                else R.string.dialog_edit_entry_title
            )
            input(
                hintRes = R.string.entry_hint_text,
                inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE,
                waitForPositiveButton = false
            ) { _, text ->
                todo.title = text.toString()
            }

            positiveButton(R.string.save) {
                dbHelper.saveTodo(todo)
            }
            negativeButton(R.string.cancel)
            onShow { dlg ->
                activeTodo = todo
                dlg.getInputField().setText(todo.title)
            }
            onDismiss {
                activeTodo = null//happens after saving activity state if rotation
            }
        }*/
    }
}
