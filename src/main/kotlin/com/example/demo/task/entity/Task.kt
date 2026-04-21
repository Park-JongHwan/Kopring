package com.example.demo.task.entity

import com.example.demo.common.status.Priority
import com.example.demo.member.entity.Member
import com.example.demo.task.dto.EditTaskDto
import com.example.demo.task.dto.TaskInfoDto
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.PrePersist
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import java.time.LocalDateTime


@Entity
@Table(
    uniqueConstraints = [UniqueConstraint(name = "uk_task_id", columnNames = ["id"])]
)
class Task(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long? = null,

    title: String,

    content: String,

    priority: Priority = Priority.MEDIUM,

    sortOrder: Int,

    var createdAt: LocalDateTime? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    var member: Member,

    isDeleted: Boolean,
) {
    fun toDto(): TaskInfoDto =
        TaskInfoDto(id!!, title, content, priority.name, sortOrder, createdAt.toString())

    @Column(nullable = false)
    var title: String = title
        protected set

    @Column(columnDefinition = "TEXT")
    var content: String = content
        protected set

    @Enumerated(EnumType.STRING)
    var priority: Priority = priority
        protected set

    @Column(name = "sort_order")
    var sortOrder: Int = sortOrder
        protected set

    @Column(name = "is_deleted")
    var isDeleted: Boolean = false
        protected set

    @PrePersist
    fun onCreate() {
        createdAt = LocalDateTime.now()
    }

    fun editTask(
        title: String,
        content: String,
        priority: Priority,
        sortOrder: Int) {
        this.title = title
        this.content = content
        this.priority = priority
        this.sortOrder = sortOrder
    }

    fun deleteTask(isDeleted: Boolean) {
        this.isDeleted = isDeleted
    }
}