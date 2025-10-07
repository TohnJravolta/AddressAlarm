-keep class org.flagdrive.service.** { *; }
-keep class androidx.room.** { *; }
-keep class * extends android.accessibilityservice.AccessibilityService

# SQLCipher
-keep class net.sqlcipher.** { *; }
-keep class net.sqlcipher.database.** { *; }

# Tink
-keep class com.google.crypto.tink.** { *; }

# Javax Annotation
-dontwarn javax.annotation.Nullable
-dontwarn javax.annotation.concurrent.GuardedBy

# Google API Client
-keep class com.google.api.client.http.** { *; }
-keep class com.google.api.client.http.javanet.** { *; }

# Joda-Time
-keep class org.joda.time.Instant
