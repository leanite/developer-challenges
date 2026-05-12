package com.leanite.dynaquiz.feature.splash.anim

import kotlinx.coroutines.delay

internal object SplashAnimation {
    const val INITIAL_TEXT = "Dynamox"

    /**
     * Roda a sequência completa de typing:
     * 1. Hold pra ler "Dynamox"
     * 2. Apaga "m, o, x" -> "Dyna"
     * 3. Pausa
     * 4. Escreve "q, u, i, z" -> "Dynaquiz"
     * 5. Hold final
     */
    suspend fun runTypingSequence(
        onTextChanged: (String) -> Unit,
    ) {
        delay(HOLD_INITIAL_MS)

        ERASE_SEQUENCE.forEach { text ->
            delay(LETTER_DELAY_MS)
            onTextChanged(text)
        }

        delay(PAUSE_BETWEEN_MS)

        TYPE_SEQUENCE.forEach { text ->
            delay(LETTER_DELAY_MS)
            onTextChanged(text)
        }

        delay(HOLD_FINAL_MS)
    }

    private val ERASE_SEQUENCE = listOf("Dynamo", "Dynam", "Dyna")
    private val TYPE_SEQUENCE = listOf("Dynaq", "Dynaqu", "Dynaqui", "Dynaquiz")

    private const val HOLD_INITIAL_MS = 400L
    private const val LETTER_DELAY_MS = 180L
    private const val PAUSE_BETWEEN_MS = 500L
    private const val HOLD_FINAL_MS = 440L
}