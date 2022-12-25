import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
  kotlin("multiplatform")
}

group = "org.jraf"
version = "1.0.0"

repositories {
  mavenCentral()
}

kotlin {
  jvm {
    compilations.all {
      kotlinOptions {
        jvmTarget = "1.8"
      }
    }
  }
  macosArm64 {
    binaries {
      executable {
        entryPoint = "org.jraf.memoryleak.main"
      }
    }
  }
  linuxX64 {
    binaries {
      executable {
        entryPoint = "org.jraf.memoryleak.main"
      }
    }
  }

  sourceSets {
    val nativeMain by creating

    val commonMain by getting {
      dependencies {
        implementation(Ktor.client.core)
      }
    }
    val macosArm64Main by getting {
      dependsOn(nativeMain)

      dependencies {
        implementation(Ktor.client.curl)
      }
    }
    val linuxX64Main by getting {
      dependsOn(nativeMain)

      dependencies {
        implementation(Ktor.client.curl)
      }
    }
    val jvmMain by getting {
      dependencies {
        implementation(Ktor.client.okHttp)
      }
    }

    val jvmTest by getting {
      dependencies {
        implementation(Kotlin.test.junit)
      }
    }
  }

  // See https://kotlinlang.org/docs/native-memory-manager.html#memory-consumption
  targets.withType<KotlinNativeTarget> {
    binaries.all {
      freeCompilerArgs = freeCompilerArgs + "-Xallocator=std"
    }
  }
}

// `./gradlew refreshVersions` to update dependencies
