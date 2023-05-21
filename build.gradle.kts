plugins {
    `java-library`
    `maven-publish`
}

group = "com.runicrealms.plugin"
version = "1.0-SNAPSHOT"
val artifactName = "chat"

dependencies {
    compileOnly(commonLibs.paper)
    compileOnly(commonLibs.spigot)
    compileOnly(commonLibs.acf)
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
            artifactId = artifactName
            version = "1.0-SNAPSHOT"
            from(components["java"])
        }
    }
}