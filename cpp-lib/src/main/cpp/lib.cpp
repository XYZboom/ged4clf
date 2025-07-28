
#include <iostream>
#include "jni.h"
#define GXL_GEDLIB_SHARED
#include "src/env/ged_env.hpp"

extern "C" JNIEXPORT void JNICALL Java_io_github_xyzboom_gedlib_GEDEnv_init(JNIEnv *env, jobject obj) {
    jclass envClass = env->FindClass("io/github/xyzboom/gedlib/GEDEnv");
    if (envClass == nullptr) {
        return; // exception has been thrown
    }
    jfieldID nativePointerFieldId = env->GetFieldID(envClass, "nativePointer", "J");
    auto nativeEnv = new ged::GEDEnv<ged::GXLNodeID, ged::GXLLabel, ged::GXLLabel>;
    env->SetLongField(obj, nativePointerFieldId, reinterpret_cast<jlong>(nativeEnv));
    env->DeleteLocalRef(envClass);
}

void hello() {
    std::cout << "Hello, World!" << std::endl;
}