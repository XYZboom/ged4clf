#include "utils.h"

jlong to_jlong(unsigned long num) {
    auto *pNum = &num;
    const auto *pJLong = reinterpret_cast<jlong *>(pNum);
    return *pJLong;
}

std::string jstring_to_std(JNIEnv *env, jstring jstr) {
    const char *cstr = env->GetStringUTFChars(jstr, nullptr);
    std::string std_str(cstr ? cstr : "");
    env->ReleaseStringUTFChars(jstr, cstr);
    return std_str;
}

std::map<std::string, std::string> java_map_to_cpp_map(JNIEnv *env, jobject javaMap) {
    std::map<std::string, std::string> cpp_map;

    jclass mapClass = env->GetObjectClass(javaMap);
    jmethodID entrySet = env->GetMethodID(mapClass, "entrySet", "()Ljava/util/Set;");
    jobject set = env->CallObjectMethod(javaMap, entrySet);

    jclass setClass = env->FindClass("java/util/Set");
    jmethodID iterator = env->GetMethodID(setClass, "iterator", "()Ljava/util/Iterator;");
    jobject iter = env->CallObjectMethod(set, iterator);

    jclass iteratorClass = env->FindClass("java/util/Iterator");
    jmethodID hasNext = env->GetMethodID(iteratorClass, "hasNext", "()Z");
    jmethodID next = env->GetMethodID(iteratorClass, "next", "()Ljava/lang/Object;");

    jclass entryClass = env->FindClass("java/util/Map$Entry");
    jmethodID getKey = env->GetMethodID(entryClass, "getKey", "()Ljava/lang/Object;");
    jmethodID getValue = env->GetMethodID(entryClass, "getValue", "()Ljava/lang/Object;");

    while (env->CallBooleanMethod(iter, hasNext)) {
        jobject entry = env->CallObjectMethod(iter, next);
        jstring jKey = (jstring) env->CallObjectMethod(entry, getKey);
        jstring jValue = (jstring) env->CallObjectMethod(entry, getValue);

        cpp_map[jstring_to_std(env, jKey)] = jstring_to_std(env, jValue);

        env->DeleteLocalRef(entry);
        env->DeleteLocalRef(jKey);
        env->DeleteLocalRef(jValue);
    }
    return cpp_map;
}