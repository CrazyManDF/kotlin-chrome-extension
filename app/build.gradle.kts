plugins {
    kotlin("multiplatform")
}

group = "com.template"
version = "0.1.0"

kotlin {
    js(IR){
        binaries.executable()
        browser {}
    }
    sourceSets {
        val jsMain by getting {
            dependencies {
                implementation(project(":background"))
                implementation(project(":content"))
                implementation(project(":popup"))
            }
        }
    }
}

tasks {
    // Copy js scripts
    val background = ":background:jsBrowserDistribution"
    val content = ":content:jsBrowserDistribution"
    val popup = ":popup:jsBrowserDistribution"
    val extensionFolder = "$projectDir/build/extension"

    val copyBundleFile = register<Copy>("copyBundleFile") {
        dependsOn(background, content, popup)
        from(
            "$projectDir/../build/distributions/background.js",
            "$projectDir/../build/distributions/content.js",
            "$projectDir/../build/distributions/popup.js",
        )
        into(extensionFolder)
    }

    // Copy resources
    val copyResources = register<Copy>("copyResources") {
        val resourceFolder = "src/jsMain/resources"
        from(
            "$resourceFolder/manifest.json",
            "$resourceFolder/icons",
            "$resourceFolder/html",
            "$resourceFolder/css"
        )
        into(extensionFolder)
    }

    // Build modules
    val buildExtension = register("buildExtension") {
        dependsOn(copyBundleFile, copyResources)
    }

    // Zip extension
    val packageExtension = register<Zip>("packageExtension") {
        dependsOn(buildExtension)
        from(extensionFolder)
    }
}
