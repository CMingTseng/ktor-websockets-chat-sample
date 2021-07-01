buildscript {
    val kotlin_version: String by project
    val ktor_version: String by project
    extra.apply {
        set("android_gradle_plugin_version", "4.1.2")
        set("kotlin_version", kotlin_version)
        set("ktor_version", ktor_version)
        set("serialization_version", "1.2.1")
    }
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        jcenter()
        google()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:${rootProject.extra["android_gradle_plugin_version"].toString()}")
        classpath(kotlin("gradle-plugin", rootProject.extra["kotlin_version"].toString()))
        classpath(kotlin("serialization", rootProject.extra["kotlin_version"].toString()))
    }
}

allprojects {
    repositories {
        mavenLocal()
        mavenCentral()
        jcenter()
        gradlePluginPortal()
        google()
    }
}