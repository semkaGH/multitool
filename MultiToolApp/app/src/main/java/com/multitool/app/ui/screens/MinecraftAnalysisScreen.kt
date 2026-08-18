package com.multitool.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.multitool.app.data.MinecraftAnalyzerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MinecraftAnalysisScreen(onBack: () -> Unit) {
    val viewModel: MinecraftAnalyzerViewModel = viewModel()
    var selectedMode by remember { mutableStateOf(MinecraftAnalyzerViewModel.AnalysisMode.NORMAL) }
    
    val logText by viewModel.logText.collectAsState()
    val apiKey by viewModel.apiKey.collectAsState()
    val selectedProvider by viewModel.selectedProvider.collectAsState()
    val expanded by viewModel.expanded.collectAsState()
    val result by viewModel.result.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Анализ ошибок Minecraft") },
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
                .padding(16.dp)
        ) {
            // Mode Selection
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedMode == MinecraftAnalyzerViewModel.AnalysisMode.NORMAL,
                    onClick = { selectedMode = MinecraftAnalyzerViewModel.AnalysisMode.NORMAL },
                    label = { Text("Обычный режим") },
                    modifier = Modifier.weight(1f)
                )
                
                FilterChip(
                    selected = selectedMode == MinecraftAnalyzerViewModel.AnalysisMode.AI,
                    onClick = { selectedMode = MinecraftAnalyzerViewModel.AnalysisMode.AI },
                    label = { Text("ИИ Анализ") },
                    modifier = Modifier.weight(1f)
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Log Input
            OutlinedTextField(
                value = logText,
                onValueChange = { viewModel.updateLogText(it) },
                label = { Text("Вставьте лог ошибки сюда") },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                minLines = 10,
                maxLines = 15
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // AI Settings (only visible in AI mode)
            if (selectedMode == MinecraftAnalyzerViewModel.AnalysisMode.AI) {
                OutlinedTextField(
                    value = apiKey,
                    onValueChange = { viewModel.updateApiKey(it) },
                    label = { Text("API Ключ (Gemini/OpenRouter)") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { viewModel.updateExpanded(it) }
                ) {
                    OutlinedTextField(
                        value = selectedProvider,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Провайдер ИИ") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { viewModel.updateExpanded(false) }
                    ) {
                        listOf("Gemini", "OpenRouter").forEach { provider ->
                            DropdownMenuItem(
                                text = { Text(provider) },
                                onClick = {
                                    viewModel.updateSelectedProvider(provider)
                                    viewModel.updateExpanded(false)
                                }
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            // Analyze Button
            Button(
                onClick = { viewModel.analyzeLog(selectedMode) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = logText.isNotBlank() && 
                         (selectedMode == MinecraftAnalyzerViewModel.AnalysisMode.NORMAL || apiKey.isNotBlank())
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Анализ...")
                } else {
                    Text("Анализировать")
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Result Display
            if (result.isNotBlank()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Результат анализа:",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = result,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}
