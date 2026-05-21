package com.leanite.dynaquiz.feature.quiz.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import com.leanite.dynaquiz.core.domain.model.Question
import com.leanite.dynaquiz.core.domain.model.QuestionId
import com.leanite.dynaquiz.uitest.UiTest
import com.leanite.dynaquiz.uitest.assertTextIsDisplayed
import com.leanite.dynaquiz.uitest.assertTextIsNotEnabled
import com.leanite.dynaquiz.uitest.clickOnText
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalTestApi::class)
class QuestionCardTest : UiTest() {
    @Composable
    private fun QuestionCardContent(
        question: Question = sampleQuestion,
        selectedAnswer: String? = null,
        isSubmitting: Boolean = false,
        onOptionSelected: (String) -> Unit = {},
    ) {
        QuestionCard(
            question = question,
            selectedAnswer = selectedAnswer,
            isSubmitting = isSubmitting,
            onOptionSelected = onOptionSelected,
        )
    }

    private val sampleQuestion =
        Question(
            id = QuestionId("q-1"),
            statement = "Qual a capital do Brasil?",
            options = listOf("São Paulo", "Brasília", "Rio de Janeiro", "Salvador"),
        )

    @Test
    fun `should render the question statement`() =
        runComposeUiTest {
            setContent {
                QuestionCardContent()
            }

            assertTextIsDisplayed("Qual a capital do Brasil?")
        }

    @Test
    fun `should render one OptionButton per option in the question`() =
        runComposeUiTest {
            setContent {
                QuestionCardContent()
            }

            sampleQuestion.options.forEach { assertTextIsDisplayed(it) }
        }

    @Test
    fun `should call onOptionSelected with the tapped option text`() =
        runComposeUiTest {
            val captured = mutableListOf<String>()
            setContent {
                QuestionCardContent(
                    onOptionSelected = { captured += it },
                )
            }

            clickOnText("Brasília")

            assertEquals(listOf("Brasília"), captured)
        }

    @Test
    fun `should disable all options once selectedAnswer is not null`() =
        runComposeUiTest {
            setContent {
                QuestionCardContent(
                    selectedAnswer = "Brasília",
                )
            }

            sampleQuestion.options.forEach { assertTextIsNotEnabled(it) }
        }

    @Test
    fun `should disable all options while isSubmitting is true`() =
        runComposeUiTest {
            setContent {
                QuestionCardContent(
                    isSubmitting = true,
                )
            }

            sampleQuestion.options.forEach { assertTextIsNotEnabled(it) }
        }
}
