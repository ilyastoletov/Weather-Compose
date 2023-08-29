package com.appninjas.composeweather_2.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.appninjas.composeweather_2.R
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.appninjas.composeweather_2.presentation.model.BottomNavItem
import com.appninjas.composeweather_2.presentation.screens.crypto.view.CurrencyScreen
import com.appninjas.composeweather_2.presentation.screens.weather.view.WeatherScreen
import com.appninjas.composeweather_2.presentation.theme.ComposeWeatherTheme
import com.appninjas.composeweather_2.presentation.theme.Grey
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeWeatherTheme {
                val navController = rememberNavController()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(bottomBar = { BottomNavigationBar(navController = navController) }) {padding ->
                        NavigationHost(navController = navController, padding = padding)
                    }
                }
            }
        }
    }
}

@Composable
fun NavigationHost(navController: NavHostController, padding: PaddingValues) {

    NavHost(modifier = Modifier.padding(paddingValues = padding), navController = navController, startDestination = "weather") {
        composable("weather") { WeatherScreen() }
        composable("crypto") { CurrencyScreen() }
    }

}

@Composable
fun BottomNavigationBar(navController: NavHostController) {

    BottomNavigation(backgroundColor = Color.White, contentColor = Grey) {
        val navBackStackEntry = navController.currentBackStackEntryAsState()
        val navBarItems = prepareBottomNavItems()
        val currentRoute = navBackStackEntry.value?.destination?.route
        
        navBarItems.forEachIndexed { index, item ->
            BottomNavigationItem(
                selected = currentRoute == item.route,
                onClick = {
                          navController.navigate(item.route)
                },
                icon = {
                    Icon(
                        painter = if (index == 0) painterResource(id = R.drawable.baseline_cloud_24) else painterResource(id = R.drawable.baseline_attach_money_24),
                        contentDescription = "${item.name} icon"
                    )
                },
                label = { Text(text = item.name) },
                alwaysShowLabel = false
            )
        }
    }
}

fun prepareBottomNavItems(): List<BottomNavItem> {
    return listOf(
        BottomNavItem(name = "Погода", icon = Icons.Filled.Star, route = "weather"),
        BottomNavItem(name = "Крипта", icon = Icons.Filled.Phone, route = "crypto")
    )
}