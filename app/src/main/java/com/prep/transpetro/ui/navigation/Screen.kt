package com.prep.transpetro.ui.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object DailyRecord : Screen("daily_record")
    data object Simulados : Screen("simulados")
    data object SimuladoDetail : Screen("simulado/{id}") {
        fun createRoute(id: Long) = "simulado/$id"
    }
    data object ErrorLog : Screen("error_log")
    data object ErrorLogDetail : Screen("error_log/{id}") {
        fun createRoute(id: Long) = "error_log/$id"
    }
    data object BlockCoverage : Screen("block_coverage")
    data object StudyPlan : Screen("study_plan")
    data object Formulas : Screen("formulas")
    data object Settings : Screen("settings")
}
