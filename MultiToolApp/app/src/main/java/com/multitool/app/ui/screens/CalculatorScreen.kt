package com.multitool.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorScreen(onBack: () -> Unit) {
    var expression by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Калькулятор") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Display
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = expression,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = result,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Buttons
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CalculatorButton("C", onClick = { 
                        expression = ""
                        result = ""
                    }, modifier = Modifier.weight(1f))
                    CalculatorButton("⌫", onClick = { 
                        expression = expression.dropLast(1)
                    }, modifier = Modifier.weight(1f))
                    CalculatorButton("%", onClick = { 
                        expression += "%"
                    }, modifier = Modifier.weight(1f))
                    CalculatorButton("÷", onClick = { 
                        expression += "/"
                    }, modifier = Modifier.weight(1f))
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CalculatorButton("7", onClick = { expression += "7" }, modifier = Modifier.weight(1f))
                    CalculatorButton("8", onClick = { expression += "8" }, modifier = Modifier.weight(1f))
                    CalculatorButton("9", onClick = { expression += "9" }, modifier = Modifier.weight(1f))
                    CalculatorButton("×", onClick = { expression += "*" }, modifier = Modifier.weight(1f))
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CalculatorButton("4", onClick = { expression += "4" }, modifier = Modifier.weight(1f))
                    CalculatorButton("5", onClick = { expression += "5" }, modifier = Modifier.weight(1f))
                    CalculatorButton("6", onClick = { expression += "6" }, modifier = Modifier.weight(1f))
                    CalculatorButton("-", onClick = { expression += "-" }, modifier = Modifier.weight(1f))
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CalculatorButton("1", onClick = { expression += "1" }, modifier = Modifier.weight(1f))
                    CalculatorButton("2", onClick = { expression += "2" }, modifier = Modifier.weight(1f))
                    CalculatorButton("3", onClick = { expression += "3" }, modifier = Modifier.weight(1f))
                    CalculatorButton("+", onClick = { expression += "+" }, modifier = Modifier.weight(1f))
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CalculatorButton("0", onClick = { expression += "0" }, modifier = Modifier.weight(1f))
                    CalculatorButton(".", onClick = { expression += "." }, modifier = Modifier.weight(1f))
                    CalculatorButton("=", onClick = {
                        try {
                            val evaluated = evaluateExpression(expression)
                            result = evaluated.toString()
                        } catch (e: Exception) {
                            result = "Ошибка"
                        }
                    }, modifier = Modifier.weight(2f))
                }
            }
        }
    }
}

@Composable
fun CalculatorButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(60.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

private fun evaluateExpression(expr: String): Double {
    // Simple expression evaluator
    return try {
        val sanitized = expr.replace("%", "/100.0")
        // Use Kotlin's scripting engine for evaluation
        val scriptEngine = javax.script.ScriptEngineManager().getEngineByName("JavaScript")
        scriptEngine?.eval(sanitized)?.toString()?.toDoubleOrNull() ?: 0.0
    } catch (e: Exception) {
        throw e
    }
}
