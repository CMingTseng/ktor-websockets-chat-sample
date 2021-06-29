buildscript {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        jcenter()
        google()
    }

    dependencies {
    }
}

plugins {
    kotlin("jvm") version "1.5.10"
}

allprojects{
    apply(plugin = "org.jetbrains.kotlin.jvm")
    repositories {
        mavenLocal()
        mavenCentral()
        jcenter()
        gradlePluginPortal()
        google()
    }
}