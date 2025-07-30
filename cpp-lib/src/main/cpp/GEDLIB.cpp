#define GXL_GEDLIB_SHARED
// ReSharper disable once CppUnusedIncludeDirective
#include "src/env/ged_env.hpp"
#include "jni.h"
#include "utils.h"

typedef ged::GEDEnv<ged::GXLNodeID, ged::GXLLabel, ged::GXLLabel> GEDEnv;

GEDEnv *toNativeEnv(JNIEnv *env, jobject obj) {
    jclass envClass = env->FindClass("io/github/xyzboom/gedlib/GEDEnv");
    if (envClass == nullptr) {
        return nullptr; // exception has been thrown
    }
    jfieldID nativePointerFieldId = env->GetFieldID(envClass, "nativePointer", "J");
    jlong nativePointer = env->GetLongField(obj, nativePointerFieldId);
    env->DeleteLocalRef(envClass);
    return reinterpret_cast<GEDEnv *>(nativePointer);
}

extern "C" JNIEXPORT void JNICALL
Java_io_github_xyzboom_gedlib_GEDEnv_init(JNIEnv *env, jobject obj) {
    jclass envClass = env->FindClass("io/github/xyzboom/gedlib/GEDEnv");
    if (envClass == nullptr) {
        return; // exception has been thrown
    }
    jfieldID nativePointerFieldId = env->GetFieldID(envClass, "nativePointer", "J");
    auto nativeEnv = new GEDEnv;
    nativeEnv->set_method(ged::Options::GEDMethod::BRANCH);
    env->SetLongField(obj, nativePointerFieldId, reinterpret_cast<jlong>(nativeEnv));
    env->DeleteLocalRef(obj);
}

extern "C" JNIEXPORT void JNICALL
Java_io_github_xyzboom_gedlib_GEDEnv_release(JNIEnv *env, jobject obj, jlong nativePointer) {
    delete reinterpret_cast<GEDEnv *>(nativePointer);
}

extern "C" JNIEXPORT jlong JNICALL
Java_io_github_xyzboom_gedlib_GEDEnv_addGraphNative(JNIEnv *env, jobject obj) {
    auto nativeEnv = toNativeEnv(env, obj);
    if (nativeEnv == nullptr) {
        return 0;
    }
    auto graphId = to_jlong(nativeEnv->add_graph());
    nativeEnv->set_edit_costs(ged::Options::EditCosts::CONSTANT);
    // call init as the document said
    nativeEnv->init();
    auto *graphNativePointer = new ged::GEDGraph(graphId);
    return reinterpret_cast<jlong>(graphNativePointer);
}

extern "C" JNIEXPORT void JNICALL
Java_io_github_xyzboom_gedlib_GEDEnv_clearGraphNative(JNIEnv *env, jobject obj, jlong gNativePointer) {
    const auto nativeEnv = toNativeEnv(env, obj);
    if (nativeEnv == nullptr) {
        return;
    }
    const auto *graphNativePointer = reinterpret_cast<ged::GEDGraph *>(gNativePointer);
    const std::size_t graph_id = graphNativePointer->id();
    nativeEnv->clear_graph(graph_id);
    // call init as the document said
    nativeEnv->init();
    delete graphNativePointer;
}

ged::GEDGraph *toNativeGraph(JNIEnv *env, jobject obj);

extern "C" JNIEXPORT jdouble JNICALL
Java_io_github_xyzboom_gedlib_GEDEnv_getLowerBoundNative(
    JNIEnv *env, jobject obj, jobject graph1, jobject graph2
) {
    const auto nativeEnv = toNativeEnv(env, obj);
    if (nativeEnv == nullptr) {
        return std::numeric_limits<double>::infinity();
    }
    const auto *gPointer = toNativeGraph(env, graph1);
    const auto *hPointer = toNativeGraph(env, graph2);
    const std::size_t gId = gPointer->id();
    const std::size_t hId = hPointer->id();
    if (!nativeEnv->initialized()) {
        nativeEnv->init();
    }
    nativeEnv->init_method();
    nativeEnv->run_method(gId, hId);
    return nativeEnv->get_lower_bound(gId, hId);
}

extern "C" JNIEXPORT jdouble JNICALL
Java_io_github_xyzboom_gedlib_GEDEnv_getUpperBoundNative(
    JNIEnv *env, jobject obj, jobject graph1, jobject graph2
) {
    const auto nativeEnv = toNativeEnv(env, obj);
    if (nativeEnv == nullptr) {
        return std::numeric_limits<double>::infinity();
    }
    const auto *gPointer = toNativeGraph(env, graph1);
    const auto *hPointer = toNativeGraph(env, graph2);
    const std::size_t gId = gPointer->id();
    const std::size_t hId = hPointer->id();
    if (!nativeEnv->initialized()) {
        nativeEnv->init();
    }
    nativeEnv->init_method();
    nativeEnv->run_method(gId, hId);
    return nativeEnv->get_upper_bound(gId, hId);
}


#ifndef GEDGRAPH
#define GEDGRAPH

ged::GEDGraph *toNativeGraph(JNIEnv *env, jobject obj) {
    jclass clazz = env->FindClass("io/github/xyzboom/gedlib/GEDGraph");
    if (clazz == nullptr) {
        return nullptr; // exception has been thrown
    }
    jfieldID nativePointerFieldId = env->GetFieldID(clazz, "nativePointer", "J");
    jlong nativePointer = env->GetLongField(obj, nativePointerFieldId);
    env->DeleteLocalRef(clazz);
    return reinterpret_cast<ged::GEDGraph *>(nativePointer);
}

extern "C" JNIEXPORT void JNICALL
Java_io_github_xyzboom_gedlib_GEDGraph_addNodeNative(
    JNIEnv *env, jobject obj, jobject jEnv, jstring nodeId, jobject nodeProperties
) {
    auto graph = toNativeGraph(env, obj);
    auto gId = graph->id();
    auto gEnv = toNativeEnv(env, jEnv);
    auto strNodeId = jstring_to_std(env, nodeId);
    auto mapNodeProperties = java_map_to_cpp_map(env, nodeProperties);
    gEnv->add_node(gId, strNodeId, mapNodeProperties);
}

extern "C" JNIEXPORT void JNICALL
Java_io_github_xyzboom_gedlib_GEDGraph_addEdgeNative(
    JNIEnv *env, jobject obj, jobject jEnv, jstring jFromId, jstring jToId,
    jobject jProperties, jboolean jIgnoreDuplicates
) {
    auto graph = toNativeGraph(env, obj);
    auto gId = graph->id();
    auto gEnv = toNativeEnv(env, jEnv);
    auto fromId = jstring_to_std(env, jFromId);
    auto toId = jstring_to_std(env, jToId);
    auto properties = java_map_to_cpp_map(env, jProperties);
    gEnv->add_edge(gId, fromId, toId, properties, jIgnoreDuplicates);
}

#endif // GEDGRAPH
