plugins {
    id("account-shield.common-conventions")
}

dependencies {
    api(project(":api"))
    api(libs.v4guard.connector)
}