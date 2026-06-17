package com.renatojobal.kmptemplate.features.core.accessibility.data

import androidx.compose.runtime.Composable

@Composable
expect fun isReduceMotionEnabled(): Boolean

@Composable
expect fun PlatformBackHandler(enabled: Boolean, onBack: () -> Unit)
