package com.prep.transpetro.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.prep.transpetro.ui.screens.blockcoverage.BlockCoverageScreen
import com.prep.transpetro.ui.screens.errorlog.ErrorLogDetailScreen
import com.prep.transpetro.ui.screens.errorlog.ErrorLogScreen
import com.prep.transpetro.ui.screens.formulas.FormulasScreen
import com.prep.transpetro.ui.screens.home.HomeScreen
import com.prep.transpetro.ui.screens.record.DailyRecordScreen
import com.prep.transpetro.ui.screens.settings.SettingsScreen
import com.prep.transpetro.ui.screens.simulado.SimuladoDetailScreen
import com.prep.transpetro.ui.screens.simulado.SimuladoScreen
import com.prep.transpetro.ui.screens.studyplan.StudyPlanScreen

private data class BottomNavItem(val screen: Screen, val label: String, val icon: @Composable () -> Unit)

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    val bottomItems = listOf(
        BottomNavItem(Screen.Home, "Home") { Icon(Icons.Default.Home, null) },
        BottomNavItem(Screen.DailyRecord, "Registro") { Icon(Icons.Default.EditNote, null) },
        BottomNavItem(Screen.Simulados, "Simulados") { Icon(Icons.Default.Quiz, null) },
        BottomNavItem(Screen.ErrorLog, "Erros") { Icon(Icons.Default.Book, null) },
        BottomNavItem(Screen.StudyPlan, "Plano") { Icon(Icons.Default.CalendarMonth, null) },
    )

    Scaffold(
        bottomBar = { BottomBar(navController, bottomItems) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    onNavigateToRecord = { navController.navigate(Screen.DailyRecord.route) },
                    onNavigateToSimulados = { navController.navigate(Screen.Simulados.route) },
                    onNavigateToBlocks = { navController.navigate(Screen.BlockCoverage.route) },
                    onNavigateToSettings = { navController.navigate(Screen.Settings.route) }
                )
            }
            composable(Screen.DailyRecord.route) {
                DailyRecordScreen()
            }
            composable(Screen.Simulados.route) {
                SimuladoScreen(
                    onNavigateToDetail = { id -> navController.navigate(Screen.SimuladoDetail.createRoute(id)) }
                )
            }
            composable(
                Screen.SimuladoDetail.route,
                arguments = listOf(navArgument("id") { type = NavType.LongType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getLong("id") ?: -1L
                SimuladoDetailScreen(
                    simuladoId = id,
                    onBack = { navController.popBackStack() }
                )
            }
            composable(Screen.ErrorLog.route) {
                ErrorLogScreen(
                    onNavigateToDetail = { id -> navController.navigate(Screen.ErrorLogDetail.createRoute(id)) },
                    onNavigateToNew = { navController.navigate(Screen.ErrorLogDetail.createRoute(-1L)) }
                )
            }
            composable(
                Screen.ErrorLogDetail.route,
                arguments = listOf(navArgument("id") { type = NavType.LongType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getLong("id") ?: -1L
                ErrorLogDetailScreen(
                    entryId = id,
                    onBack = { navController.popBackStack() }
                )
            }
            composable(Screen.BlockCoverage.route) {
                BlockCoverageScreen()
            }
            composable(Screen.StudyPlan.route) {
                StudyPlanScreen()
            }
            composable(Screen.Formulas.route) {
                FormulasScreen()
            }
            composable(Screen.Settings.route) {
                SettingsScreen()
            }
        }
    }
}

@Composable
private fun BottomBar(navController: NavHostController, items: List<BottomNavItem>) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar {
        items.forEach { item ->
            val selected = currentDestination?.hierarchy?.any { it.route == item.screen.route } == true
            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(item.screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = item.icon,
                label = { Text(item.label) }
            )
        }
    }
}
