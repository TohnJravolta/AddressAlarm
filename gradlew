#!/usr/bin/env sh
DIR="$(cd "$(dirname "$0")" && pwd)"
if [ -f "$DIR/gradle/wrapper/gradle-wrapper.jar" ]; then
  exec java -jar "$DIR/gradle/wrapper/gradle-wrapper.jar" "$@"
else
  echo "gradle-wrapper.jar not found. Open the project in Android Studio to download it automatically."
  exit 1
fi
