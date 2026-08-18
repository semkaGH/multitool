package com.multitool.app.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class MinecraftAnalyzerViewModel : ViewModel() {
    
    enum class AnalysisMode { NORMAL, AI }
    
    private val _logText = MutableStateFlow("")
    val logText: StateFlow<String> = _logText.asStateFlow()
    
    private val _apiKey = MutableStateFlow("")
    val apiKey: StateFlow<String> = _apiKey.asStateFlow()
    
    private val _selectedProvider = MutableStateFlow("Gemini")
    val selectedProvider: StateFlow<String> = _selectedProvider.asStateFlow()
    
    private val _expanded = MutableStateFlow(false)
    val expanded: StateFlow<Boolean> = _expanded.asStateFlow()
    
    private val _result = MutableStateFlow("")
    val result: StateFlow<String> = _result.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    fun updateLogText(text: String) {
        _logText.value = text
    }
    
    fun updateApiKey(key: String) {
        _apiKey.value = key
    }
    
    fun updateSelectedProvider(provider: String) {
        _selectedProvider.value = provider
    }
    
    fun updateExpanded(expanded: Boolean) {
        _expanded.value = expanded
    }
    
    fun analyzeLog(mode: AnalysisMode) {
        viewModelScope.launch {
            _isLoading.value = true
            _result.value = ""
            
            try {
                if (mode == AnalysisMode.NORMAL) {
                    _result.value = analyzeWithNormalMode(_logText.value)
                } else {
                    _result.value = analyzeWithAI(_logText.value, _apiKey.value, _selectedProvider.value)
                }
            } catch (e: Exception) {
                _result.value = "Ошибка анализа: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    private suspend fun analyzeWithNormalMode(log: String): String {
        // Simulate analysis delay
        delay(1000)
        
        val sb = StringBuilder()
        sb.appendLine("=== Анализ лога Minecraft ===\n")
        
        // Check for common errors
        val errorPatterns = mapOf(
            Pattern.compile("OutOfMemoryError", Pattern.CASE_INSENSITIVE) to 
                "❌ Ошибка нехватки памяти (OOM)\n" +
                "Решение: Увеличьте выделенную память для Minecraft в лаунчере.\n" +
                "Рекомендуется: 4-8 ГБ RAM для модпаков.\n\n",
            
            Pattern.compile("NullPointerException", Pattern.CASE_INSENSITIVE) to 
                "❌ NullPointerException\n" +
                "Решение: Обычно вызвано проблемой с модом или повреждёнными данными.\n" +
                "Попробуйте удалить проблемные моды или мир.\n\n",
            
            Pattern.compile("StackOverflowError", Pattern.CASE_INSENSITIVE) to 
                "❌ StackOverflowError\n" +
                "Решение: Бесконечная рекурсия. Проверьте моды на совместимость.\n\n",
            
            Pattern.compile("NoSuchMethodError", Pattern.CASE_INSENSITIVE) to 
                "❌ NoSuchMethodError - Несовместимость модов\n" +
                "Решение: Обновите все моды до совместимых версий.\n\n",
            
            Pattern.compile("ClassNotFoundException", Pattern.CASE_INSENSITIVE) to 
                "❌ ClassNotFoundException - Отсутствует класс\n" +
                "Решение: Проверьте целостность файлов игры и модов.\n\n",
            
            Pattern.compile("java\\.lang\\.Exception", Pattern.CASE_INSENSITIVE) to 
                "⚠️ Общая ошибка Java\n" +
                "Требуется дополнительный анализ конкретного сообщения.\n\n",
            
            Pattern.compile("Mod\\s+([\\w]+)\\s+has failed", Pattern.CASE_INSENSITIVE) to 
                "❌ Мод не загрузился\n" +
                "Решение: Проверьте зависимости мода и совместимость версий.\n\n",
            
            Pattern.compile("FML", Pattern.CASE_INSENSITIVE) to 
                "⚠️ Ошибка Forge Mod Loader\n" +
                "Решение: Переустановите Forge и проверьте моды.\n\n"
        )
        
        var foundErrors = false
        for ((pattern, message) in errorPatterns) {
            if (pattern.matcher(log).find()) {
                sb.append(message)
                foundErrors = true
            }
        }
        
        if (!foundErrors) {
            sb.appendLine("✅ Явных ошибок не обнаружено")
            sb.appendLine("Возможно, лог содержит предупреждения или информационные сообщения.")
        }
        
        // Extract mod list if present
        val modPattern = Pattern.compile("FML\\s+found\\s+(\\d+)\\s+mods")
        val modMatcher = modPattern.matcher(log)
        if (modMatcher.find()) {
            sb.appendLine("\n📦 Найдено модов: ${modMatcher.group(1)}")
        }
        
        return sb.toString()
    }
    
    private suspend fun analyzeWithAI(log: String, apiKey: String, provider: String): String {
        // Simulate API call delay
        delay(2000)
        
        if (apiKey.isBlank()) {
            return "❌ Введите API ключ для ИИ анализа"
        }
        
        // In a real implementation, this would call the actual AI API
        // For now, return a simulated response
        return """
            ═══ ИИ Анализ лога Minecraft ═══
            
            Провайдер: $provider
            
            📊 Общий анализ:
            Лог содержит признаки критической ошибки, требующей внимания.
            
            🔍 Найденные проблемы:
            1. Критическая ошибка в процессе загрузки
            2. Возможный конфликт модов
            
            💡 Рекомендации:
            • Проверьте последние добавленные моды
            • Убедитесь в совместимости версий
            • Попробуйте запустить с минимальным набором модов
            • Проверьте выделенную память (RAM)
            
            ⚙️ Технические детали:
            Для получения полного анализа необходимо настроить API подключение.
            
            Примечание: Это демонстрационный ответ. 
            Для реального ИИ анализа подключите API Gemini или OpenRouter.
        """.trimIndent()
    }
}
