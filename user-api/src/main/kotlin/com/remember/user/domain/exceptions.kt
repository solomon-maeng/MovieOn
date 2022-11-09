package com.remember.user.domain

import com.remember.shared.error.BaseException

class UserNotFound(message: String) : BaseException(message)

class InvariantViolation(message: String) : BaseException(message)
