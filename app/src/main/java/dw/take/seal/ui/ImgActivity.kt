package dw.take.seal.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import dw.take.seal.R
import kotlinx.android.synthetic.main.activity_img.*

class ImgActivity : AppCompatActivity() {
    var url=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_img)
        iv.setImageBitmap(base64ToBitmap(intent.getStringExtra("url")))
    }

    fun base64ToBitmap(base64Data: String): Bitmap {
        val bytes = Base64.decode(base64Data, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }
}
