package com.renatojobal.kmptemplate

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

fun main() {
    initApp()
    application {
        val windowState: WindowState = rememberWindowState(
            size = DpSize(width = 1100.dp, height = 740.dp),
        )
        Window(
            onCloseRequest = ::exitApplication,
            title = "KMP App Template",
            state = windowState,
        ) {
            App()
        }
    }
}
