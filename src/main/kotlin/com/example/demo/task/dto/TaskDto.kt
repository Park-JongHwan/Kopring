package com.example.demo.task.dto

import com.example.demo.common.status.Priority
import com.example.demo.member.entity.Member
import com.example.demo.task.entity.Task
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class TaskInfoDto(
    val id: Long,
    val title: String,
    val content: String,
    val priority: String,
    val sortOrder: Int,
    val createdAt: String
)

data class CreateTaskDto(
    var id: Long?,

    @field:NotBlank
    @JsonProperty("title")
    private val _title: String?,

    @field:NotBlank
    @JsonProperty("content")
    private val _content: String?,

    @JsonProperty("priority")
    private val _priority: Priority?,

    @JsonProperty("sortOrder")
    private val _sortOrder: Int?,

) {
    val title: String
        get() = _title!!
    val content: String
        get() = _content!!
    val priority: Priority
        get() = _priority!!
    val sortOrder: Int
        get() = _sortOrder!!

    fun toDto(member: Member): Task =
        Task(id, title, content, priority, sortOrder, LocalDateTime.now(), member, false)
}

data class EditTaskDto(
    @field:NotBlank
    @JsonProperty("title")
    private val _title: String?,

    @field:NotBlank
    @JsonProperty("content")
    private val _content: String?,

    @JsonProperty("priority")
    private val _priority: Priority?,

    @JsonProperty("sortOrder")
    private val _sortOrder: Int?,
) {
    val title: String
        get() = _title!!
    val content: String
        get() = _content!!
    val priority: Priority
        get() = _priority!!
    val sortOrder: Int
        get() = _sortOrder!!
}