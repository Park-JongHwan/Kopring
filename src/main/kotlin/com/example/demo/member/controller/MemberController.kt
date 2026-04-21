package com.example.demo.member.controller

import com.example.demo.common.authority.TokenInfo
import com.example.demo.common.dto.BaseResponse
import com.example.demo.common.dto.CustomUser
import com.example.demo.common.status.ResultCode
import com.example.demo.member.dto.ChangePasswordDto
import com.example.demo.member.dto.LoginDto
import com.example.demo.member.dto.MemberDtoRequest
import com.example.demo.member.dto.MemberInfoDto
import com.example.demo.member.dto.UpdateMemberDto
import com.example.demo.member.service.MemberService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/member")
class MemberController(private val memberService: MemberService) {

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    fun signUp(@RequestBody @Valid memberDtoRequest: MemberDtoRequest): BaseResponse<Unit> {
        memberService.signUp(memberDtoRequest)
        return BaseResponse.success(Unit)
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    fun login(@RequestBody @Valid loginDto: LoginDto): BaseResponse<TokenInfo> {
        val tokenInfo = memberService.login(loginDto)
        return BaseResponse.success(tokenInfo)
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun memberInfo(): BaseResponse<MemberInfoDto> {
        val userId = (SecurityContextHolder.getContext().authentication?.principal as CustomUser).userId
        val response = memberService.searchMyInfo(userId)
        return BaseResponse.success(response)
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun saveMyInfo(@RequestBody @Valid updateMemberDto: UpdateMemberDto): BaseResponse<String> {
        val userId = (SecurityContextHolder.getContext().authentication?.principal as CustomUser).userId
        val resultMsg: String = memberService.saveMyInfo(userId, updateMemberDto)
        return BaseResponse.success(resultMsg)
    }

    @PatchMapping("/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun changePassword(@RequestBody @Valid changePasswordDto: ChangePasswordDto): BaseResponse<String> {
        val userId = (SecurityContextHolder.getContext().authentication?.principal as CustomUser).userId
        val resultMsg: String = memberService.changePassword(userId, changePasswordDto)
        return BaseResponse.success(resultMsg)
    }
}