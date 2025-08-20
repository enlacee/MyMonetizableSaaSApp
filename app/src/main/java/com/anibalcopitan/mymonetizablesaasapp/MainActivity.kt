package com.anibalcopitan.mymonetizablesaasapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.anibalcopitan.mymonetizablesaasapp.ui.theme.MyMonetizableSaaSAppTheme

class MainActivity : ComponentActivity() {

    companion object {

        /**
         * Here is the API SERVICE FOR LOCAL AND PRODUCTION
         */
        const val API_OKEYPAY: String =
            "https://script.google.com/macros/s/AKfycbwuzqUNuG9rD7wCkBteBJhETboxrKJ4HTlVGa68dQYfbw0vq_cW22QovLdVdayYzLd6tw/exec"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyMonetizableSaaSAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LoginScreen()
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyMonetizableSaaSAppTheme {
//        Greeting("Android")
        LoginScreen()
    }
}