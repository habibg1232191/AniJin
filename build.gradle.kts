import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
    kotlin("plugin.serialization") version "1.5.31"
    id("org.jetbrains.compose") version "1.0.1-rc2"
}

group = "me.habib"
version = "1.0"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.3.1")
    implementation("com.arkivanov.decompose:decompose:0.4.0")
    implementation("com.arkivanov.decompose:extensions-compose-jetbrains:0.4.0")
    implementation("io.ktor:ktor-client-core:1.6.6")
    implementation("io.ktor:ktor-client-apache:1.6.6")
    implementation("io.ktor:ktor-client-serialization:1.6.6")
    implementation("net.bramp.ffmpeg:ffmpeg:0.6.2")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.20")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "16"
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "AniJin"
            packageVersion = "1.0.0"
        }
    }
}