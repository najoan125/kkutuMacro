plugins {
    id("java")
    id("com.gradleup.shadow") version "8.3.0"
}

group = "kr.hyfata.najoan"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.seleniumhq.selenium:selenium-java:4.24.0")
    implementation("com.formdev:flatlaf:3.5.1")
    // https://mvnrepository.com/artifact/ch.qos.logback/logback-classic
    implementation("ch.qos.logback:logback-classic:1.2.13")
}

tasks.test {
    useJUnitPlatform()
}

tasks.jar {
    manifest { attributes["Main-Class"] = "kr.hyfata.najoan.kkutumacro.Main" }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}