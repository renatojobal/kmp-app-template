package com.renatojobal.kmptemplate.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.renatojobal.kmptemplate.features.todo.ui.screen.TodoListScreenRoot

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = TodoList,
    ) {
        composable<TodoList> {
            TodoListScreenRoot()
        }
    }
}
