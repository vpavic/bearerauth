plugins {
    `java-library`
    id("io.github.vpavic.bearerauth.conventions")
}

dependencies {
    implementation(platform("org.junit:junit-bom:5.5.2"))
    implementation(platform("org.springframework:spring-framework-bom:5.1.12.RELEASE"))

    api(project(":bearerauth-core"))
    api("org.springframework:spring-webflux")

    testImplementation("org.assertj:assertj-core:3.14.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api")

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}