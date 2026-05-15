package com.leanite.dynaquiz.feature.ranking.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.leanite.dynaquiz.feature.ranking.RankingTab
import com.leanite.dynaquiz.feature.ranking.res.RankingRes
import org.jetbrains.compose.resources.stringResource

@Composable
fun RankingTabBar(
    selectedTab: RankingTab,
    onTabSelected: (RankingTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    val tabs = listOf(RankingTab.All, RankingTab.Mine)
    val selectedIndex = tabs.indexOf(selectedTab)

    SecondaryTabRow(
        selectedTabIndex = selectedIndex,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        indicator = {
            TabRowDefaults.SecondaryIndicator(
                modifier = Modifier.tabIndicatorOffset(selectedIndex, matchContentSize = false),
                color = MaterialTheme.colorScheme.secondary,
            )
        },
        divider = {},
        modifier = modifier,
    ) {
        tabs.forEach { tab ->
            Tab(
                selected = selectedTab == tab,
                onClick = { onTabSelected(tab) },
                text = {
                    Text(
                        text = stringResource(
                            when (tab) {
                                RankingTab.All -> RankingRes.TabAll
                                RankingTab.Mine -> RankingRes.TabMine
                            }
                        ),
                    )
                },
            )
        }
    }
}