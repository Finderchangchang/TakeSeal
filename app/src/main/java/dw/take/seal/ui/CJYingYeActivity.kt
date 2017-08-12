package dw.take.seal.ui

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Camera
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import dw.take.seal.R
import dw.take.seal.control.ZJSBListener
import dw.take.seal.model.ApplySealCertificateData
import dw.take.seal.model.OrganizationJianModel
import dw.take.seal.utils.ImgUtils
import kotlinx.android.synthetic.main.ac_legal_person.*
import kotlinx.android.synthetic.main.activity_check_code.*
import kotlinx.android.synthetic.main.activity_cjying_ye.*
import wai.gr.cla.base.BaseActivity
import wai.gr.cla.method.Utils
import wai.gr.cla.model.key
import java.io.File

//营业执照采集
class CJYingYeActivity : BaseActivity() {
    var isPai: Boolean = false;
    var isfaren: Boolean = true
    var facemodel: ApplySealCertificateData = ApplySealCertificateData()
    override fun initEvents() {
        ying_iv_farenz.setOnClickListener {
            //拍照
            startActivityForResult(Intent(this, CameraPersonActivity::class.java), 12)
        }
        ying_next_btn.setOnClickListener {
            //下一步
            if (isPai) {
                facemodel.SealCertificateName = "营业执照"
                facemodel.SealCertificateType = "01"
                findb!!.deleteByWhere(ApplySealCertificateData::class.java, "SealCertificateType='01'")
                findb!!.save(facemodel)
                if (isfaren) {
                    startActivity(Intent(this, MainActivity::class.java).putExtra("isFaRen", false))
                } else {
                    startActivity(Intent(this, WTSActivity::class.java).putExtra("isFaRen", false))
                }
            } else {
                toast("请先上传营业执照照片")
            }
        }
        ying_close_btn.setOnClickListener {
            finish()
        }
    }

    override fun initViews() {
        setContentView(R.layout.activity_cjying_ye)
        isfaren = dw.take.seal.utils.Utils(this).ReadString(key.KEY_TAKESEAL_ISFAREN).equals("1")
        if (isfaren) {
            tv_yingye_title.text = "第六步"
        } else {
            tv_yingye_title.text = "第七步"
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 12 && resultCode == 12) {
            var mypath = data!!.getStringExtra("PATH")
            var photo = Utils.getimage(100, mypath.toString())
            //val zhengbm = Utils.centerSquareScaleBitmap(photo, 100)
            photo = Utils.rotaingImageView(90, photo)
            ying_iv_farenz!!.setImageBitmap(photo)
            facemodel.SealCertificateImage = ImgUtils().bitmapToBase64(Utils.rotaingImageView(90, photo))
            isPai = true
        }
    }

    override fun onDestroy() {
        findb!!.deleteByWhere(ApplySealCertificateData::class.java, "SealCertificateType='01'")
        super.onDestroy()
    }
}
