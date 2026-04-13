package com.example.demo.member.service

import com.example.demo.common.authority.JwtTokenProvider
import com.example.demo.common.authority.TokenInfo
import com.example.demo.common.exception.InvalidInputException
import com.example.demo.common.status.ROLE
import com.example.demo.member.dto.ChangePasswordDto
import com.example.demo.member.dto.LoginDto
import com.example.demo.member.dto.MemberDtoRequest
import com.example.demo.member.dto.MemberInfoDto
import com.example.demo.member.dto.UpdateMemberDto
import com.example.demo.member.entity.Member
import com.example.demo.member.entity.MemberRole
import com.example.demo.member.repository.MemberRepository
import com.example.demo.member.repository.MemberRoleRepository
import jakarta.transaction.Transactional
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
@Transactional
class MemberService(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder,
    private val memberRoleRepository: MemberRoleRepository,
    private val authenticationManagerBuilder: AuthenticationManagerBuilder,
    private val jwtTokenProvider: JwtTokenProvider,
) {

    fun signUp(memberDtoRequest: MemberDtoRequest): String {
        val member: Member? = memberRepository.findByLoginId(memberDtoRequest.loginId)
        if (member != null) {
            throw InvalidInputException("loginId", "이미 등록된 ID 입니다.")
        }

        val encodedPassword = passwordEncoder.encode(memberDtoRequest.password)

        val saveMember = memberRepository.save(
            memberDtoRequest.toEntity(encodedPassword!!)
        )

        val memberRole: MemberRole = MemberRole(null, ROLE.USER, saveMember)
        memberRoleRepository.save(memberRole)
        return "회원가입이 완료되었습니다."
    }

    fun login(loginDto: LoginDto): TokenInfo {
        val authenticationToken = UsernamePasswordAuthenticationToken(loginDto.loginId, loginDto.password)
        val authentication = authenticationManagerBuilder.`object`.authenticate(authenticationToken)
        return jwtTokenProvider.createToken(authentication)
    }

    fun searchMyInfo(id: Long): MemberInfoDto {
        val member: Member = memberRepository.findByIdOrNull(id) ?: throw InvalidInputException("id", "존재하지 않은 유저입니다.")
        return member.toDto()
    }

    fun saveMyInfo(id: Long, updateMemberDto: UpdateMemberDto): String {
        val member: Member = memberRepository.findByIdOrNull(id) ?: throw InvalidInputException("id", "존재하지 않은 유저입니다.")
        member.updateInfo(updateMemberDto.name, updateMemberDto.birthday, updateMemberDto.gender, updateMemberDto.email)
        return "수정되었습니다."
    }

    fun changePassword(id: Long, changePasswordDto: ChangePasswordDto): String {
        val member: Member = memberRepository.findByIdOrNull(id) ?: throw InvalidInputException("id", "존재하지 않은 유저입니다.")
        val encodePassword = passwordEncoder.encode(changePasswordDto.password)
        val matches : Boolean = passwordEncoder.matches(member.password, encodePassword)

        if (matches) {
            passwordEncoder.encode(changePasswordDto.newPassword)?.let { member.updatePassword(it) }
            return "변경되었습니다."
        } else {
            throw InvalidInputException("password", "현재 비밀번호가 일치하지 않습니다.")
        }
    }

}