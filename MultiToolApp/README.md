# MultiTool - Android Application

Многофункциональное приложение для Android с различными инструментами.

## 🚀 Возможности

- **🧮 Калькулятор** - Базовые математические вычисления
- **🐛 Анализ ошибок Minecraft** - Два режима:
  - Обычный режим - анализ логов по шаблонам
  - ИИ режим - анализ через Gemini/OpenRouter API
- **📥 Загрузка с YouTube** - Скачивание видео в MP4/MP3 с выбором качества (до 1080p)
- **⚙️ Настройки** - Персонализация приложения
- **ℹ️ О приложении** - Информация о версии и возможностях

## 🛠 Технологии

- **Язык**: Kotlin
- **UI**: Jetpack Compose
- **Дизайн**: Material Design 3
- **Архитектура**: MVVM
- **Сеть**: Retrofit + OkHttp
- **Асинхронность**: Kotlin Coroutines

## 📁 Структура проекта

```
app/src/main/java/com/multitool/app/
├── MainActivity.kt
├── data/
│   ├── MinecraftAnalyzerViewModel.kt
│   └── YouTubeDownloaderViewModel.kt
├── ui/
│   ├── navigation/
│   │   ├── Screen.kt
│   │   └── AppNavigation.kt
│   ├── screens/
│   │   ├── HomeScreen.kt
│   │   ├── CalculatorScreen.kt
│   │   ├── MinecraftAnalysisScreen.kt
│   │   ├── YouTubeDownloaderScreen.kt
│   │   ├── SettingsScreen.kt
│   │   └── AboutScreen.kt
│   └── theme/
│       ├── Color.kt
│       ├── Theme.kt
│       └── Type.kt
├── network/
│   └── ApiClient.kt
├── services/
│   └── DownloadService.kt
└── utils/
    ├── PermissionUtils.kt
    └── UpdateUtils.kt
```

## 🔧 Сборка

### Требования
- Android Studio Hedgehog или новее
- JDK 17
- Android SDK 34

### Шаги
1. Откройте проект в Android Studio
2. Дождитесь синхронизации Gradle
3. Запустите на эмуляторе или устройстве

## 📝 Примечания

### YouTube Загрузка
Для реальной загрузки видео необходимо:
- Интегрировать NewPipe Extractor
- Или использовать yt-dlp через серверный API

### ИИ Анализ
Для работы ИИ режима требуется:
- API ключ Google Gemini или OpenRouter
- Настроить соответствующие endpoint'ы

### Автообновление
Реализуйте проверку обновлений через:
- GitHub Releases API
- Собственный сервер

## 📄 Лицензия

MIT License

## 👨‍💻 Разработка

Проект создан для образовательных целей.
