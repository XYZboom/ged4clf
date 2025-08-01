plugins {
    `cpp-library`
    idea
}

group = "io.github.xyzboom"
version = "0.2.0"

idea {
    module {
        val currentExcludes: Set<File> = excludeDirs

        val newExcludes = projectDir.listFiles { file ->
            file.isDirectory && file.name.startsWith("cmake-build")
        }?.toSet() ?: emptySet()

        excludeDirs = currentExcludes + newExcludes
    }
}

@Suppress("PropertyName")
val GEDLIB_ROOT = (System.getenv("GEDLIB_ROOT") ?: project.properties["GEDLIB_ROOT"]).apply {
    if (this == null) {
        throw GradleException("GEDLIB_ROOT not found in environment or property.")
    }
    this as String
}

tasks.withType<CppCompile> {
    val javaHome = System.getProperty("java.home")
    includes.from("${javaHome}/include")
    if (org.gradle.internal.os.OperatingSystem.current().isLinux) {
        includes.from("${javaHome}/include/linux")
    }
    includes.from("$GEDLIB_ROOT")
    includes.from("${GEDLIB_ROOT}/ext/boost.1.69.0")
    includes.from("${GEDLIB_ROOT}/ext/eigen.3.3.4/Eigen")
    includes.from("${GEDLIB_ROOT}/ext/nomad.3.8.1/src")
    includes.from("${GEDLIB_ROOT}/ext/nomad.3.8.1/ext/sgtelib/src")
    includes.from("${GEDLIB_ROOT}/ext/lsape.5/include")
    includes.from("${GEDLIB_ROOT}/ext/lsape.5/cpp/include")
    includes.from("${GEDLIB_ROOT}/ext/libsvm.3.22")
    includes.from("${GEDLIB_ROOT}/ext/fann.2.2.0/include")
}

tasks.withType<LinkSharedLibrary> {
    // libs.from("${GEDLIB_ROOT}/ext/fann.2.2.0/lib/libdoublefann.2.dylib")
    libs.from("${GEDLIB_ROOT}/ext/libsvm.3.22/libsvm.so")
    libs.from("${GEDLIB_ROOT}/ext/nomad.3.8.1/lib/libnomad.so")
    linkerArgs.addAll(listOf(
        "-L${GEDLIB_ROOT}/lib",
        "-lgxlgedlib"
    ))
}

library {
    linkage.set(listOf(Linkage.SHARED))
    targetMachines.addAll(
        machines.windows.x86_64,
        machines.linux.x86_64,
    )
    baseName.set("ged4jni")
    binaries.configureEach(CppSharedLibrary::class) {
        compileTask.get().compilerArgs.addAll(listOf("-std=c++11"))
    }
}
