
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.Log
import com.example.ripcurrent.R
import org.tensorflow.lite.Interpreter
import java.nio.ByteBuffer
import java.nio.ByteOrder

class ImageClassifier(private val context: Context, modelPath: String) {
    private var interpreter: Interpreter

    init {
        // 載入 .tflite 模型
        val assetFileDescriptor = context.assets.openFd(modelPath)
        val inputStream = assetFileDescriptor.createInputStream()
        val modelData = inputStream.readBytes()
        val buffer = ByteBuffer.allocateDirect(modelData.size).apply {
            order(ByteOrder.nativeOrder())
            put(modelData)
        }
        interpreter = Interpreter(buffer)
    }

    fun classify(image: Bitmap): Boolean {
        val inputImage = Bitmap.createScaledBitmap(image, 224, 224, true)
        val inputBuffer = ByteBuffer.allocateDirect(4 * 224 * 224 * 3).apply {
            order(ByteOrder.nativeOrder())
        }
        for (y in 0 until 224) {
            for (x in 0 until 224) {
                val pixel = inputImage.getPixel(x, y)
                inputBuffer.putFloat((pixel shr 16 and 0xFF) / 255.0f) // R
                inputBuffer.putFloat((pixel shr 8 and 0xFF) / 255.0f)  // G
                inputBuffer.putFloat((pixel and 0xFF) / 255.0f)         // B
            }
        }

        // 準備輸出緩衝區
        val outputBuffer = Array(1) { FloatArray(10) } // 假設模型有10個類別
        interpreter.run(inputBuffer, outputBuffer)

        // 設定信心閾值
        val confidenceThreshold = 0.6f

        // 獲取所有類別的信心值並打印
        val confidences = outputBuffer[0]
        Log.i("ImageClassifier", "Confidence scores: ${confidences.contentToString()}")

        // 找到最大信心值和對應的類別索引
        val maxConfidence = confidences.maxOrNull() ?: 0f
        val maxIndex = confidences.indexOfFirst { it == maxConfidence } // 更新為使用 indexOfFirst

        // 日誌中顯示分類結果
        Log.i("ImageClassifier", "Highest confidence class index: $maxIndex with score: $maxConfidence")

        // 檢查是否超過閾值
        return maxConfidence > confidenceThreshold
    }

    fun classifyAndMark(image: Bitmap): Bitmap {
        val inputImage = Bitmap.createScaledBitmap(image, 224, 224, true)
        val inputBuffer = ByteBuffer.allocateDirect(4 * 224 * 224 * 3).apply {
            order(ByteOrder.nativeOrder())
        }
        for (y in 0 until 224) {
            for (x in 0 until 224) {
                val pixel = inputImage.getPixel(x, y)
                inputBuffer.putFloat((pixel shr 16 and 0xFF) / 255.0f) // R
                inputBuffer.putFloat((pixel shr 8 and 0xFF) / 255.0f)  // G
                inputBuffer.putFloat((pixel and 0xFF) / 255.0f)         // B
            }
        }

        // 準備輸出緩衝區
        val outputBuffer = Array(1) { FloatArray(10) } // 假設模型有10個類別
        interpreter.run(inputBuffer, outputBuffer)

        // 設定信心閾值
        val confidenceThreshold = 0.6f

        // 獲取最大信心值
        val confidences = outputBuffer[0]
        val maxConfidence = confidences.maxOrNull() ?: 0f

        // 如果檢測到目標（超過信心閾值），則在圖像上進行標記
        val markedBitmap = image.copy(Bitmap.Config.ARGB_8888, true) // 複製為可編輯的Bitmap
        if (maxConfidence > confidenceThreshold) {
            val canvas = android.graphics.Canvas(markedBitmap)
            val paint = Paint().apply {
                color = Color.RED // 邊界框顏色
                strokeWidth = 5f // 邊界框線條粗細
                style = Paint.Style.STROKE // 繪製外框
            }

            // 繪製邊界框（在此示例中，邊界框固定在圖像中央）
            val rect = Rect(
                (image.width * 0.3).toInt(), (image.height * 0.3).toInt(),
                (image.width * 0.7).toInt(), (image.height * 0.7).toInt()
            )
            canvas.drawRect(rect, paint)

            // 加上文字標記
            paint.style = Paint.Style.FILL
            paint.textSize = 40f
            canvas.drawText(context.getText(R.string.rip_current).toString(), rect.left.toFloat(), rect.top - 10f, paint)
        }

        return markedBitmap // 返回標記過的圖像
    }


}
