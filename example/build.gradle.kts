plugins {
    kotlin("jvm")
    id("com.github.johnrengelman.shadow") version "7.1.2"
    application
}

group = "io.github.kotlin-inquirer"

repositories {
    mavenCentral()
    maven("https://us-central1-maven.pkg.dev/varabyte-repos/public") // REMOVE ME
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":"))
}

tasks {
    shadowJar {
        archiveBaseName.set("kotlin-pizza")
        archiveClassifier.set("")
        archiveVersion.set("")
        manifest {
            attributes(Pair("Main-Class", "PizzaKt"))
        }
    }
}

application {
    mainClass.set("PizzaKt")
//    mainClass.set("UsagesKt")
}
