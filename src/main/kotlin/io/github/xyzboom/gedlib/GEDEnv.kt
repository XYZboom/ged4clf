package io.github.xyzboom.gedlib

import java.io.File
import java.lang.ref.Cleaner
import kotlin.io.path.absolutePathString
import kotlin.io.path.createTempDirectory


/**
 * Provides the API of GEDLIB.
 */
class GEDEnv {
    @Suppress("unused")
    private val nativePointer: Long = 0L

    init {
        init()
        // avoid to catch `this` in lambda
        val nativePointer = this.nativePointer
        cleaner.register(this) {
            release(nativePointer)
        }
    }

    companion object {
        private val cleaner = Cleaner.create()

        private val libNames = listOf(
            "libdoublefann.so.2",
            "libged4jni.so",
            "libgxlgedlib.so",
            "libnomad.so",
            "libsgtelib.so",
            "libsvm.so",
        )

        init {
            prepareDynamicLib()
        }

        private fun prepareDynamicLib() {
            val os = System.getProperty("os.name").lowercase()

            when {
                "linux" in os -> "linux"
                "win" in os -> "win"
                else -> throw RuntimeException("Unsupported OS: $os")
            }

            val loader = this::class.java.classLoader
            val tempDirectory = createTempDirectory(prefix = "gedlib")
            File(tempDirectory.absolutePathString()).deleteOnExit()
            for (libName in libNames) {
                val resource = loader.getResourceAsStream("gedlib/$os/$libName")!!
                val tempFile = File(tempDirectory.resolve(libName).absolutePathString())
                tempFile.outputStream().use { outputStream ->
                    resource.copyTo(outputStream)
                }
            }
            System.load(tempDirectory.resolve("libged4jni.so").absolutePathString())
        }

        @JvmStatic
        private external fun release(nativePointer: Long)
    }

    private external fun init()
    private external fun addGraphNative(): Long /*(ged::GEDGraph *)*/
    private external fun clearGraphNative(graphPointer: Long)
    private external fun getLowerBoundNative(graph1: GEDGraph, graph2: GEDGraph): Double
    private external fun getUpperBoundNative(graph1: GEDGraph, graph2: GEDGraph): Double

    fun addGraph(): GEDGraph {
        val graphPointer = addGraphNative()
        return GEDGraph(graphPointer, this).apply {
            cleaner.register(this) {
                clearGraphNative(graphPointer)
            }
        }
    }

    fun getLowerBound(graph1: GEDGraph, graph2: GEDGraph): Double {
        return getLowerBoundNative(graph1, graph2)
    }

    fun getUpperBound(graph1: GEDGraph, graph2: GEDGraph): Double {
        return getUpperBoundNative(graph1, graph2)
    }
}