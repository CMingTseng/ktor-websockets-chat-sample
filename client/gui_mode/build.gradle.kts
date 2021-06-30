plugins {
    application
    kotlin("jvm")
}

group = "com.jetbrains.handson"
version = "1.0-SNAPSHOT"
application {
    mainClassName = "com.jetbrains.handson.chat.client.ChatClient"
}

val ktor_version: String by project
val logback_version: String by project
dependencies {
    implementation("io.ktor:ktor-client-websockets:$ktor_version")
    implementation("io.ktor:ktor-client-cio:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
}