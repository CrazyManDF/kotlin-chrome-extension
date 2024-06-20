plugins {
    kotlin("js")
}

dependencies {
    implementation(kotlin("stdlib-js"))
    implementation(project(":chromeextapimappings"))
//    implementation(project(":chrome"))
    implementation(project(":data"))
    implementation(Deps.Kotlin.serialization)
    implementation(Deps.Kotlin.Coroutines.core_js)
    implementation(Deps.Network.Ktor.client_core)
//    implementation(Deps.Network.Ktor.client_okhttp)
    implementation(Deps.Network.Ktor.client_js)
    implementation(Deps.Network.Ktor.client_serialization)
    implementation(Deps.Network.Ktor.client_content_negotiation)
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
