package com.remember.user.infrastructure.jwt

import com.remember.shared.error.BaseException

class RefreshTokenNotFound : BaseException("RefreshToken을 찾을 수 없습니다.")
class InvalidArgument : BaseException("잘못된 입력입니다.")
class AlreadyExpiredToken : BaseException("이미 만료된 토큰입니다.")
class InvalidToken(message: String) : BaseException(message)
