package com.example.demo.common.dto

import com.example.demo.common.status.ResultCode

data class BaseResponse<T>(
    val success: Boolean,
    val resultCode: ResultCode,
    val data: T? = null,
    val message: String
) {
    companion object {

        fun <T> success(data: T): BaseResponse<T> {
            return BaseResponse(
                success = true,
                resultCode = ResultCode.SUCCESS,
                data = data,
                message = ResultCode.SUCCESS.msg
            )
        }

        fun success(): BaseResponse<Unit> {
            return BaseResponse(
                success = true,
                resultCode = ResultCode.SUCCESS,
                data = null,
                message = ResultCode.SUCCESS.msg
            )
        }

        fun <T> error(resultCode: ResultCode): BaseResponse<T> {
            return BaseResponse(
                success = false,
                resultCode = resultCode,
                data = null,
                message = resultCode.msg
            )
        }
    }
}