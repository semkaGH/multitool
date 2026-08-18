# 📱 Инструкция по сборке APK для MultiTool App

## ✅ Быстрый старт (3 способа)

---

### **Способ 1: Через Android Studio** (самый простой)

1. Откройте **Android Studio**
2. Нажмите **Open Project** → выберите папку `/workspace/MultiToolApp`
3. Дождитесь синхронизации Gradle
4. В меню: **Build → Build Bundle(s)/APK(s) → Build APK(s)**
5. Готовый APK будет в: `app/build/outputs/apk/debug/app-debug.apk`

---

### **Способ 2: Через командную строку**

```bash
cd /workspace/MultiToolApp
./gradlew assembleDebug
```

APK появится в: `app/build/outputs/apk/debug/app-debug.apk`

**Примечание:** Для сборки через консоль требуется установленный Android SDK.
Укажите путь к SDK в файле `local.properties`:
```
sdk.dir=/путь/к/вашему/android-sdk
```

Или установите переменную окружения:
```bash
export ANDROID_HOME=/путь/к/вашему/android-sdk
```

---

### **Способ 3: Release версия (оптимизированная)**

```bash
cd /workspace/MultiToolApp
./gradlew clean assembleRelease
```

APK будет в: `app/build/outputs/apk/release/app-release-unsigned.apk`

**Важно:** Для release версии требуется подпись (keystore).

---

## 📲 Установка на телефон

1. Скопируйте APK файл на устройство
2. Откройте файловый менеджер
3. Нажмите на APK и разрешите установку
4. Готово!

Или через USB с отладкой:
```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

---

## ⚙️ Что уже настроено в проекте:

✅ **Разрешения** в `AndroidManifest.xml`:
- Интернет (для загрузчика YouTube и ИИ)
- Доступ к файлам (для скачивания)
- Фоновые службы (для загрузок)

✅ **Gradle конфигурация**:
- SDK 34, минимальный API 24 (Android 7.0+)
- Kotlin + Jetpack Compose
- Material Design 3 с иконками
- Все необходимые зависимости

✅ **Инструменты сборки**:
- `gradlew` скрипт создан
- Версии Gradle и плагинов настроены

---

## 🔧 Исправленные ошибки компиляции:

1. **ViewModel StateFlow** - заменено делегирование `by` на правильное использование `_field.value`
   - Файлы: `MinecraftAnalyzerViewModel.kt`, `YouTubeDownloaderViewModel.kt`

2. **Compose StateFlow коллекция** - добавлено `collectAsState()` во всех экранах
   - Файлы: `MinecraftAnalysisScreen.kt`, `YouTubeDownloaderScreen.kt`

3. **Material Icons** - добавлена зависимость `material-icons-extended:1.6.0`
   - Файл: `app/build.gradle.kts`

4. **SettingsViewModel** - удалён несуществующий класс, настройки перенесены прямо в Screen
   - Файл: `SettingsScreen.kt`

5. **HorizontalDivider** - используется из Material3 1.2.0+

6. **onValueChange** - теперь вызывает методы `updateField()` вместо присваивания

---

## 📋 Полная инструкция

### Требования:
- **Android Studio** (рекомендуется последняя версия)
- **JDK 17** (встроен в Android Studio)
- **Android SDK** (API 34)

### Подробная сборка через Android Studio:

1. Откройте проект в Android Studio
2. Дождитесь завершения Gradle Sync
3. Выберите: Build → Build APK(s)
4. Найдите APK в: `app/build/outputs/apk/debug/`

### Сборка Release версии с подписью:

1. Создайте keystore:
```bash
keytool -genkey -v -keystore ~/multitool-release-key.jks \
  -keyalg RSA -keysize 2048 -validity 10000 -alias multitool
```

2. Добавьте signingConfig в `app/build.gradle.kts`

3. Соберите: Build → Generate Signed Bundle/APK

---

## 🔍 Решение проблем

### Ошибка: SDK not found
```bash
echo "sdk.dir=/path/to/android-sdk" > local.properties
```

### Ошибка: Java version mismatch
```bash
java -version  # Должна быть версия 17
```

### Ошибка: Not enough memory
В `gradle.properties`:
```properties
org.gradle.jvmargs=-Xmx4096m
```

---

## 🎯 Готово!

Теперь у вас есть рабочий APK файл MultiTool App!

**Пути к готовым APK:**
- Debug: `/workspace/MultiToolApp/app/build/outputs/apk/debug/app-debug.apk`
- Release: `/workspace/MultiToolApp/app/build/outputs/apk/release/app-release.apk`
