package com.example.demo.task.controller

import com.example.demo.common.dto.BaseResponse
import com.example.demo.common.dto.CustomUser
import com.example.demo.task.dto.CreateTaskDto
import com.example.demo.task.dto.EditTaskDto
import com.example.demo.task.dto.TaskInfoDto
import com.example.demo.task.service.TaskService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/tasks")
class TaskController(
    private val taskService: TaskService
) {

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun myTasks(): BaseResponse<List<TaskInfoDto>> {
        val userId = (SecurityContextHolder.getContext().authentication?.principal as CustomUser).userId
        val response = taskService.myTasks(userId)
        return BaseResponse.success(response)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createTask(@RequestBody @Valid createTaskDto: CreateTaskDto): BaseResponse<Unit> {
        val userId = (SecurityContextHolder.getContext().authentication?.principal as CustomUser).userId
        taskService.createTask(userId, createTaskDto)
        return BaseResponse.success(Unit)
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun editTask(@PathVariable id: Long, @RequestBody @Valid editTaskDto: EditTaskDto): BaseResponse<Unit> {
        val userId = (SecurityContextHolder.getContext().authentication?.principal as CustomUser).userId
        taskService.editTask(userId, id, editTaskDto)
        return BaseResponse.success(Unit)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteTask(@PathVariable id: Long): BaseResponse<Unit> {
        val userId = (SecurityContextHolder.getContext().authentication?.principal as CustomUser).userId
        taskService.deleteTask(userId, id)
        return BaseResponse.success(Unit)
    }

}