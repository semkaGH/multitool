package com.multitool.app.ui.screens

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.multitool.app.data.YouTubeDownloaderViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YouTubeDownloaderScreen(onBack: () -> Unit) {
    val viewModel: YouTubeDownloaderViewModel = viewModel()
    val context = LocalContext.current
    
    // Permission launcher for Android 13+
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Permission granted, can proceed with download
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Загрузка с YouTube") },
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
            // URL Input
            OutlinedTextField(
                value = viewModel.videoUrl,
                onValueChange = { viewModel.updateVideoUrl(it) },
                label = { Text("Вставьте ссылку на видео") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                trailingIcon = {
                    if (viewModel.videoUrl.isNotBlank()) {
                        IconButton(onClick = { viewModel.updateVideoUrl("") }) {
                            Icon(
                                androidx.compose.material.icons.Icons.Default.Clear,
                                contentDescription = "Очистить"
                            )
                        }
                    }
                }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Fetch Info Button
            Button(
                onClick = { viewModel.fetchVideoInfo() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = viewModel.videoUrl.isNotBlank() && !viewModel.isLoading
            ) {
                if (viewModel.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Загрузка...")
                } else {
                    Text("Получить информацию")
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Video Info Display
            if (viewModel.videoTitle.isNotBlank()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = viewModel.videoTitle,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        
                        // Format Selection
                        Text(
                            text = "Формат:",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            FilterChip(
                                selected = viewModel.selectedFormat == "mp4",
                                onClick = { viewModel.updateSelectedFormat("mp4") },
                                label = { Text("MP4 (Видео)") },
                                modifier = Modifier.weight(1f)
                            )
                            
                            FilterChip(
                                selected = viewModel.selectedFormat == "mp3",
                                onClick = { viewModel.updateSelectedFormat("mp3") },
                                label = { Text("MP3 (Аудио)") },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Quality Selection
                        Text(
                            text = "Качество:",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        
                        ExposedDropdownMenuBox(
                            expanded = viewModel.qualityExpanded,
                            onExpandedChange = { viewModel.updateQualityExpanded(it) }
                        ) {
                            OutlinedTextField(
                                value = viewModel.selectedQuality,
                                onValueChange = {},
                                readOnly = true,
                                trailingIcon = { 
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = viewModel.qualityExpanded) 
                                },
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth()
                            )
                            
                            ExposedDropdownMenu(
                                expanded = viewModel.qualityExpanded,
                                onDismissRequest = { viewModel.updateQualityExpanded(false) }
                            ) {
                                viewModel.availableQualities.forEach { quality ->
                                    DropdownMenuItem(
                                        text = { Text(quality) },
                                        onClick = {
                                            viewModel.updateSelectedQuality(quality)
                                            viewModel.updateQualityExpanded(false)
                                        }
                                    )
                                }
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Download Button
                        Button(
                            onClick = {
                                // Request permission if needed
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                }
                                viewModel.downloadVideo(context)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            enabled = !viewModel.isDownloading
                        ) {
                            if (viewModel.isDownloading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Загрузка… ${viewModel.downloadProgress}%")
                            } else {
                                Text("Скачать")
                            }
                        }
                        
                        // Progress indicator
                        if (viewModel.isDownloading) {
                            Spacer(modifier = Modifier.height(8.dp))
                            LinearProgressIndicator(
                                progress = viewModel.downloadProgress / 100f,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
            
            // Error message
            if (viewModel.errorMessage.isNotBlank()) {
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = viewModel.errorMessage,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}
