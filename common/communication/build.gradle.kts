plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") //Ref : https://kotlinlang.org/docs/serialization.html#example-json-serialization
}

group = "tw.gov.president.communication"
version = "0.0.1"

dependencies {
    implementation(kotlin("stdlib", project.extra["kotlin_version"].toString()))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${project.extra["serialization_version"].toString()}")
}