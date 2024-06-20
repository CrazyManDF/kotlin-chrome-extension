//plugins {
//    kotlin("multiplatform")
//}

//plugins {
//    // this is necessary to avoid the plugins to be loaded multiple times
//    // in each subproject's classloader
//    alias(libs.plugins.jetbrainsCompose) apply false
//    alias(libs.plugins.compose.compiler) apply false
//    alias(libs.plugins.kotlinMultiplatform) apply false
//}

plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    id("org.jetbrains.compose") apply false
    id("org.jetbrains.kotlin.plugin.compose") apply false
    id("org.jetbrains.kotlin.multiplatform") apply false
}
//
//allprojects {
//    repositories {
//        google()
//        mavenCentral()
//        gradlePluginPortal()
//        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
//    }
//}
