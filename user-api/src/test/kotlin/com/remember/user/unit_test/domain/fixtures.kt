package com.remember.user.unit_test.domain

import com.remember.shared.contracts.LoginUserCommand
import com.remember.shared.contracts.RegisterUserCommand
import com.remember.shared.contracts.RegisteredUserConfirmCommand

data class SetUpRegisterUserCommand(val command: RegisterUserCommand, val actual: String)
data class SetUpRegisterConfirmCommand(val command: RegisteredUserConfirmCommand, val actual: String)
data class SetUpLoginUserCommand(val command: LoginUserCommand, val actual: String)
