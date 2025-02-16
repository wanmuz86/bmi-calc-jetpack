package com.muzaffar.bmicalculator


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialogDefaults.containerColor
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable

import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.muzaffar.bmicalculator.ui.theme.BMICalculatorTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BMICalculatorScreen()
        }

    }
}

@Composable
fun BMICalculatorScreen (){
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    @OptIn(ExperimentalMaterial3Api::class)
    BMICalculatorTheme {
        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(title = { Text(text = "BMI Calculator")}   )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    // Show snackbar in another suspend (async-await)
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("Welcome to my app")
                    }
                }) {
                    Icon(painter =painterResource(R.drawable.ic_launcher_foreground),
                        contentDescription = "About me")
                }
            }
        ) { padding->
            BMICalculator(modifier = Modifier.padding(padding))
        }
    }
}




@Composable
fun BMICalculator(modifier: Modifier = Modifier){

    var weight by remember { mutableFloatStateOf(70f) }
    var height by remember { mutableFloatStateOf(170f) }

    val bmi = weight / (height/100 * height/100) // weight (kg) / (height in m)^2
    val bmiCategory = calculateBMI(bmi)


    Column(modifier = modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally){
        Text(text = "BMI Calculator",  fontSize = 32.sp, color = Color.Blue)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "We care about you health", fontSize = 24.sp, color = Color.Red)
        Spacer(modifier = Modifier.height(16.dp))
        Image(painter = painterResource(id = R.drawable.bmi), contentDescription = "BMI",)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Height : ${height.toInt()} cm")
        Slider(
            value = height,
            onValueChange = { height = it},
            valueRange = 100f..250f,
            modifier = Modifier.padding(16.dp)
        )
        Text(text = "Weight : ${weight.toInt()} kg ")
        Slider(
            value = weight,
            onValueChange = { weight = it},
            valueRange = 50f..200f,
            modifier = Modifier.padding(16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Card(
            // inside material library
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)),
            modifier = Modifier.padding(16.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ){
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                Text(text = "Your BMI is ${String.format("%.2f", bmi)}",
                    fontSize = 24.sp)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = bmiCategory, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

fun calculateBMI(bmi: Float): String {
    return when {
        bmi < 18.5 -> "Underweight"
        bmi < 25 -> "Normal weight"
        bmi < 30 -> "Overweight"
        else -> "Obese"
    }
}