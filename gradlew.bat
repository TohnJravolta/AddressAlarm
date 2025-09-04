@echo off
set DIR=%~dp0
if exist "%DIR%gradle\wrapper\gradle-wrapper.jar" (
  java -jar "%DIR%gradle\wrapper\gradle-wrapper.jar" %*
) else (
  echo gradle-wrapper.jar not found. Open the project in Android Studio to download it automatically.
  exit /b 1
)
