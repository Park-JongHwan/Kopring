package com.example.demo.task.service

import com.example.demo.common.exception.InvalidInputException
import com.example.demo.member.entity.Member
import com.example.demo.member.repository.MemberRepository
import com.example.demo.task.dto.CreateTaskDto
import com.example.demo.task.dto.EditTaskDto
import com.example.demo.task.dto.TaskInfoDto
import com.example.demo.task.entity.Task
import com.example.demo.task.repository.TaskRepository
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
@Transactional
class TaskService(
    private val taskRepository: TaskRepository,
    private val memberRepository: MemberRepository
) {
    fun myTasks(userId: Long): List<TaskInfoDto> {
        val member: Member = memberRepository.findByIdOrNull(userId) ?: throw InvalidInputException("id", "존재하지 않은 유저입니다.")
        val findByMember = taskRepository.findByMemberAndIsDeletedFalse(member)
        return findByMember.map(Task::toDto)
    }

    fun createTask(userId: Long, createTaskDto: CreateTaskDto): String {
        val member: Member = memberRepository.findByIdOrNull(userId) ?: throw InvalidInputException("id", "존재하지 않은 유저입니다.")
        val toDto = createTaskDto.toDto(member)

        taskRepository.save(toDto)
        return "생성되었습니다."
    }

    fun editTask(userId: Long, id: Long, editTaskDto: EditTaskDto): String {
        val member: Member = memberRepository.findByIdOrNull(userId) ?: throw InvalidInputException("id", "존재하지 않은 유저입니다.")
        val task = taskRepository.findByIdAndMember(id, member)
        task.editTask(editTaskDto.title, editTaskDto.content, editTaskDto.priority, editTaskDto.sortOrder)

        return "변경되었습니다."
    }

    fun deleteTask(userId: Long, id: Long): String {
        val member: Member = memberRepository.findByIdOrNull(userId) ?: throw InvalidInputException("id", "존재하지 않은 유저입니다.")
        val task = taskRepository.findByIdAndMember(id, member)
        task.deleteTask(true)

        return "삭제되었습니다."
    }
}