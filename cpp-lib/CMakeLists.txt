cmake_minimum_required(VERSION 3.31)
project(Ged4CLF)

set(CMAKE_CXX_STANDARD 11)

# Define a cache variable (can be passed via -D flag)
set(GEDLIB_ROOT "" CACHE PATH "Root directory path of the GEDLib project")

# Validate the variable
if(NOT GEDLIB_ROOT)
    message(FATAL_ERROR "Please specify the path using -DGEDLIB_ROOT=<path>")
endif()

if(NOT IS_DIRECTORY ${GEDLIB_ROOT})
    message(FATAL_ERROR "Invalid directory: ${GEDLIB_ROOT}")
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
link_directories(${GEDLIB_ROOT}/ext/nomad.3.8.1/lib)
link_directories(${GEDLIB_ROOT}/ext/libsvm.3.22)
link_directories(${GEDLIB_ROOT}/ext/fann.2.2.0/lib)
#target_link_libraries(your_target gedlib)

include_directories(include)
include_directories(include/linux)
add_library(Ged4CLF SHARED library.cpp)
#target_link_libraries(Ged4CLF PRIVATE ${GEDLIB_ROOT}/ext/fann.2.2.0/lib/libdoublefann.2.dylib)
target_link_libraries(Ged4CLF PRIVATE ${GEDLIB_ROOT}/ext/libsvm.3.22/libsvm.so)
target_link_libraries(Ged4CLF PRIVATE ${GEDLIB_ROOT}/ext/nomad.3.8.1/lib/libnomad.so)