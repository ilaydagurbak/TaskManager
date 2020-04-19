package com.example.taskmanager

import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

@Parcelize
@IgnoreExtraProperties
data class Todo(
    var id: String = "",
    var title: String = "",
    var description: String = "",
    var dueDateTimestamp: Long = ZonedDateTime.now().toEpochSecond(),
    var taskStatus: TaskStatus = TaskStatus.IN_PROGRESS,
    var taskType: TaskType = TaskType.TODO
) : Parcelable {

    // can't be named like java beans convention, it causes problems with firebase
    fun dueDate(): LocalDateTime = ZonedDateTime.ofInstant(
        Instant.ofEpochSecond(dueDateTimestamp),
        ZoneId.systemDefault()
    ).toLocalDateTime()

    fun dueDate(value: LocalDateTime) {
        dueDateTimestamp = ZonedDateTime.of(value, ZoneId.systemDefault()).toEpochSecond()
    }

    fun changeStatus() {
        taskStatus = when (taskStatus) {
            TaskStatus.IN_PROGRESS -> TaskStatus.DONE
            TaskStatus.DONE -> TaskStatus.IN_PROGRESS
        }
    }

}
