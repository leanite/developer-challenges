package com.leanite.dynaquiz.core.domain.usecase

import com.leanite.dynaquiz.core.domain.repository.UserRepository

class SetLastNicknameUseCase(
    private val repository: UserRepository,
) {
    suspend operator fun invoke(nickname: String) {
        repository.setLastNickname(nickname)
    }
}
