package com.multitool.app.data

import android.content.Context
import android.os.Environment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

class YouTubeDownloaderViewModel : ViewModel() {
    
    private val _videoUrl = MutableStateFlow("")
    val videoUrl: StateFlow<String> = _videoUrl.asStateFlow()
    
    private val _videoTitle = MutableStateFlow("")
    val videoTitle: StateFlow<String> = _videoTitle.asStateFlow()
    
    private val _selectedFormat = MutableStateFlow("mp4")
    val selectedFormat: StateFlow<String> = _selectedFormat.asStateFlow()
    
    private val _selectedQuality = MutableStateFlow("1080p")
    val selectedQuality: StateFlow<String> = _selectedQuality.asStateFlow()
    
    private val _availableQualities = MutableStateFlow(listOf("1080p", "720p", "480p", "360p"))
    val availableQualities: StateFlow<List<String>> = _availableQualities.asStateFlow()
    
    private val _qualityExpanded = MutableStateFlow(false)
    val qualityExpanded: StateFlow<Boolean> = _qualityExpanded.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _isDownloading = MutableStateFlow(false)
    val isDownloading: StateFlow<Boolean> = _isDownloading.asStateFlow()
    
    private val _downloadProgress = MutableStateFlow(0)
    val downloadProgress: StateFlow<Int> = _downloadProgress.asStateFlow()
    
    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage.asStateFlow()
    
    fun updateVideoUrl(url: String) {
        _videoUrl.value = url
        _errorMessage.value = ""
    }
    
    fun updateSelectedFormat(format: String) {
        _selectedFormat.value = format
        // Update available qualities based on format
        _availableQualities.value = if (format == "mp3") {
            listOf("128kbps", "192kbps", "256kbps", "320kbps")
        } else {
            listOf("1080p", "720p", "480p", "360p")
        }
        _selectedQuality.value = _availableQualities.value.first()
    }
    
    fun updateSelectedQuality(quality: String) {
        _selectedQuality.value = quality
    }
    
    fun updateQualityExpanded(expanded: Boolean) {
        _qualityExpanded.value = expanded
    }
    
    fun fetchVideoInfo() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = ""
            
            try {
                // Simulate fetching video info
                // In a real implementation, this would use NewPipe Extractor or YouTube API
                delay(1500)
                
                // Extract video ID from URL (simplified)
                val videoId = extractVideoId(_videoUrl.value)
                if (videoId != null) {
                    _videoTitle.value = "Demo Video Title - Sample YouTube Video ($videoId)"
                } else {
                    _errorMessage.value = "Неверная ссылка на YouTube"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Ошибка получения информации: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun downloadVideo(context: Context) {
        viewModelScope.launch {
            _isDownloading.value = true
            _downloadProgress.value = 0
            
            try {
                // Simulate download process
                // In a real implementation, this would download the actual video
                val downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                val fileName = "${_videoTitle.value.substring(0..50).replace(Regex("[^a-zA-Z0-9]"), "_")}.${_selectedFormat.value}"
                val file = File(downloadDir, "MultiTool/$fileName")
                file.parentFile?.mkdirs()
                
                // Simulate download progress
                for (i in 0..100 step 5) {
                    delay(200)
                    _downloadProgress.value = i
                }
                
                // Create dummy file to simulate download
                FileOutputStream(file).use { fos ->
                    fos.write("Demo content - This is a placeholder".toByteArray())
                }
                
                _errorMessage.value = ""
            } catch (e: Exception) {
                _errorMessage.value = "Ошибка загрузки: ${e.message}"
            } finally {
                _isDownloading.value = false
                if (_downloadProgress.value >= 100) {
                    delay(1000)
                    _downloadProgress.value = 0
                }
            }
        }
    }
    
    private fun extractVideoId(url: String): String? {
        // Simple extraction - supports common YouTube URL formats
        val patterns = listOf(
            Regex("(?:v=|/)([a-zA-Z0-9_-]{11})(?:[&?]|$)"),
            Regex("youtu\\.be/([a-zA-Z0-9_-]{11})"),
            Regex("youtube\\.com/embed/([a-zA-Z0-9_-]{11})")
        )
        
        for (pattern in patterns) {
            val match = pattern.find(url)
            if (match != null) {
                return match.groupValues[1]
            }
        }
        return null
    }
}
