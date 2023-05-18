plugins {
    `java-library`
    `maven-publish`
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "com.runicrealms.plugin"
version = "1.0-SNAPSHOT"

dependencies {
    compileOnly(commonLibs.paper)
    compileOnly(commonLibs.spigot)
    implementation(commonLibs.acf)
    compileOnly(commonLibs.placeholderapi)
    compileOnly(commonLibs.configme)
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.runicrealms.plugin"
            artifactId = "chat"
            version = "1.0-SNAPSHOT"
            from(components["java"])
        }
    }
}

tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
//    build {
//        dependsOn(shadowJar)
//    }
}

tasks.register("wrapper")
tasks.register("prepareKotlinBuildScriptModel")