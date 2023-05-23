package com.example.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.currentComposer
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.weatherapp.ui.theme.WeatherAppTheme

sealed class Screen(val route: String){
    object Main : Screen("main")
    object SecondPage : Screen("second_page")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            WeatherAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(navController = navController, startDestination = Screen.Main.route ){
                        composable(Screen.Main.route){
                            MainScreen(navController)
                        }
                        composable(Screen.SecondPage.route){
                            SecondScreen(navController)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MainScreen(navController: NavController){
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Greeting("Android")
        Button(onClick = { navController.navigate((Screen.SecondPage.route)) },
        modifier = Modifier.size(width = 200.dp, height = 60.dp)) {
            Text(text = "BUTTON PRESS IT")
        }
    }
}

@Composable
fun SecondScreen (navController: NavController){
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Text(text = "made it to second page", modifier = Modifier.clickable { navController.popBackStack() })
    }
}
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}
