package com.leanite.dynaquiz.feature.home.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import com.leanite.dynaquiz.feature.home.HomeValidation
import com.leanite.dynaquiz.uitest.UiTest
import com.leanite.dynaquiz.uitest.assertTextIsDisplayed
import com.leanite.dynaquiz.uitest.typeOnField
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalTestApi::class)
class ProfileInputTest : UiTest() {
    @Composable
    private fun ProfileInputContent(
        nickname: String,
        onNicknameChange: (String) -> Unit = {},
    ) {
        ProfileInput(nickname = nickname, onNicknameChange = onNicknameChange)
    }

    @Test
    fun `should render the nickname value in the input field`() =
        runComposeUiTest {
            setContent {
                ProfileInputContent(nickname = "Leandro")
            }

            assertTextIsDisplayed("Leandro")
        }

    @Test
    fun `should render the NOME placeholder when nickname is empty`() =
        runComposeUiTest {
            setContent {
                ProfileInputContent(nickname = "")
            }

            assertTextIsDisplayed("NOME")
        }

    @Test
    fun `should call onNicknameChange with the typed value`() =
        runComposeUiTest {
            val captured = mutableListOf<String>()
            setContent {
                ProfileInputContent(nickname = "", onNicknameChange = { captured += it })
            }

            typeOnField("NOME", "Lea")

            assertEquals("Lea", captured.last())
        }

    @Test
    fun `should render character count when nickname is shorter than minimum`() =
        runComposeUiTest {
            setContent {
                ProfileInputContent(nickname = "Le")
            }

            assertTextIsDisplayed("2/${HomeValidation.MAX_NICKNAME_LENGTH} caracteres")
        }

    @Test
    fun `should render character count reflecting current length when valid`() =
        runComposeUiTest {
            setContent {
                ProfileInputContent(nickname = "Leandro")
            }

            assertTextIsDisplayed("7/${HomeValidation.MAX_NICKNAME_LENGTH} caracteres")
        }
}
