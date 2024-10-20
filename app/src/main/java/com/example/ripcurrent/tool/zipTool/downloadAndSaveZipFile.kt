package com.example.ripcurrent.tool.zipTool

//fun downloadAndSaveZipFile(context: Context) {
//    CoroutineScope(Dispatchers.IO).launch {
//        try {
//            val response = Retrofit.apiService.getImages()
//            if (response.isSuccessful) {
//                val body = response.body()
//                if (body != null) {
//                    saveToFile(body, context.filesDir)
//                    Log.d("linpoi", "ZIP file downloaded successfully")
//                    //解壓縮
//                    val zipFilePath = File(context.filesDir, "AllImage").absolutePath
//                    val destDirectory = File(context.filesDir, "unzipped").absolutePath
//                    unzip(zipFilePath, destDirectory)
//                }
//            } else {
//                Log.e("linpoi", "Failed: ${response.errorBody()?.string()}")
//            }
//        } catch (e: Exception) {
//            Log.e("linpoi", "Exception: ${e.message}")
//        }
//    }
//}
//fun saveToFile(body: ResponseBody, directory: File) {
//    try {
//        val file = File(directory, "AllImage")
//        var inputStream: InputStream? = null
//        var outputStream: FileOutputStream? = null
//
//        try {
//            inputStream = body.byteStream()
//            outputStream = FileOutputStream(file)
//            val buffer = ByteArray(4096)
//            var bytesRead: Int
//
//            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
//                outputStream.write(buffer, 0, bytesRead)
//            }
//            outputStream.flush()
//        } finally {
//            inputStream?.close()
//            outputStream?.close()
//        }
//    } catch (e: Exception) {
//        Log.e("SaveToFile", "Exception: ${e.message}")
//    }
//}