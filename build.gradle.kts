buildscript {
    val kotlin_version: String by project
    extra.apply {
        set("kotlin_version", kotlin_version)
    }
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        jcenter()
        google()
    }

    dependencies {
        classpath(kotlin("gradle-plugin", rootProject.extra["kotlin_version"].toString()))
    }
}


allprojects{
    repositories {
        mavenLocal()
        mavenCentral()
        jcenter()
        gradlePluginPortal()
        google()
    }
}