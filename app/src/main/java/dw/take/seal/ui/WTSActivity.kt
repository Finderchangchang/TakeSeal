package dw.take.seal.ui

import android.content.Intent
import dw.take.seal.R
import dw.take.seal.model.ApplySealCertificateData
import dw.take.seal.utils.ImgUtils
import kotlinx.android.synthetic.main.activity_cjying_ye.*
import kotlinx.android.synthetic.main.activity_wts.*
import wai.gr.cla.base.BaseActivity
import wai.gr.cla.method.Utils
import wai.gr.cla.model.key

//委托书采集
class WTSActivity : BaseActivity() {
    var isSuccess: Boolean = false

    var facemodel: ApplySealCertificateData = ApplySealCertificateData()
    override fun initViews() {
        setContentView(R.layout.activity_wts)
        facemodel.SealCertificateType="04"
        facemodel.SealCertificateName="委托书（法人/乡政府委托书/首席代表）"

    }

    override fun initEvents() {
        wts_iv_farenz.setOnClickListener {
            //拍照
            startActivityForResult(Intent(this, CameraPersonActivity::class.java), 12)
        }
        wts_close_btn.setOnClickListener {
            findb!!.deleteByWhere(ApplySealCertificateData::class.java,"SealCertificateType='04'")
            finish()
        }
        wts_next_btn.setOnClickListener {
            if (isSuccess) {
                findb!!.save(facemodel)
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
            var photo = Utils.getimage(100, mypath.toString())
            //val zhengbm = Utils.centerSquareScaleBitmap(photo, 100)
            photo = Utils.rotaingImageView(90, photo)
            wts_iv_farenz!!.setImageBitmap(photo)
            facemodel.SealCertificateImageString= ImgUtils().bitmapToBase64(Utils.rotaingImageView(-90, photo))
            isSuccess=true
        }
    }


}
