package com.sttuffe.expensetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
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

    NavHost(
        navController = navController,
        startDestination = "list" //시작 화면
    ) {
        composable(route = "list") {
            ListScreen(
                onNavigateToAddScreen = {
                    navController.navigate("add")
                }
            )
        }

        composable(route = "add") {
            AddScreen(
                onNavigateToListScreen = {
                    navController.navigate("list")
                }
            )
        }
    }

}