plugins {
    kotlin("jvm") version "1.5.0"
    java
    maven
    `maven-publish`
    application // Add a main class for testing loading binaries within the jar
}

group = "org.valkyrienskies.physics_api_krunch"
// Determine the version
if (project.hasProperty("CustomReleaseVersion")) {
    version = project.property("CustomReleaseVersion") as String
} else {
    // Yes, I know there is a gradle plugin to detect git version.
    // But its made by Palantir 0_0.
    val gitRevisionProcess = Runtime.getRuntime().exec("git rev-parse HEAD", emptyArray(), File("."))
    val processInputStream = gitRevisionProcess.inputStream

    var gitRevision = ""
    while (true) {
        val lastReadByte = processInputStream.read()
        if (lastReadByte == -1) break
        gitRevision += lastReadByte.toChar()
    }
    version = "1.0.0+" + gitRevision.substring(0, 10)
}

repositories {
    mavenCentral()
    maven { setUrl("https://jitpack.io") }
}

dependencies {
    // Kotlin
    implementation(kotlin("stdlib-jdk8"))

    // VS Physics API
    api("com.github.ValkyrienSkies:Valkyrien-Skies-Physics-API:c84f1419e606de702e43d5417dd1c925bf0eefd8")

    // JOML for Math
    api("org.joml", "joml", "1.10.0")
    api("org.joml", "joml-primitives", "1.10.0")

    // FastUtil for Fast Primitive Collections
    implementation("it.unimi.dsi", "fastutil", "8.2.1")

    // Junit 5 for Unit Testing
    testImplementation("org.junit.jupiter", "junit-jupiter", "5.4.2")
}

// Set the main class name
val mainClassLocation = "org.valkyrienskies.physics_api_krunch.MainKt"
application {
    mainClass.set(mainClassLocation)
}
project.setProperty("mainClassName", mainClassLocation)

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

// Publish javadoc and sources to maven
java {
    withJavadocJar()
    withSourcesJar()
}

publishing {
    repositories {
        val ghpUser = (project.findProperty("gpr.user") ?: System.getenv("USERNAME")) as String?
        val ghpPassword = (project.findProperty("gpr.key") ?: System.getenv("TOKEN")) as String?
        // Publish to Github Packages
        if (ghpUser != null && ghpPassword != null) {
            maven {
                name = "GithubPackages"
                url = uri("https://maven.pkg.github.com/ValkyrienSkies/Valkyrien-Skies-Krunch-Physics-API")
                credentials {
                    username = ghpUser
                    password = ghpPassword
                }
            }
        }
    }
    publishing {
        publications {
            create<MavenPublication>("maven") {
                groupId = "org.valkyrienskies"
                artifactId = "physics_api_krunch"
                version = project.version as String

                from(components["java"])
            }
        }
    }
}
