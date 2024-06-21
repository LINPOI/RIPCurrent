package com.example.ripcurrent.tool.zipTool

import android.content.Context
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

//fun unzip(zipFilePath: String, targetDirectory: String) {
//    val buffer = ByteArray(1024)
//    try {
//        val inputStream = FileInputStream(zipFilePath)
//        val zipInputStream = ZipInputStream(BufferedInputStream(inputStream))
//        var zipEntry: ZipEntry? = zipInputStream.nextEntry
//
//        while (zipEntry != null) {
//            val fileName = zipEntry.name
//            val newFile = File(targetDirectory, fileName)
//
//            if (zipEntry.isDirectory) {
//                newFile.mkdirs()
//            } else {
//                // Create parent directories if needed
//                File(newFile.parent).mkdirs()
//
//                val fileOutputStream = FileOutputStream(newFile)
//                var len: Int
//                while (zipInputStream.read(buffer).also { len = it } > 0) {
//                    fileOutputStream.write(buffer, 0, len)
//                }
//                fileOutputStream.close()
//            }
//
//            zipInputStream.closeEntry()
//            zipEntry = zipInputStream.nextEntry
//        }
//
//        zipInputStream.close()
//        inputStream.close()
//    } catch (e: IOException) {
//        e.printStackTrace()
//    }
//}
fun unzip(zipFilePath: String, destDirectory: String) {
    val destDir = File(destDirectory)
    if (!destDir.exists()) destDir.mkdirs()

    ZipInputStream(FileInputStream(zipFilePath)).use { zis ->
        var zipEntry: ZipEntry? = zis.nextEntry
        while (zipEntry != null) {
            val newFile = File(destDirectory, zipEntry.name)
            if (zipEntry.isDirectory) {
                newFile.mkdirs()
            } else {
                // Ensure parent directories exist
                newFile.parentFile?.mkdirs()
                FileOutputStream(newFile).use { fos ->
                    val buffer = ByteArray(4096)
                    var len: Int
                    while (zis.read(buffer).also { len = it } > 0) {
                        fos.write(buffer, 0, len)
                    }
                }
            }
            zipEntry = zis.nextEntry
        }
        zis.closeEntry()
    }
}
fun listFilesInDirectory(context: Context): List<File> {
    val destDirectory = File(context.filesDir, "unzipped").absolutePath
    val directory = File(destDirectory)
    if (!directory.exists() || !directory.isDirectory) {
        return emptyList()
    }
    return directory.listFiles()?.toList() ?: emptyList()
}