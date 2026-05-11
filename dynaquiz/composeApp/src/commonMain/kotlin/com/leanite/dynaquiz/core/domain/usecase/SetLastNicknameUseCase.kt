package com.leanite.dynaquiz.core.domain.usecase

import com.leanite.dynaquiz.core.domain.repository.UserRepository

class SetLastNicknameUseCase(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(nickname: String) {
        userRepository.setLastNickname(nickname)
    }
}