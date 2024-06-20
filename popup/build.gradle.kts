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
            }
        }
    }
}
