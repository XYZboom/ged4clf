plugins {
    `cpp-library`
}

group = "io.github.xyzboom"
version = "1.0-SNAPSHOT"

@Suppress("PropertyName")
val GEDLIB_ROOT = (System.getenv("GEDLIB_ROOT") ?: project.properties["GEDLIB_ROOT"]).apply {
    if (this == null) {
        throw GradleException("GEDLIB_ROOT not found in environment or property.")
    }
    this as String
}

tasks.withType<CppCompile> {
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
}

library {
    targetMachines.addAll(
        machines.windows.x86_64,
        machines.linux.x86_64,
    )
    baseName.set("ged4jni")
    binaries.configureEach(CppSharedLibrary::class) {
        compileTask.get().compilerArgs.addAll(listOf("-std=c++11"))
    }
}
