plugins {
    java
    kotlin("jvm") version "1.6.10"
    `maven-publish`
    jacoco
}

description = "kotlin-inquirer"
group = "com.github.kotlin-inquirer"
version = "0.1.0"

repositories {
    mavenCentral()
    maven("https://us-central1-maven.pkg.dev/varabyte-repos/public")
}

dependencies {
    implementation(kotlin("stdlib"))
    api("com.varabyte.kotter:kotter:1.0.0-SNAPSHOT")
    testImplementation(kotlin("test"))
    testImplementation("com.varabyte.kotterx:kotter-test-support:1.0.0-SNAPSHOT")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
}

kotlin {
    explicitApi()
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }

    test {
        useJUnitPlatform()
    }

    register<JacocoReport>("codeCoverageReport") {
        dependsOn(test)

        executionData.setFrom(fileTree(project.rootDir.absolutePath) {
            include("**/build/jacoco/*.exec")
        })

        reports {
            xml.required.set(true)
            xml.outputLocation.set(file("$buildDir/reports/jacoco/report.xml"))
            html.required.set(false)
            csv.required.set(false)
        }

        sourceSets(sourceSets.main.get())
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.github.kotlin-inquirer"
            artifactId = "kotlin-inquirer"
            version = "0.1.0"

            from(components["java"])
        }
    }
}
