package com.leanite.dynaquiz.core.domain.usecase

import com.leanite.dynaquiz.core.domain.repository.UserRepository

class GetLastNicknameUseCase(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(): String? = userRepository.getLastNickname()
}