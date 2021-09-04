plugins {
    kotlin("jvm") version "1.5.0"
    id("org.jlleitschuh.gradle.ktlint") version "10.0.0"
    java
    maven
    checkstyle
    `maven-publish`
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
    maven { setUrl("https://dl.bintray.com/kotlin/kotlin-eap") }
    maven { setUrl("https://jitpack.io") }
    maven {
        name = "Valkyrien Skies Internal"
        setUrl("https://maven.valkyrienskies.org/repository/internal/")
    }
}

dependencies {
    // Kotlin
    implementation(kotlin("stdlib-jdk8"))

    // VS Physics API
    api("org.valkyrienskies:physics_api:1.0.0+e6f7e5f1c5")

    // Krunch
    implementation("org.valkyrienskies:krunch:1.0.0+a7f6e3ce56")

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

        // Publish to VS maven
        if ((System.getenv("VS_MAVEN_USERNAME") != null) and (System.getenv("VS_MAVEN_PASSWORD") != null)) {
            maven {
                name = "ValkyrienSkiesMaven"
                url = uri("https://maven.valkyrienskies.org/repository/internal/")
                credentials {
                    username = System.getenv("VS_MAVEN_USERNAME")
                    password = System.getenv("VS_MAVEN_PASSWORD")
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
