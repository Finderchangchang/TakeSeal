package dw.take.seal.utils

import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.IOException

/**
 * Created by lenovo on 2017/7/16.
 */
class ImgUtils{
    /***
     * bitmap转成Base64
     */
    fun bitmapToBase64(bitmap: Bitmap?): String {
        var bitmap = bitmap
        var result: String? = null
        var baos: ByteArrayOutputStream? = null
        try {
            if (bitmap != null) {
                bitmap = rotaingImageView(90, bitmap)
                baos = ByteArrayOutputStream()
                var options = 100
                bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos)
                while (baos.toByteArray().size / 1024 > 1324) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
                    baos.reset()//重置baos即清空baos
                    bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos)//这里压缩options%，把压缩后的数据存放到baos中
                    options -= 10//每次都减少10
                }
                baos.flush()
                baos.close()

                val bitmapBytes = baos.toByteArray()
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                if (baos != null) {
                    baos.flush()
                    baos.close()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        return result!!
    }

    /***
     * 如果图片不正，进行旋转
     */
    fun rotaingImageView(angle: Int, bitmap: Bitmap): Bitmap {
        //旋转图片 动作
        val matrix = Matrix()
        matrix.postRotate(angle.toFloat())
        println("angle2=" + angle)
        // 创建新的图片
        val resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.width, bitmap.height, matrix, true)
        return resizedBitmap
    }


}