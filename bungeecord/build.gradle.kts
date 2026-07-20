plugins {
    packetevents.`shadow-conventions`
    packetevents.`library-conventions`
    packetevents.`publish-conventions`
}

repositories {
    mavenCentral()
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    api(project(":api"))
    api(project(":netty-common"))

    compileOnly(libs.bungeecord)
    implementation(libs.bundles.adventure)
    implementation(libs.bundles.adventure.serializers)
    implementation(libs.bstats.bungeecord)
}
