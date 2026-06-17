package com.renatojobal.kmptemplate

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.renatojobal.kmptemplate.navigation.AppNavHost
import com.renatojobal.kmptemplate.ui.theme.AppTheme

@Composable
@Preview
fun App() {
    AppTheme {
        AppNavHost()
    }
}
