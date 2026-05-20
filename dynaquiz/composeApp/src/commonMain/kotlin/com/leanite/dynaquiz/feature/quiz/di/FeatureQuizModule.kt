package com.leanite.dynaquiz.feature.quiz.di

import com.leanite.dynaquiz.feature.quiz.QuizViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val featureQuizModule =
    module {
        viewModel { params ->
            QuizViewModel(
                setup = params.get(),
                getRandomQuestionUseCase = get(),
                submitAnswerUseCase = get(),
                saveQuizSessionUseCase = get(),
            )
        }
    }
