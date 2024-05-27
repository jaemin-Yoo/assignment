package com.jaemin.assignment.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jaemin.assignment.navigation.AssignmentBottomBar
import com.jaemin.assignment.navigation.BottomNavItem
import com.jaemin.assignment.ui.favorites.FavoritesScreen
import com.jaemin.assignment.ui.feed.FeedScreen

@Composable
fun AssignmentApp() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            AssignmentBottomBar(
                destinations = listOf(BottomNavItem.Feed, BottomNavItem.Favorites),
                navController = navController
            )
        }
    ) { padding ->
        AssignmentNavHost(
            navController = navController,
            modifier = Modifier.padding(padding)
        )
    }
}

@Composable
fun AssignmentNavHost(
    navController: NavHostController,
    modifier: Modifier
) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Feed.route,
        modifier = modifier
    ) {
        composable(BottomNavItem.Feed.route) {
            FeedScreen()
        }
        composable(BottomNavItem.Favorites.route) {
            FavoritesScreen()
        }
    }
}