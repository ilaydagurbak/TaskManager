package com.example.taskmanager

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_add_edit_task.*
import kotlinx.android.synthetic.main.content_add_edit_task.*
import java.time.LocalDate
import java.time.LocalTime


class AddEditTaskActivity : AppCompatActivity() {

    private var dueDate: LocalDate? = null
    private var dueTime: LocalTime? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_task)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        datetask.setOnClickListener {
            val time: LocalTime = LocalTime.now()
            val date: LocalDate = LocalDate.now()

            val onDateSetListener =
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    val onTimeSetListener =
                        TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                            dueTime = LocalTime.of(hourOfDay, minute)
                            dueDate = LocalDate.of(year, month + 1, dayOfMonth)

                            datetask.setText(dueDate?.toString() + " " + dueTime?.toString())
                        }
                    TimePickerDialog(
                        this,
                        onTimeSetListener,
                        time.hour,
                        time.minute,
                        android.text.format.DateFormat.is24HourFormat(this)
                    ).show()
                }

            DatePickerDialog(
                this,
                onDateSetListener,
                date.year,
                date.monthValue - 1,
                date.dayOfMonth
            ).show()
        }
    }
}
