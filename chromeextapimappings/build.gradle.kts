plugins {
    kotlin("js")
}

dependencies {
    implementation(Deps.Kotlin.Coroutines.core)
    implementation(Deps.Kotlin.Coroutines.core_js)
}

kotlin {
    js(IR) {
        binaries.executable()
        browser {}
    }
}

// https://github.com/gioandtonic/ChromeExtAPIMappings/
