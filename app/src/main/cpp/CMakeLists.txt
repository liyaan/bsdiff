cmake_minimum_required(VERSION 3.10.2)

file(GLOB bzip_source ${CMAKE_SOURCE_DIR}/bzip/*.c)
project("bsdiff")


add_library(
             native-lib
             SHARED
             native-lib.cpp
        bspatch.c
        ${bzip_source})
#include_directories(/bzip) #include <bzlib.h>
find_library(
              log-lib
              log )


target_link_libraries(
                       native-lib
                       ${log-lib} )