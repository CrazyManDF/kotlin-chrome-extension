plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

kotlin {
    js(IR) {
        binaries.executable()
        browser {}
    }

    sourceSets {
        commonMain.dependencies {
            implementation(Deps.Kotlin.serialization)
        }
    }
}
