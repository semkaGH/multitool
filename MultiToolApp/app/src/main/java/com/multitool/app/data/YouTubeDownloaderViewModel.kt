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
    
    // Expose setters for Compose
    var videoUrl by _videoUrl
    var videoTitle by _videoTitle
    var selectedFormat by _selectedFormat
    var selectedQuality by _selectedQuality
    var qualityExpanded by _qualityExpanded
    var isLoading by _isLoading
    var isDownloading by _isDownloading
    var downloadProgress by _downloadProgress
    var errorMessage by _errorMessage
    var availableQualities by _availableQualities
    
    fun updateVideoUrl(url: String) {
        _videoUrl.value = url
        errorMessage = ""
    }
    
    fun updateSelectedFormat(format: String) {
        _selectedFormat.value = format
        // Update available qualities based on format
        _availableQualities.value = if (format == "mp3") {
            listOf("128kbps", "192kbps", "256kbps", "320kbps")
        } else {
            listOf("1080p", "720p", "480p", "360p")
        }
        _selectedQuality.value = availableQualities.first()
    }
    
    fun updateSelectedQuality(quality: String) {
        _selectedQuality.value = quality
    }
    
    fun updateQualityExpanded(expanded: Boolean) {
        _qualityExpanded.value = expanded
    }
    
    fun fetchVideoInfo() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = ""
            
            try {
                // Simulate fetching video info
                // In a real implementation, this would use NewPipe Extractor or YouTube API
                delay(1500)
                
                // Extract video ID from URL (simplified)
                val videoId = extractVideoId(videoUrl)
                if (videoId != null) {
                    videoTitle = "Demo Video Title - Sample YouTube Video ($videoId)"
                } else {
                    errorMessage = "Неверная ссылка на YouTube"
                }
            } catch (e: Exception) {
                errorMessage = "Ошибка получения информации: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
    
    fun downloadVideo(context: Context) {
        viewModelScope.launch {
            isDownloading = true
            downloadProgress = 0
            
            try {
                // Simulate download process
                // In a real implementation, this would download the actual video
                val downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                val fileName = "${videoTitle.substring(0..50).replace(Regex("[^a-zA-Z0-9]"), "_")}.${selectedFormat}"
                val file = File(downloadDir, "MultiTool/$fileName")
                file.parentFile?.mkdirs()
                
                // Simulate download progress
                for (i in 0..100 step 5) {
                    delay(200)
                    downloadProgress = i
                }
                
                // Create dummy file to simulate download
                FileOutputStream(file).use { fos ->
                    fos.write("Demo content - This is a placeholder".toByteArray())
                }
                
                errorMessage = ""
            } catch (e: Exception) {
                errorMessage = "Ошибка загрузки: ${e.message}"
            } finally {
                isDownloading = false
                if (downloadProgress >= 100) {
                    delay(1000)
                    downloadProgress = 0
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
