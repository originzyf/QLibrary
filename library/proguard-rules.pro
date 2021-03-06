#Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}

#Picasso
-dontwarn com.squareup.okhttp.**
-keep class com.squareup.okhttp.** { *;}

#okio
-dontwarn okio.**

#Retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

#Databinding
-dontwarn android.databinding.**
-keep public class * extends android.databinding.ViewDataBinding {
 *;
}

#MaterialDesign
-keep class com.rey.material.** { *; }

#uCrop
-dontwarn com.yalantis.ucrop**
-keep class com.yalantis.ucrop** { *; }
-keep interface com.yalantis.ucrop** { *; }