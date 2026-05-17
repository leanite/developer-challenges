package com.leanite.dynaquiz.core.ext

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun StringResource.usableString(): String = stringResource(this)
