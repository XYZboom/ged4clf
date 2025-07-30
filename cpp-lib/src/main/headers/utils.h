#ifndef UTILS_H
#define UTILS_H

#include "jni.h"
#include <map>
#include <string>

jlong to_jlong(unsigned long num);
std::string jstring_to_std(JNIEnv *env, jstring jstr);
std::map<std::string, std::string> java_map_to_cpp_map(JNIEnv *env, jobject javaMap);
#endif //UTILS_H
