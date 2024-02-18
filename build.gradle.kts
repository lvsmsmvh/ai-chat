// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.4.0")
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.9.9")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.21")
        classpath("com.google.firebase:perf-plugin:1.4.2")
    }
    repositories {
        google()
        mavenCentral()
    }
}


plugins {
    id("com.android.application") version "8.1.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.20" apply false    // remove later
    id("com.google.devtools.ksp") version "1.9.21-1.0.16" apply false
    id("com.android.library") version "7.3.0" apply false
//    kotlin("jvm") version "1.9.21" // or kotlin("multiplatform") or any other kotlin plugin
//    kotlin("plugin.serialization") version "1.9.21"
}

//tasks.register("clean", Delete::class) {
//    delete(rootProject.buildDir)
//}