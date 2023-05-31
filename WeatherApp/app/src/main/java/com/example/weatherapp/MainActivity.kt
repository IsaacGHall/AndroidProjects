@file:OptIn(ExperimentalMaterial3Api::class)

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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

            WeatherAppTheme {

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //makes nav host
                    NavHost(navController = navController, startDestination = Screen.Main.route) {
                        composable(Screen.Main.route) {
                            MainScreen(navController, user, weatherViewModel)
                        }
                        composable(Screen.SecondPage.route) {
                            SecondScreen(navController) //this will use weatherViewModel later
                        }
                    }
                }
            }
        }

    }

}



    @Composable
    fun MainScreen(
        navController: NavController,
        user: MainActivity.User,
        weatherViewModel: WeatherViewModel
    ) {
        //this is how the info from the API is brought to the Main Screen
        val temperature: Double? by weatherViewModel.temperature.observeAsState()
        val city: String? by weatherViewModel.cityName.observeAsState()
        val region: String? by weatherViewModel.regionName.observeAsState()
        val country: String? by weatherViewModel.countryName.observeAsState()
        val temperatureF: Double? by weatherViewModel.temperatureF.observeAsState()
        //Boolean to check if response from API, and if so, marked as true.
        val isDataAvail: Boolean by weatherViewModel.isDataAvail.observeAsState(false)

        //mutable, holds input
        var searchText by remember { mutableStateOf("") }
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    //display stuff to user here, this is basic and has no real formatting.
                    text = "Hello ${user.name}!",
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp)
                )
                Row(
                    modifier = Modifier.padding(top = 16.dp)
                ){
                    TextField(
                        value = searchText,
                        onValueChange = { searchText = it}, //implicit single param
                        placeholder = { Text("Enter Location")},
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = { weatherViewModel.fetchWeatherData(searchText) }, //search for name
                        modifier = Modifier
                            .padding(start = 8.dp)
                    ) {
                        Icon(Icons.Default.ArrowForward, contentDescription = null)
                    }
                }
                if(isDataAvail) { //if true then display info for user.
                    Text(
                        text = "Temperature in $city, $region, $country",
                        modifier = Modifier.padding(top = 16.dp)
                    )
                    Text(
                        text = "$temperature °C, $temperatureF °F",
                        modifier = Modifier.padding(top = 8.dp)
                    )
                } else {
                    Text(
                        text = "Search for your region.",
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                Button(
                    onClick = { navController.navigate("second_page") },
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .size(width = 200.dp, height = 60.dp)
                ) {
                    Text(text = "Open Second Page")
                }
            }

        }
    }


    @Composable
    fun SecondScreen(navController: NavController) {
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

    @Preview(showBackground = true)
    @Composable

//preview function (dummy functions)
    fun HomeScreenPreview() {
        val user = MainActivity.User("Example User", "exampleuser123@google.com")
        val weatherViewModel = WeatherViewModel()
        WeatherAppTheme {
            MainScreen(
                navController = rememberNavController(),
                user = user,
                weatherViewModel = weatherViewModel
            )
        }
    }

