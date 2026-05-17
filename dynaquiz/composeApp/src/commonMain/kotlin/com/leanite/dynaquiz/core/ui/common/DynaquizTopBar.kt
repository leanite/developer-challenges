package com.leanite.dynaquiz.core.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.leanite.dynaquiz.core.ui.theme.DynaquizTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DynaquizTopBar(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    showNavigation: Boolean = false,
    onNavigationClick: () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
) {
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary),
    ) {
        CenterAlignedTopAppBar(
            title = title,
            navigationIcon = {
                if (showNavigation) BackButton(onClick = onNavigationClick)
            },
            actions = actions,
            colors =
                TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
        )
    }
}

@Preview
@Composable
private fun DynaquizTopBarPreview() {
    DynaquizTheme {
        DynaquizTopBar(
            title = { Text("Dynaquiz") },
            showNavigation = true,
        )
    }
}
