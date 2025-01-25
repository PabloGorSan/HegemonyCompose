package com.example.hegemonycompose

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.hegemonycompose.ui.navigation.Navigator
import com.example.hegemonycompose.ui.theme.HegemonyComposeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HegemonyComposeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    Navigator()
                }
            }
        }
    }
}