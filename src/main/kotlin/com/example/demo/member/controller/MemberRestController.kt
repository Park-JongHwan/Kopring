package com.example.demo.member.controller

import com.example.demo.common.authority.TokenInfo
import com.example.demo.common.dto.BaseResponse
import com.example.demo.common.dto.CustomUser
import com.example.demo.member.dto.ChangePasswordDto
import com.example.demo.member.dto.LoginDto
import com.example.demo.member.dto.MemberDtoRequest
import com.example.demo.member.dto.MemberInfoDto
import com.example.demo.member.dto.UpdateMemberDto
import com.example.demo.member.service.MemberService
import jakarta.validation.Valid
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/member")
class MemberRestController(private val memberService: MemberService) {

    @PostMapping("/signup")
    fun signUp(@RequestBody @Valid memberDtoRequest: MemberDtoRequest): BaseResponse<Unit> {
        val resultMsg: String = memberService.signUp(memberDtoRequest)
        return BaseResponse(message = resultMsg)
    }

    @PostMapping("/login")
    fun login(@RequestBody @Valid loginDto: LoginDto): BaseResponse<TokenInfo> {
        val tokenInfo = memberService.login(loginDto)
        return BaseResponse(data = tokenInfo)
    }

    @GetMapping
    fun memberInfo(): BaseResponse<MemberInfoDto> {
        val userId = (SecurityContextHolder.getContext().authentication?.principal as CustomUser).userId
        val response = memberService.searchMyInfo(userId)
        return BaseResponse(data = response)
    }

    @PatchMapping
    fun saveMyInfo(@RequestBody @Valid updateMemberDto: UpdateMemberDto): BaseResponse<String> {
        val userId = (SecurityContextHolder.getContext().authentication?.principal as CustomUser).userId
        val resultMsg: String = memberService.saveMyInfo(userId, updateMemberDto)
        return BaseResponse(message = resultMsg)
    }

    @PatchMapping("/password")
    fun changePassword(@RequestBody @Valid changePasswordDto: ChangePasswordDto): BaseResponse<String> {
        val userId = (SecurityContextHolder.getContext().authentication?.principal as CustomUser).userId
        val resultMsg: String = memberService.changePassword(userId, changePasswordDto)
        return BaseResponse(data = resultMsg)
    }
}