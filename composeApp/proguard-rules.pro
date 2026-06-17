-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile
-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod
-keep public class * extends java.lang.Exception

# ---------------------------------------------------------------------------
# kotlinx.serialization — official guidance from the kotlinx.serialization repo.
# @Serializable types are accessed reflectively at runtime.
# ---------------------------------------------------------------------------
-if @kotlinx.serialization.Serializable class **
-keepclassmembers class <1> {
    static <1>$Companion Companion;
}

-if @kotlinx.serialization.Serializable class ** {
    static **$Companion *;
}
-keepclassmembers class <2>$Companion {
    kotlinx.serialization.KSerializer serializer(...);
}

-if @kotlinx.serialization.Serializable class ** {
    public static ** INSTANCE;
}
-keepclassmembers class <1> {
    public static <1> INSTANCE;
    kotlinx.serialization.KSerializer serializer(...);
}

-keep,includedescriptorclasses class com.renatojobal.kmptemplate.**$$serializer { *; }
-keepclassmembers class com.renatojobal.kmptemplate.** {
    *** Companion;
}
-keepclasseswithmembers class com.renatojobal.kmptemplate.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# ---------------------------------------------------------------------------
# SQLDelight: generated database classes are referenced reflectively by the
# driver during schema migrations.
# ---------------------------------------------------------------------------
-keep class com.renatojobal.kmptemplate.db.** { *; }

-dontwarn org.slf4j.**
