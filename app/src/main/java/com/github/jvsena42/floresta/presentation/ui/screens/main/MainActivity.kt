package com.github.jvsena42.floresta.presentation.ui.screens.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.jvsena42.floresta.presentation.ui.screens.home.ScreenHome
import com.github.jvsena42.floresta.presentation.ui.screens.node.ScreenNode
import com.github.jvsena42.floresta.presentation.ui.screens.receive.ScreenReceive
import com.github.jvsena42.floresta.presentation.ui.theme.FlorestaTheme
import org.koin.androidx.compose.KoinAndroidContext
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var navigationSelectedItem by remember { mutableStateOf(Destinations.HOME) }
            val navController = rememberNavController()
            val mainViewmodel: MainViewmodel = koinViewModel()

            FlorestaTheme {
                KoinAndroidContext {
                    Scaffold(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
                        bottomBar = {
                            NavigationBar(
                                modifier = Modifier.clip(
                                    shape = CircleShape.copy(
                                        bottomStart = CornerSize(0.dp), bottomEnd = CornerSize(0.dp),
                                    )
                                )
                            ) {
                                Destinations.entries.forEach { destination ->
                                    NavigationBarItem(
                                        selected = destination == navigationSelectedItem,
                                        onClick = {
                                            navigationSelectedItem = destination
                                            navController.navigate(destination.route) {
                                                popUpTo(navController.graph.findStartDestination().id) {
                                                    saveState = true
                                                }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        },
                                        label = {
                                            Text(destination.label)
                                        },
                                        icon = {
                                            Icon(
                                                painter = painterResource(destination.icon),
                                                contentDescription = destination.label
                                            )
                                        }
                                    )
                                }
                            }
                        }
                    ) { innerPadding ->
                        NavHost(
                            navController = navController,
                            startDestination = Destinations.HOME.route,
                            modifier = Modifier.padding(paddingValues = innerPadding)
                        ) {
                            composable(Destinations.HOME.route) {
                                ScreenHome()
                            }
                            composable(Destinations.NODE.route) {
                                ScreenNode()
                            }
                            composable(Destinations.RECEIVE.route) {
                                ScreenReceive()
                            }
                        }
                    }
                }
            }
        }
    }
}
