plugins {
    application
    kotlin("jvm")
}
group = "com.jetbrains.handson"
version = "1.0-SNAPSHOT"

application {
    mainClassName = "iio.ktor.server.netty.EngineMain"
}

val ktor_version: String by project
val logback_version: String by project

dependencies {
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("io.ktor:ktor-websockets:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
}