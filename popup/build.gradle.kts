plugins {
    kotlin("js")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
}

dependencies {
    implementation(kotlin("stdlib-js"))
//    implementation(project(":chrome"))
    implementation(project(":chromeextapimappings"))
    implementation(project(":data"))
    implementation(compose.html.core)
    implementation(compose.runtime)
    implementation(Deps.Kotlin.serialization)
}

kotlin {
    js(IR) {
        binaries.executable()
        browser {
            distribution {
                directory = File("$rootDir/build/distributions/")
            }
        }
    }
}
