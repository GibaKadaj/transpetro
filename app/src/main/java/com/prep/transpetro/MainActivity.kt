package com.prep.transpetro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.prep.transpetro.ui.navigation.AppNavGraph
import com.prep.transpetro.ui.theme.TranspetroTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TranspetroTheme {
                AppNavGraph()
            }
        }
    }
}
