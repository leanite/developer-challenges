package com.leanite.dynaquiz.core.domain.usecase

import com.leanite.dynaquiz.core.domain.repository.UserRepository

class GetLastNicknameUseCase(
    private val repository: UserRepository,
) {
    suspend operator fun invoke(): String? = repository.getLastNickname()
}