package dw.take.seal.ui

import android.content.Intent
import dw.take.seal.R
import kotlinx.android.synthetic.main.activity_wts.*
import wai.gr.cla.base.BaseActivity
import wai.gr.cla.method.Utils

//委托书采集
class WTSActivity : BaseActivity() {
    var isSuccess: Boolean = false

    override fun initViews() {
        setContentView(R.layout.activity_wts)
    }

    override fun initEvents() {
        wts_iv_farenz.setOnClickListener {
            //拍照
            startActivityForResult(Intent(this, CameraPersonActivity::class.java), 12)
        }
        wts_close_btn.setOnClickListener {
            finish()
        }
        wts_next_btn.setOnClickListener {
            if (isSuccess) {
                val intent = Intent(this@WTSActivity, MainActivity::class.java)
                startActivity(intent)
            } else {
                toast("请上传您的委托书照片")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 12 && resultCode == 12) {
            var mypath = data!!.getStringExtra("PATH")
            val photo = Utils.getimage(200, mypath.toString())
            //val zhengbm = Utils.centerSquareScaleBitmap(photo, 100)
            wts_iv_farenz!!.setImageBitmap(photo)
        }
    }


}
