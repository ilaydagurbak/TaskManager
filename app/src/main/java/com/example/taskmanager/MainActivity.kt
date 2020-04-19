package com.example.taskmanager

import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
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

        val recyclerOptions = dbHelper.getRecyclerOptions(this)
        val adapter = TodoAdapter(this::startAddEditActivity, recyclerOptions)

        val itemTouchHelper = ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            private val background: ColorDrawable = ColorDrawable()

            override fun onChildDraw(
                canvas: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                super.onChildDraw(
                    canvas,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
                val itemView = viewHolder.itemView

                when {
                    dX > 0 -> { // Swiping to the right
                        background.apply {
                            color = Color.RED
                            setBounds(
                                itemView.left,
                                itemView.top,
                                itemView.left + dX.toInt() + itemView.paddingLeft,
                                itemView.bottom
                            )
                        }
                    }
                    dX < 0 -> { // Swiping to the left
                        if (recyclerView.itemAnimator?.isRunning == false) {
                            val todo = adapter.getTodoAt(viewHolder.adapterPosition)

                            val currentColor = when (todo.taskStatus) {
                                TaskStatus.DONE -> Color.YELLOW
                                TaskStatus.IN_PROGRESS -> Color.GREEN
                            }

                            background.apply {
                                color = currentColor
                                setBounds(
                                    itemView.right + dX.toInt() - itemView.paddingRight,
                                    itemView.top,
                                    itemView.right,
                                    itemView.bottom
                                )
                            }
                        }
                    }
                    else -> { // view is unSwiped
                        background.setBounds(0, 0, 0, 0)
                    }
                }

                if (dX != 0.0F) {
                    background.draw(canvas)
                }
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val todo = adapter.getTodoAt(viewHolder.adapterPosition)
                when (direction) {
                    ItemTouchHelper.LEFT -> {
                        todo.changeStatus()
                        dbHelper.saveTodo(todo)
                        adapter.notifyDataSetChanged()
                    }
                    ItemTouchHelper.RIGHT -> {
                        dbHelper.deleteTodo(todo)
                        Snackbar.make(
                            mainCL,
                            "Todo has been deleted",
                            Snackbar.LENGTH_LONG
                        ).apply {
                            setAction("UNDO") { dbHelper.saveTodo(todo) }
                        }.show()
                    }
                }
            }
        })

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        itemTouchHelper.attachToRecyclerView(recyclerView)

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

