plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
}

kotlin {
    js(IR) {
        binaries.executable()
        browser {
            distribution {
                outputDirectory = File("$rootDir/build/distributions/")
            }
        }
    }
    sourceSets {
        val jsMain by getting {
            dependencies {
                implementation(kotlin("stdlib-js"))
//    implementation(project(":chrome"))
                implementation(project(":chromeextapimappings"))
                implementation(project(":data"))
                implementation(compose.html.core)
                implementation(compose.runtime)
                implementation(Deps.Kotlin.serialization)
                implementation("org.jetbrains.kotlinx:kotlinx-html-js:0.8.0")

                implementation("dev.shreyaspatil.generativeai:generativeai-google:0.5.0-1.0.0")

//                implementation("org.jetbrains.kotlin:kotlin-scripting-common")
//                implementation("org.jetbrains.kotlin:kotlin-scripting-jvm")
//                implementation("org.jetbrains.kotlin:kotlin-scripting-dependencies")
//                implementation("org.jetbrains.kotlin:kotlin-scripting-dependencies-maven")
//                // coroutines dependency is required for this particular definition
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
            }
        }
    }
}
