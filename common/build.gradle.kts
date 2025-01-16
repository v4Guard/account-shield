plugins {
    id("account-shield.common-conventions")
}

dependencies {
    api(project(":api"))
    compileOnly(libs.v4guard.connector)
    compileOnly(libs.jackson.databind)
}