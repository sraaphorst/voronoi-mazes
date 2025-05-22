group = "org.vorpal"
version = "1.0-SNAPSHOT"

plugins {
    java
    application
    id("org.openjfx.javafxplugin") version "0.0.14"
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

javafx {
    version = "21"
    modules = listOf(
        "javafx.controls",
        "javafx.graphics"
    )
}

application {
    mainClass.set("org.vorpal.Main")
}

tasks.register("printJavaFxJars") {
    doLast {
        // resolve the runtime classpath
        val rt = configurations.named("runtimeClasspath").get().filter {
            it.name.startsWith("javafx-")
        }
        if (rt.isEmpty()) {
            println("❌ No JavaFX jars found on runtimeClasspath.")
        } else {
            println("✅ JavaFX jars on runtimeClasspath:")
            rt.forEach { println("   " + it.absolutePath) }
        }
    }
}

// Stage the JavaFX jars for easy access from IntelliJ.
tasks.register<Copy>("stageJavaFxJars") {
    from(configurations.named("runtimeClasspath").get().filter {
        it.name.startsWith("javafx-")
    })
    into("${layout.buildDirectory.get().asFile.absolutePath}/javafx")
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

}

tasks.test {
    useJUnitPlatform()
}