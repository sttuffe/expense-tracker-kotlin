package com.sttuffe.expensetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ExpenseTrackerApp()
        }
    }
}

@Composable
fun ExpenseTrackerApp() {
    val navController = rememberNavController()
    val viewModel: TransactionViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "list" //시작 화면
    ) {
        composable(route = "list") {
            ListScreen(
                onNavigateToAddScreen = {
                    navController.navigate("add")
                },
                viewModel = viewModel
            )
        }

        composable(route = "add") {
            AddScreen(
                onNavigateToListScreen = {
                    navController.popBackStack()
                },
                viewModel = viewModel
            )
        }
    }

}