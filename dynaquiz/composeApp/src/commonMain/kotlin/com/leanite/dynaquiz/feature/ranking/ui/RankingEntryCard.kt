package com.leanite.dynaquiz.feature.ranking.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.leanite.dynaquiz.core.domain.model.ChallengeMode
import com.leanite.dynaquiz.core.domain.model.RankingEntry
import com.leanite.dynaquiz.feature.ranking.res.RankingRes
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Instant

@Composable
fun RankingEntryCard(
    position: Int,
    entry: RankingEntry,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
                shape = RoundedCornerShape(14.dp),
            )
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        PositionBadge(position = position)

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = entry.playerName,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = "${entry.challengeMode.label()} · ${stringResource(RankingRes.CorrectFormat, entry.correctAnswers, entry.totalQuestions)} · ${formatFinishedAt(entry.finishedAt)}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            )
        }

        Text(
            text = stringResource(RankingRes.PointsFormat, entry.score.points),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.secondary,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
private fun PositionBadge(position: Int) {
    val bg = when (position) {
        1 -> Color(0xFFFFD700)        // ouro
        2 -> Color(0xFFC0C0C0)        // prata
        3 -> Color(0xFFCD7F32)        // bronze
        else -> MaterialTheme.colorScheme.primary
    }
    Box(
        modifier = Modifier
            .size(36.dp)
            .background(bg, CircleShape),
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

@Composable
private fun ChallengeMode.label(): String = stringResource(
    when (this) {
        ChallengeMode.Relaxed -> RankingRes.ModeRelaxed
        ChallengeMode.Timed.Easy -> RankingRes.ModeEasy
        ChallengeMode.Timed.Medium -> RankingRes.ModeMedium
        ChallengeMode.Timed.Hard -> RankingRes.ModeHard
    }
)

private fun formatFinishedAt(instant: Instant): String {
    val dt = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    val dd = dt.day.toString().padStart(2, '0')
    val mm = dt.month.toString().padStart(2, '0')
    val hh = dt.hour.toString().padStart(2, '0')
    val mi = dt.minute.toString().padStart(2, '0')
    return "$dd/$mm $hh:$mi"
}