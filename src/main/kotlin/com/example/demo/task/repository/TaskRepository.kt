package com.example.demo.task.repository

import com.example.demo.member.entity.Member
import com.example.demo.task.entity.Task
import org.springframework.data.jpa.repository.JpaRepository

interface TaskRepository : JpaRepository<Task, Long> {
    fun findByMemberAndIsDeletedFalse(member: Member): List<Task>

    fun findByIdAndMember(id: Long, member: Member): Task
}