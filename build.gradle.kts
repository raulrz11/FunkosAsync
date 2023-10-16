plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.xerial:sqlite-jdbc:3.43.0.0")
    implementation("ch.qos.logback:logback-classic:1.4.11")
    implementation("com.google.code.gson:gson:2.7")
    implementation("com.zaxxer:HikariCP:5.0.1")
    implementation("org.mybatis:mybatis:3.5.13")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}