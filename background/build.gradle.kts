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
