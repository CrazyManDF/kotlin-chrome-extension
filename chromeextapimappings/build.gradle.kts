plugins {
    kotlin("multiplatform")
}

kotlin {
    js(IR) {
        binaries.executable()
        browser {}
    }
    sourceSets {
        val jsMain by getting {
            dependencies {
                implementation(Deps.Kotlin.Coroutines.core)
                implementation(Deps.Kotlin.Coroutines.core_js)
            }
        }
    }
}

// https://github.com/gioandtonic/ChromeExtAPIMappings/
