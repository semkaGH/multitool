package com.multitool.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.multitool.app.data.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(onBack: () -> Unit) {
    val viewModel = remember { SettingsViewModel() }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Настройки") },
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
        ) {
            // Auto Update Setting
            SettingsSwitchItem(
                icon = Icons.Default.Update,
                title = "Автообновление",
                subtitle = "Автоматически проверять обновления",
                checked = viewModel.autoUpdateEnabled,
                onCheckedChange = { viewModel.autoUpdateEnabled = it }
            )
            
            HorizontalDivider()
            
            // Dark Mode Setting
            SettingsSwitchItem(
                icon = Icons.Default.DarkMode,
                title = "Тёмная тема",
                subtitle = "Использовать тёмную тему оформления",
                checked = viewModel.darkModeEnabled,
                onCheckedChange = { viewModel.darkModeEnabled = it }
            )
            
            HorizontalDivider()
            
            // Language Setting
            SettingsListItem(
                icon = Icons.Default.Language,
                title = "Язык",
                subtitle = viewModel.selectedLanguage,
                onClick = { /* Show language selector */ }
            )
            
            HorizontalDivider()
            
            // Download Path Setting
            SettingsListItem(
                icon = Icons.Default.Folder,
                title = "Путь загрузок",
                subtitle = "/storage/emulated/0/Download/MultiTool",
                onClick = { /* Show folder picker */ }
            )
            
            HorizontalDivider()
            
            // Clear Cache Setting
            SettingsListItem(
                icon = Icons.Default.DeleteSweep,
                title = "Очистить кэш",
                subtitle = "Удалить временные файлы",
                onClick = { viewModel.clearCache() }
            )
            
            HorizontalDivider()
            
            // API Key Settings (for Minecraft AI analysis)
            SettingsListItem(
                icon = Icons.Default.VpnKey,
                title = "API Ключ ИИ",
                subtitle = "Настроить ключ для Gemini/OpenRouter",
                onClick = { /* Show API key dialog */ }
            )
            
            HorizontalDivider()
            
            // Notification Settings
            SettingsSwitchItem(
                icon = Icons.Default.Notifications,
                title = "Уведомления",
                subtitle = "Показывать уведомления о загрузках",
                checked = viewModel.notificationsEnabled,
                onCheckedChange = { viewModel.notificationsEnabled = it }
            )
        }
    }
}

@Composable
fun SettingsSwitchItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
fun SettingsListItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        IconButton(onClick = onClick) {
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Открыть"
            )
        }
    }
}

class SettingsViewModel {
    var autoUpdateEnabled by mutableStateOf(true)
    var darkModeEnabled by mutableStateOf(false)
    var selectedLanguage by mutableStateOf("Русский")
    var notificationsEnabled by mutableStateOf(true)
    
    fun clearCache() {
        // Implement cache clearing logic
    }
}
