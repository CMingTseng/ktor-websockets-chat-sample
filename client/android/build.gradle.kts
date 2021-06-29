plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
}

android {
    compileSdkVersion(30)
    buildToolsVersion("30.0.3")
    buildFeatures.viewBinding = true
    lintOptions {
        isIgnoreTestSources = true
    }
    defaultConfig {
        applicationId = "org.nosemaj.wsreader"
        minSdkVersion(21)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    testOptions.unitTests.isIncludeAndroidResources = true

    compileOptions {
        targetCompatibility = JavaVersion.VERSION_1_8
        sourceCompatibility = JavaVersion.VERSION_1_8
    }

//    flavorDimensions("demo")
//    productFlavors {
//        register("flavor1")
//        register("flavorA")
//    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    packagingOptions {
        exclude("META-INF/kotlinx-coroutines-core.kotlin_module")
    }
}

dependencies {
    testImplementation("junit:junit:4.12")
    androidTestImplementation("com.android.support.test:runner:1.0.2")
    androidTestImplementation("com.android.support.test.espresso:espresso-core:3.0.2")
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
//    implementation(kotlin("stdlib", org.jetbrains.kotlin.config.KotlinCompilerVersion.VERSION))
    implementation(kotlin("stdlib", project.extra["kotlin_version"].toString()))

    implementation("androidx.core:core-ktx:1.3.2")
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("com.google.android.material:material:1.2.1")
    implementation("androidx.constraintlayout:constraintlayout:2.0.4")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.2.0")

    implementation("io.ktor:ktor-client-websockets:${project.extra["ktor_version"].toString()}")
    implementation("io.ktor:ktor-client-okhttp:${project.extra["ktor_version"].toString()}")

    implementation("org.conscrypt:conscrypt-android:2.5.1")
}