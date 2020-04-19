package com.example.taskmanager

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import kotlinx.android.synthetic.main.activity_add_edit_task.*
import kotlinx.android.synthetic.main.content_add_edit_task.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class AddEditTaskActivity : AppCompatActivity() {

    private lateinit var todo: Todo
    private val dbHelper = DbHelper()
    private val dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_task)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        addEditTaskCL.isSoundEffectsEnabled = false
        addEditTaskCL.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                hideKeyboard(view)
            }
        }

        fab.setOnClickListener {
            updateTodo()
            dbHelper.saveTodo(todo)
            finish()
        }

        dueDateEt.setOnFocusChangeListener { view, hasFocus ->
            val dueDate = todo.dueDate()
            val onDateSetListener =
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    val onTimeSetListener =
                        TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                            todo.dueDate(
                                LocalDateTime.of(
                                    LocalDate.of(year, month + 1, dayOfMonth),
                                    LocalTime.of(hourOfDay, minute)
                                )
                            )

                            dueDateEt.setText(dateTimeFormatter.format(dueDate))
                        }
                    TimePickerDialog(
                        this,
                        onTimeSetListener,
                        dueDate.hour,
                        dueDate.minute,
                        DateFormat.is24HourFormat(this)
                    ).show()
                }

            if (hasFocus) {
                hideKeyboard(view)
                DatePickerDialog(
                    this,
                    onDateSetListener,
                    dueDate.year,
                    dueDate.monthValue - 1,
                    dueDate.dayOfMonth
                ).show()
                view.clearFocus()
            }
        }

        taskTypeSpinner.adapter = ArrayAdapter.createFromResource(
            this, R.array.task_types, android.R.layout.simple_spinner_item
        )
            .also { arrayAdapter -> arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }
        taskTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) = Unit

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                todo.taskType = TaskTypeConverter.toTaskType(position)
            }

        }

        setData()
    }

    private fun setData() {
        todo = intent.getParcelableExtra("TODO") ?: throw IllegalStateException()
        titleEt.setText(todo.title)
        descriptionEt.setText(todo.description)
        dueDateEt.setText(dateTimeFormatter.format(todo.dueDate()))
        taskTypeSpinner.setSelection(TaskTypeConverter.fromTaskType(todo.taskType), true)
    }

    private fun updateTodo() {
        todo.apply {
            title = titleEt.text.toString()
            description = descriptionEt.text.toString()
        }
    }

    private fun hideKeyboard(view: View) {
        val imm = getSystemService<InputMethodManager>()
        imm?.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    object TaskTypeConverter {
        fun fromTaskType(taskType: TaskType): Int = when (taskType) {
            TaskType.CALL -> 0
            TaskType.MEETING -> 1
            TaskType.TODO -> 2
            TaskType.EMAIL -> 3
        }

        fun toTaskType(intValue: Int) = when (intValue) {
            0 -> TaskType.CALL
            1 -> TaskType.MEETING
            2 -> TaskType.TODO
            3 -> TaskType.EMAIL
            else -> throw IllegalStateException()
        }
    }

}
