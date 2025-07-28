package io.github.xyzboom.gedlib

/**
 * Provides the API of GEDLIB.
 * As this is a jni wrapper for the original GEDEnv,
 * only one object will be created for one process.
 */
class GEDEnv private constructor() {
    @Suppress("unused")
    private val nativePointer: Long = 0L

    companion object {
        init {
            System.loadLibrary("ged4jni")
        }
        private var env: GEDEnv = GEDEnv().apply { init() }
    }

    private external fun init()
}