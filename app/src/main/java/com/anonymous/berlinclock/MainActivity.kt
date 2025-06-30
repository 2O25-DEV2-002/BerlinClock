package com.anonymous.berlinclock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.anonymous.berlinclock.presentation.berlinclock.navgraph.BerlinClockNavGraph
import com.anonymous.berlinclock.ui.theme.BerlinClockTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BerlinClockTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BerlinClockUI()
                }
            }
        }
    }
}

@Composable
fun BerlinClockUI() {
    BerlinClockNavGraph()
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BerlinClockTheme {
        BerlinClockUI()
    }
}