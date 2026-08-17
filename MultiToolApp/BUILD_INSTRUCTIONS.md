# 📱 Инструкция по сборке APK для MultiTool App

## 📋 Требования

### Обязательные:
- **Android Studio** (рекомендуется последняя версия)
  - Скачать: https://developer.android.com/studio
- **JDK 17** (встроен в Android Studio)

### Опционально (для сборки через командную строку):
- Установленная переменная окружения `ANDROID_HOME`
- Командная строка или терминал

---

## 🔧 Способ 1: Сборка через Android Studio (Рекомендуется)

### Шаг 1: Откройте проект
1. Запустите **Android Studio**
2. Нажмите **"Open an Existing Project"**
3. Выберите папку `/workspace/MultiToolApp`
4. Дождитесь завершения синхронизации Gradle

### Шаг 2: Настройте сборку (если нужно)
1. Откройте `app/build.gradle.kts`
2. Проверьте версию приложения в `versionName = "1.0.0"`
3. При необходимости измените `versionCode`

### Шаг 3: Сборка Debug APK (для тестирования)
1. В меню выберите: **Build → Build Bundle(s) / APK(s) → Build APK(s)**
2. Дождитесь завершения сборки
3. APK файл будет создан по пути:
   ```
   /workspace/MultiToolApp/app/build/outputs/apk/debug/app-debug.apk
   ```

### Шаг 4: Сборка Release APK (для публикации)

#### Вариант A: Без подписи (только для тестов)
1. В меню: **Build → Generate Signed Bundle / APK**
2. Выберите **APK**
3. Создайте новый ключ или используйте существующий
4. Выберите сборку **release**
5. Нажмите **Finish**

#### Вариант B: С подписью (для Google Play)
1. Создайте keystore файл:
   ```bash
   keytool -genkey -v -keystore ~/multitool-release-key.jks \
     -keyalg RSA -keysize 2048 -validity 10000 -alias multitool
   ```

2. Добавьте в `gradle.properties` (в корне проекта):
   ```properties
   MULTITOOL_STORE_FILE=/path/to/multitool-release-key.jks
   MULTITOOL_STORE_PASSWORD=ваш_пароль
   MULTITOOL_KEY_ALIAS=multitool
   MULTITOOL_KEY_PASSWORD=ваш_пароль_ключа
   ```

3. Обновите `app/build.gradle.kts`, добавив signingConfigs:
   ```kotlin
   android {
       signingConfigs {
           create("release") {
               storeFile = file(System.getenv("MULTITOOL_STORE_FILE") ?: "path/to/keystore.jks")
               storePassword = System.getenv("MULTITOOL_STORE_PASSWORD") ?: ""
               keyAlias = System.getenv("MULTITOOL_KEY_ALIAS") ?: ""
               keyPassword = System.getenv("MULTITOOL_KEY_PASSWORD") ?: ""
           }
       }
       buildTypes {
           release {
               isMinifyEnabled = true
               proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
               signingConfig = signingConfigs.getByName("release")
           }
       }
   }
   ```

4. Соберите: **Build → Generate Signed Bundle / APK**

---

## 💻 Способ 2: Сборка через командную строку

### Шаг 1: Перейдите в директорию проекта
```bash
cd /workspace/MultiToolApp
```

### Шаг 2: Сборка Debug APK
```bash
./gradlew assembleDebug
```

APK будет создан по пути:
```
app/build/outputs/apk/debug/app-debug.apk
```

### Шаг 3: Сборка Release APK
```bash
./gradlew assembleRelease
```

APK будет создан по пути:
```
app/build/outputs/apk/release/app-release-unsigned.apk
```

### Шаг 4: Очистка и пересборка (если есть ошибки)
```bash
./gradlew clean
./gradlew assembleRelease
```

---

## 🚀 Способ 3: Быстрая сборка одной командой

```bash
cd /workspace/MultiToolApp && ./gradlew clean assembleDebug --stacktrace
```

После успешной сборки скопируйте APK:
```bash
cp app/build/outputs/apk/debug/app-debug.apk ~/MultiTool.apk
```

---

## 📲 Установка APK на устройство

### Через USB:
1. Включите **Отладку по USB** на устройстве
2. Подключите устройство к компьютеру
3. Выполните команду:
   ```bash
   adb install /workspace/MultiToolApp/app/build/outputs/apk/debug/app-debug.apk
   ```

### Через файловый менеджер:
1. Скопируйте APK файл на устройство
2. Откройте файловый менеджер
3. Нажмите на APK файл и разрешите установку из неизвестных источников

---

## ⚙️ Настройка перед сборкой

### 1. Разрешения (уже настроены в AndroidManifest.xml)
Убедитесь, что в `AndroidManifest.xml` есть необходимые разрешения:
```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" 
    android:maxSdkVersion="29" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" 
    android:maxSdkVersion="32" />
<uses-permission android:name="android.permission.MANAGE_EXTERNAL_STORAGE" />
```

### 2. API ключи для ИИ-режима
Создайте файл `local.properties` в корне проекта:
```properties
GEMINI_API_KEY=ваш_ключ_gemini
OPENROUTER_API_KEY=ваш_ключ_openrouter
```

### 3. Настройка ProGuard (для уменьшения размера APK)
В `app/build.gradle.kts` для release сборки:
```kotlin
buildTypes {
    release {
        isMinifyEnabled = true
        isShrinkResources = true
        proguardFiles(
            getDefaultProguardFile("proguard-android-optimize.txt"),
            "proguard-rules.pro"
        )
    }
}
```

---

## 🔍 Решение проблем

### Ошибка: SDK not found
```bash
export ANDROID_HOME=$HOME/Android/Sdk
export PATH=$PATH:$ANDROID_HOME/tools:$ANDROID_HOME/platform-tools
```

### Ошибка: Java version mismatch
Убедитесь, что используется JDK 17:
```bash
java -version
```

### Ошибка: Not enough memory
В `gradle.properties` увеличьте память:
```properties
org.gradle.jvmargs=-Xmx4096m -Dfile.encoding=UTF-8
```

### Ошибка: Build failed with error 65280
Очистите кэш:
```bash
./gradlew clean
rm -rf ~/.gradle/caches/
./gradlew assembleDebug
```

---

## 📊 Размер APK

- **Debug APK**: ~50-80 MB (включает отладочную информацию)
- **Release APK**: ~30-50 MB (оптимизированный)
- **Release с ProGuard**: ~20-35 MB (максимально оптимизированный)

---

## ✅ Проверка сборки

После сборки проверьте APK:
```bash
ls -lh app/build/outputs/apk/debug/app-debug.apk
```

Проверьте подпись (для release):
```bash
jarsigner -verify -verbose -certs app/build/outputs/apk/release/app-release.apk
```

---

## 🎯 Готово!

Теперь у вас есть рабочий APK файл MultiTool App, который можно:
- Установить на любое Android устройство (Android 7.0+)
- Распространять среди пользователей
- Опубликовать в Google Play Store (требуется подпись)

**Пути к готовым APK:**
- Debug: `/workspace/MultiToolApp/app/build/outputs/apk/debug/app-debug.apk`
- Release: `/workspace/MultiToolApp/app/build/outputs/apk/release/app-release.apk`
