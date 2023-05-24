package com.example.weatherapp

import android.accounts.AccountManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.weatherapp.ui.theme.WeatherAppTheme
import com.example.weatherapp.ui.theme.WeatherViewModel

sealed class Screen(val route: String){
    //more pages to come
    object Main : Screen("main")
    object SecondPage : Screen("second_page")
}

class MainActivity : ComponentActivity() {
    data class User(val name: String, val email: String) //save for android user info
    private val weatherViewModel: WeatherViewModel by viewModels() //ignore warning pretty sure
    private lateinit var user: User //use of a not null property outside of a constructor
    override fun onCreate(savedInstanceState: Bundle?) { //this is for getting google accounts
        super.onCreate(savedInstanceState)
        val accountManager = getSystemService(Context.ACCOUNT_SERVICE) as AccountManager
        val accounts = accountManager.getAccountsByType("com.google")
        if (accounts.isNotEmpty()) { //use first account
            val account = accounts[0]
            val userName = account.name ?: ""
            val userEmail = accountManager.getUserData(account, "email") ?: ""

            user = User(userName, userEmail) //Hello John Doe!
            Log.d("MainActivity", "User information: $user") //check logcat
        } else { //else Hello Guest!
            //no account, make a default user.
            user = User("Guest", "")
            Log.d("MainActivity", "User information: $user") //check logcat

        }

        setContent {
            val navController = rememberNavController()

            WeatherAppTheme{

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //makes nav host
                    NavHost(navController = navController, startDestination = Screen.Main.route ){
                        composable(Screen.Main.route){
                            MainScreen(navController, user, weatherViewModel)
                        }
                        composable(Screen.SecondPage.route){
                            SecondScreen(navController) //this will use weatherViewModel later
                        }
                    }
                }
            }
        }

    }
}

@Composable
fun MainScreen(navController: NavController, user:MainActivity.User, weatherViewModel: WeatherViewModel){
    //this is how the info from the API is brought to the Main Screen
    val temperature: Double? by weatherViewModel.temperature.observeAsState()
    val city: String? by weatherViewModel.cityName.observeAsState()
    val country: String? by weatherViewModel.countryName.observeAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column( modifier =  Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
            ){
            Text(
                //display stuff to user here, this is basic and has no real formatting.
                text = "Hello ${user.name}!\nTemperature in $city, $country: $temperature °C",
                modifier = Modifier.padding( start = 16.dp, top = 16.dp)
            )
            Button(onClick = { navController.navigate("second_page") },
                modifier = Modifier
                    .padding(top = 16.dp)
                    .size(width = 200.dp, height = 60.dp)
            ){
                Text(text = "Open Second Page")
            }
        }

    }
}


@Composable
fun SecondScreen (navController: NavController){
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Greeting("The Button Returns You")
            Button(
                onClick = { navController.navigate("main") },
                modifier = Modifier
                    .padding(top = 16.dp)
                    .size(width = 200.dp, height = 60.dp)
            ) {
                Text(text = "Open First Page")
            }
        }

    }
}
@Composable
//temp function to have a greeting, but it currently is used more than once bc im lazy
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview (showBackground = true)
@Composable

//preview function (dummy functions)
fun HomeScreenPreview() {
    val user = MainActivity.User("Example User", "exampleuser123@google.com")
    val weatherViewModel = WeatherViewModel()
    WeatherAppTheme {
        MainScreen(navController = rememberNavController(), user=user, weatherViewModel = weatherViewModel)
    }
}
