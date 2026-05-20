package com.leanite.dynaquiz.feature.ranking.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leanite.dynaquiz.core.domain.model.ChallengeMode
import com.leanite.dynaquiz.core.domain.model.QuizPerformance
import com.leanite.dynaquiz.core.domain.model.QuizSetup
import com.leanite.dynaquiz.core.domain.model.RankingEntry
import com.leanite.dynaquiz.core.domain.model.Score
import com.leanite.dynaquiz.core.ui.common.label
import com.leanite.dynaquiz.core.ui.theme.DynamoxLightPurple
import com.leanite.dynaquiz.core.ui.theme.DynaquizTheme
import com.leanite.dynaquiz.feature.ranking.res.RankingRes
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Instant

@Composable
fun RankingEntryCard(
    position: Int,
    entry: RankingEntry,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
                    shape = RoundedCornerShape(14.dp),
                ).padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        PositionBadge(position = position)

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = entry.setup.playerName,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text =
                    "${entry.setup.challengeMode.label()} · " +
                        "${stringResource(
                            RankingRes.CorrectFormat,
                            entry.performance.correctAnswers,
                            entry.performance.totalQuestions,
                        )} · " +
                        formatFinishedAt(entry.finishedAt),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            )
        }

        Text(
            text = stringResource(RankingRes.PointsFormat, entry.performance.score.points),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.secondary,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
private fun PositionBadge(position: Int) {
    val imageResource =
        when (position) {
            1 -> RankingRes.Drawable.Top1
            2 -> RankingRes.Drawable.Top2
            3 -> RankingRes.Drawable.Top3
            else -> null
        }

    if (imageResource != null) {
        Image(
            painter = painterResource(imageResource),
            contentDescription = null,
            modifier = Modifier.width(36.dp),
        )
    } else {
        Box(
            modifier =
                Modifier
                    .size(36.dp)
                    .background(DynamoxLightPurple, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = position.toString(),
                style = MaterialTheme.typography.titleSmall,
                color = Color.White,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

private fun formatFinishedAt(instant: Instant): String {
    val dt = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    val dd = dt.day.toString().padStart(2, '0')
    val mm =
        dt.month.number
            .toString()
            .padStart(2, '0')
    val hh = dt.hour.toString().padStart(2, '0')
    val mi = dt.minute.toString().padStart(2, '0')
    return "$dd/$mm $hh:$mi"
}

@Preview
@Composable
private fun RankingEntryCardTopPreview() {
    DynaquizTheme {
        RankingEntryCard(
            position = 1,
            entry =
                RankingEntry(
                    setup =
                        QuizSetup(
                            playerName = "Leandro",
                            challengeMode = ChallengeMode.Timed.Hard,
                        ),
                    performance =
                        QuizPerformance(
                            score = Score(420),
                            correctAnswers = 9,
                            totalQuestions = 10,
                        ),
                    finishedAt = Instant.fromEpochSeconds(1_700_000_000),
                ),
        )
    }
}

@Preview
@Composable
private fun RankingEntryCardRegularPreview() {
    DynaquizTheme {
        RankingEntryCard(
            position = 12,
            entry =
                RankingEntry(
                    setup =
                        QuizSetup(
                            playerName = "Player",
                            challengeMode = ChallengeMode.Timed.Easy,
                        ),
                    performance =
                        QuizPerformance(
                            score = Score(80),
                            correctAnswers = 6,
                            totalQuestions = 10,
                        ),
                    finishedAt = Instant.fromEpochSeconds(1_700_000_000),
                ),
        )
    }
}
