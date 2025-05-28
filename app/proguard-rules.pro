# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Saving the kotlin.Metadata annotation needed for kotlin-reflect
-keep class kotlin.Metadata { *; }

# saving all nested classes in the package com.jeksonsoftsolutions.cryptos.ui.scaffold.environment
-keep class com.jeksonsoftsolutions.cryptos.ui.scaffold.environment.** { *; }

# Saving class names used in reflection
-keepnames class com.jeksonsoftsolutions.cryptos.ui.scaffold.environment.** { *; }

# Saving nested classes to avoid problems with Class.forName
-keepattributes InnerClasses, EnclosingMethod

# saving the signatures needed for reflection
-keepattributes Signature

# Adding rules for network models
-keep class com.jeksonsoftsolutions.cryptos.data.network.entities.** { *; }
-keepclassmembers class com.jeksonsoftsolutions.cryptos.data.network.entities.** { *; }

# Adding rules for domain models
-keep class com.jeksonsoftsolutions.cryptos.domain.models.** { *; }
-keepclassmembers class com.jeksonsoftsolutions.cryptos.domain.models.** { *; }

# Adding rules for Room entities
#-keep class com.jeksonsoftsolutions.cryptos.data.local.room.entities.** { *; }
#-keepclassmembers class com.jeksonsoftsolutions.cryptos.data.local.room.entities.** { *; }

# Rules for Gson
-keep class com.google.gson.** { *; }
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer
-keepclassmembers,allowobfuscation class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# Rools for Kotlin Serialization
-keepclassmembers class kotlinx.** { *; }
-keepclassmembers class kotlinx.serialization.** { *; }
-keep,includedescriptorclasses class com.jeksonsoftsolutions.cryptos.**$$serializer { *; }
-keepclassmembers class com.jeksonsoftsolutions.cryptos.** {
    *** Companion;
}
-keepclasseswithmembers class com.jeksonsoftsolutions.cryptos.** {
    kotlinx.serialization.KSerializer serializer(...);
}
