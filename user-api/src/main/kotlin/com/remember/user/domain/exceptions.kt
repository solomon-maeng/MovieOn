package com.remember.user.domain

import com.remember.shared.error.BaseException

class UserNotFound : BaseException("유저가 존재하지 않습니다.")

class InvariantViolation(message: String) : BaseException(message)
