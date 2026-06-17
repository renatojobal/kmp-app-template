package com.renatojobal.kmptemplate.features.core.accessibility.data

import androidx.compose.runtime.Composable
import platform.UIKit.UIAccessibilityIsReduceMotionEnabled

@Composable
actual fun isReduceMotionEnabled(): Boolean {
    return UIAccessibilityIsReduceMotionEnabled()
}

@Composable
actual fun PlatformBackHandler(enabled: Boolean, onBack: () -> Unit) {
    // No-op on iOS — no system back button
}
