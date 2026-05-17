package com.leanite.dynaquiz.feature.ranking

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.leanite.dynaquiz.feature.ranking.res.RankingRes
import kotlinx.coroutines.flow.Flow
import org.jetbrains.compose.resources.getString

@Composable
fun RankingEventEffects(
    events: Flow<RankingEvent>,
    snackbarHostState: SnackbarHostState,
    onNavigateBack: () -> Unit,
) {
    LaunchedEffect(events) {
        events.collect { event ->
            when (event) {
                RankingEvent.NavigateBack -> onNavigateBack()
                is RankingEvent.ShowMessage -> {
                    val text =
                        when (event.type) {
                            RankingMessage.LoadFailed -> getString(RankingRes.MsgLoadFailed)
                        }
                    snackbarHostState.showSnackbar(text)
                }
            }
        }
    }
}
