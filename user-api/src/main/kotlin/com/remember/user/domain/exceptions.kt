package com.remember.user.domain

import com.remember.shared.error.BaseException

class DuplicatedUsername(message: String) : BaseException(message)

class DuplicatedEmail(message: String) : BaseException(message)

class UserNotFound(message: String) : BaseException(message)
