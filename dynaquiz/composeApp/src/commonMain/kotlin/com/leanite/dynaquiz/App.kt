package com.leanite.dynaquiz

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.leanite.dynaquiz.core.ui.theme.DynaquizTheme
import com.leanite.dynaquiz.navigation.DynaquizAppNavigation

@Composable
@Preview
fun App() {
    DynaquizTheme {
        DynaquizAppNavigation()
    }
}
