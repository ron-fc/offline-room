package com.nemesis.offlinefroom

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.nemesis.offlinefroom.ui.navigation.AppNavigation
import com.nemesis.offlinefroom.ui.theme.OfflineFRoomAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OfflineFRoomAppTheme {
                AppNavigation()
            }
        }
    }
}