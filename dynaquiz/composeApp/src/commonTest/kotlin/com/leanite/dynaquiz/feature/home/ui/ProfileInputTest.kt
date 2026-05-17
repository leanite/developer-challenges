package com.leanite.dynaquiz.feature.home.ui

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import com.leanite.dynaquiz.core.ui.theme.DynaquizTheme
import com.leanite.dynaquiz.feature.home.HomeValidation
import com.leanite.dynaquiz.uitest.UiTest
import com.leanite.dynaquiz.uitest.assertTextIsDisplayed
import com.leanite.dynaquiz.uitest.typeOnField
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalTestApi::class)
class ProfileInputTest : UiTest() {
    @Test
    fun `should render the nickname value in the input field`() =
        runComposeUiTest {
            setContent {
                DynaquizTheme {
                    ProfileInput(nickname = "Leandro", onNicknameChange = {})
                }
            }

            assertTextIsDisplayed("Leandro")
        }

    @Test
    fun `should render the NOME placeholder when nickname is empty`() =
        runComposeUiTest {
            setContent {
                DynaquizTheme {
                    ProfileInput(nickname = "", onNicknameChange = {})
                }
            }

            assertTextIsDisplayed("NOME")
        }

    @Test
    fun `should call onNicknameChange with the typed value`() =
        runComposeUiTest {
            val captured = mutableListOf<String>()
            setContent {
                DynaquizTheme {
                    ProfileInput(nickname = "", onNicknameChange = { captured += it })
                }
            }

            typeOnField("NOME", "Lea")

            assertEquals("Lea", captured.last())
        }

    @Test
    fun `should render character count when nickname is shorter than minimum`() =
        runComposeUiTest {
            setContent {
                DynaquizTheme {
                    ProfileInput(nickname = "Le", onNicknameChange = {})
                }
            }

            assertTextIsDisplayed("2/${HomeValidation.MAX_NICKNAME_LENGTH} caracteres")
        }

    @Test
    fun `should render character count reflecting current length when valid`() =
        runComposeUiTest {
            setContent {
                DynaquizTheme {
                    ProfileInput(nickname = "Leandro", onNicknameChange = {})
                }
            }

            assertTextIsDisplayed("7/${HomeValidation.MAX_NICKNAME_LENGTH} caracteres")
        }
}
