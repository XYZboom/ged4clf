import com.vanniktech.maven.publish.SonatypeHost

plugins {
    kotlin("jvm") version "2.1.21"
    id("com.vanniktech.maven.publish") version "0.31.0"
}

val myGroup = "io.github.xyzboom"
val myId = "ged4clf"
val myVersion = "0.1.0"
group = "io.github.xyzboom"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

    signAllPublications()

    coordinates(myGroup, myId, myVersion)

    pom {
        name.set(myId)
        description.set("A Kotlin wrapper for gedlib")
        url.set("https://github.com/XYZboom/ged4clf")

        licenses {
            license {
                name.set("Apache-2.0")
                url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }

        developers {
            developer {
                id.set("XYZboom")
                name.set("Xiaotian Ma")
                email.set("xyzboom@qq.com")
            }
        }

        scm {
            connection = "scm:git:https://github.com/XYZboom/ged4clf.git"
            developerConnection = "scm:git:https://github.com/XYZboom/ged4clf.git"
            url = "https://github.com/XYZboom/ged4clf.git"
        }
    }
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(11)
}