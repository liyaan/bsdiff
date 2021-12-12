package com.liyaan.bsdiff

import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import java.io.File
import java.io.IOException


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        PermissionUtils.isGrantExternalRW(this,0)
        //原生安卓11有时候获取读取权限还是不能操作sd卡 必须打开设置允许所有的对sd卡权限
        val intent =  Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent)
        findViewById<TextView>(R.id.sample_text).text = BuildConfig.VERSION_NAME
    }
    fun updateApp(view: View){
        object:AsyncTask<Void,Void,File>(){
            override fun doInBackground(vararg params: Void?): File? {
// 获取旧版本路径（正在运行的apk路径）
//                String oldApk = getApplicationInfo().sourceDir;

                // 获取旧版本路径（正在运行的apk路径）
//                String oldApk = getApplicationInfo().sourceDir;
                val patch: String =
                    File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path, "patch")
                        .absolutePath
                val oldApk: String =
                    File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path, "old.apk")
                        .absolutePath
                val output: String = createNewApk()!!.absolutePath
                Log.e("oldApk---->>", oldApk)
                Log.e("patch----->>", patch)
                Log.e("output---->>", output)
                if (!File(patch).exists()) {
                    Log.i(":aa","aaaaaaaaa")
                    return null
                }
                BsPatcher().bsPatcher(oldApk, patch, output)
                return File(output)
            }

            override fun onPostExecute(file: File?) {
                super.onPostExecute(file)
                // 已经合成了，调用该方法，安装新版本apk
                if (file != null) {
                    if (!file.exists()) return
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        val fileUri: Uri = FileProvider.getUriForFile(
                            this@MainActivity,
                            this@MainActivity.applicationInfo.packageName + ".fileprovider",
                            file
                        )
                        intent.setDataAndType(fileUri, "application/vnd.android.package-archive")
                    } else {
                        intent.setDataAndType(
                            Uri.fromFile(file),
                            "application/vnd.android.package-archive"
                        )
                    }
                    this@MainActivity.startActivity(intent)
                } else {
                    Toast.makeText(this@MainActivity, "差分包不存在！", Toast.LENGTH_LONG).show()
                }
            }
        }.execute()
    }

    /**
     * 创建合成后的新版本apk文件
     *
     * @return
     */
    private fun createNewApk(): File? {
        val newApk =
            File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path, "bsdiffOne.apk")
        if (!newApk.exists()) {
            try {
                newApk.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return newApk
    }
}