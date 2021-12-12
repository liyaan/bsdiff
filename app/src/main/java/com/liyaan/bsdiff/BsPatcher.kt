package com.liyaan.bsdiff

class BsPatcher {
    companion object {
        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }
    }
    external fun stringFromJNI(): String
    external fun bsPatcher(oldApk:String,patch:String,output:String)
}