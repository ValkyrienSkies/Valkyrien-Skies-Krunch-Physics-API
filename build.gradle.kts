plugins {
    kotlin("jvm") version "1.5.0"
    id("org.jlleitschuh.gradle.ktlint") version "10.0.0"
    java
    maven
    checkstyle
}

group = "org.valkyrienskies.physics_api_krunch"
version = "1.0"

repositories {
    mavenCentral()
    maven { setUrl("https://dl.bintray.com/kotlin/kotlin-eap") }
    maven { setUrl("https://jitpack.io") }
    maven {
        name = "Github Packages Krunch"
        setUrl("https://maven.pkg.github.com/ValkyrienSkies/Krunch-Physics-Engine")
        // Use VS Machine User account to download from github packages
        credentials {
            username = "valkyrienskies-machineuser"
            password = "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"
        }
    }
}

dependencies {
    // Kotlin
    implementation(kotlin("stdlib-jdk8"))

    // VS Physics API
    implementation("com.github.ValkyrienSkies:Valkyrien-Skies-Physics-API:4b8cbef1acbc31c4543f2a239440dfbb8c8c7f08")

    // Krunch
    implementation("org.valkyrienskies:krunch:1.0.0+c00eb987f1")

    // JOML for Math
    api("org.joml", "joml", "1.10.0")
    api("org.joml", "joml-primitives", "1.10.0")

    // Guava
    implementation("com.google.guava", "guava", "29.0-jre")

    // FastUtil for Fast Primitive Collections
    implementation("it.unimi.dsi", "fastutil", "8.2.1")

    // Junit 5 for Unit Testing
    testImplementation("org.junit.jupiter", "junit-jupiter", "5.4.2")
}

tasks.withType<Checkstyle> {
    reports {
        // Do not output html reports
        html.isEnabled = false
        // Output xml reports
        xml.isEnabled = true
    }
}

checkstyle {
    toolVersion = "8.41"
    configFile = file("$rootDir/.checkstyle/checkstyle.xml")
    isIgnoreFailures = false
}

ktlint {
    disabledRules.set(setOf("parameter-list-wrapping"))
    reporters {
        reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.CHECKSTYLE)
    }
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileJava {
        sourceCompatibility = "1.8"
        targetCompatibility = "1.8"
    }
    compileTestJava {
        sourceCompatibility = "1.8"
        targetCompatibility = "1.8"
    }
    test {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
    }
}
