cmake_minimum_required(VERSION 3.31)
project(Ged4CLF)

set(CMAKE_CXX_STANDARD 11)

# Define a cache variable (can be passed via -D flag)
set(GEDLIB_ROOT "" CACHE PATH "Root directory path of the GEDLib project")
set(JAVA_HOME "" CACHE PATH "Java Home")

# Validate the variable
if(NOT GEDLIB_ROOT)
    message(FATAL_ERROR "Please specify the path using -DGEDLIB_ROOT=<path>")
endif()

if(NOT IS_DIRECTORY ${GEDLIB_ROOT})
    message(FATAL_ERROR "Invalid directory: ${GEDLIB_ROOT}")
endif()

# Validate the variable
if(NOT JAVA_HOME)
    message(FATAL_ERROR "Please specify the path using -JAVA_HOME=<path>")
endif()

if(NOT IS_DIRECTORY ${JAVA_HOME})
    message(FATAL_ERROR "Invalid directory: ${JAVA_HOME}")
endif()

include_directories(${GEDLIB_ROOT})
include_directories(${GEDLIB_ROOT}/ext/boost.1.69.0)
include_directories(${GEDLIB_ROOT}/ext/eigen.3.3.4/Eigen)
include_directories(${GEDLIB_ROOT}/ext/nomad.3.8.1/src)
include_directories(${GEDLIB_ROOT}/ext/nomad.3.8.1/ext/sgtelib/src)
include_directories(${GEDLIB_ROOT}/ext/lsape.5/include)
include_directories(${GEDLIB_ROOT}/ext/lsape.5/cpp/include)
include_directories(${GEDLIB_ROOT}/ext/libsvm.3.22)
include_directories(${GEDLIB_ROOT}/ext/fann.2.2.0/include)
include_directories(${JAVA_HOME}/include)
include_directories(${JAVA_HOME}/include/linux)
link_directories(${GEDLIB_ROOT}/ext/nomad.3.8.1/lib)
link_directories(${GEDLIB_ROOT}/ext/libsvm.3.22)
link_directories(${GEDLIB_ROOT}/ext/fann.2.2.0/lib)
include_directories(src/main/headers)
#target_link_libraries(your_target gedlib)

add_library(Ged4CLF SHARED
        src/main/cpp/GEDLIB.cpp
        src/main/cpp/utils.cpp
)
#target_link_libraries(Ged4CLF PRIVATE ${GEDLIB_ROOT}/ext/fann.2.2.0/lib/libdoublefann.2.dylib)
target_link_libraries(Ged4CLF PRIVATE ${GEDLIB_ROOT}/ext/libsvm.3.22/libsvm.so)
target_link_libraries(Ged4CLF PRIVATE ${GEDLIB_ROOT}/ext/nomad.3.8.1/lib/libnomad.so)