package io.github.xyzboom.gedlib

import java.lang.ref.Cleaner


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

        init {
            System.loadLibrary("ged4jni")
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