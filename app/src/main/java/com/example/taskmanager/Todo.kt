package com.example.taskmanager

import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize
import java.time.LocalDateTime


@Parcelize
@IgnoreExtraProperties
data class Todo(
    var id: String = "",
    var title: String = "",
    var description: String = "",
    var dueDate: LocalDateTime= LocalDateTime.now(),
    var taskStatus: TaskStatus = TaskStatus.IN_PROGRESS,
    var taskType: TaskType = TaskType.TODO
) : Parcelable
