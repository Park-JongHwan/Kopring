package com.example.demo.member.entity

import com.example.demo.common.status.Gender
import com.example.demo.common.status.ROLE
import com.example.demo.member.dto.MemberInfoDto
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.ForeignKey
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import jakarta.persistence.Temporal
import jakarta.persistence.TemporalType
import jakarta.persistence.UniqueConstraint
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Entity
@Table(
    uniqueConstraints = [UniqueConstraint(name = "uk_member_login_id", columnNames = ["loginId"])]
)
class Member(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null,

    @Column(nullable = false, length = 30, updatable = false)
    val loginId: String,

    password: String,

    name: String,

    birthday: LocalDate,

    gender: Gender,

    email: String,
) {
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member")
    val memberRole: List<MemberRole>? = null

    private fun LocalDate.formatDate(): String =
        this.format(DateTimeFormatter.ofPattern("yyyyMMdd"))

    fun toDto(): MemberInfoDto =
        MemberInfoDto(id!!, loginId, name, birthday.formatDate(), gender.name, email)

    @Column(nullable = false, length = 100)
    var password: String = password
        protected set

    @Column(nullable = false, length = 100)
    var name: String = name
        protected set

    @Column(nullable = false)
    var birthday: LocalDate = birthday
        protected set

    @Column(nullable = false, length = 5)
    @Enumerated(EnumType.STRING)
    var gender: Gender = gender
        protected set

    @Column(nullable = false, length = 30)
    var email: String = email
        protected set

    fun updatePassword(newPassword: String) {
        this.password = newPassword
    }

    fun updateInfo(name: String, birthday: LocalDate, gender: Gender, email: String) {
        this.name = name
        this.birthday = birthday
        this.gender = gender
        this.email = email
    }
}

@Entity
class MemberRole (
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null,

    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    val role: ROLE,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = ForeignKey(name = "fk_member_role_member_id"))
    val member: Member,
)