plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.wingspan.shopkeeper"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.wingspan.shopkeeper"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures{
        viewBinding=true
    }
}

dependencies {
    //glide
    implementation ("com.github.bumptech.glide:glide:4.12.0")

    // Add BouncyCastle (if required)
    implementation ("org.bouncycastle:bcprov-jdk15on:1.70")
    implementation ("org.bouncycastle:bcpkix-jdk15on:1.70")

    // Add Conscrypt for modern TLS/SSL support
    implementation ("org.conscrypt:conscrypt-android:2.5.2")

    //swiperefreshlayout
    implementation ("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    // Ensure OkHttp is up-to-date
    implementation ("com.squareup.okhttp3:okhttp:4.10.0")
    //view model
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.6")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.8.6")
    //retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.1")

    //pinview
    implementation ("io.github.chaosleung:pinview:1.4.4")
    //viewpager
    implementation ("androidx.viewpager2:viewpager2:1.1.0")
    //navigation component
    implementation ("androidx.navigation:navigation-fragment-ktx:2.8.0")
    implementation ("androidx.navigation:navigation-ui-ktx:2.8.0")

    //circular Image
    implementation ("de.hdodenhof:circleimageview:3.1.0")

    //location
    implementation ("com.google.android.gms:play-services-location:21.3.0")

    // Facebook Shimmer Layout
    implementation("com.facebook.shimmer:shimmer:0.5.0")
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}