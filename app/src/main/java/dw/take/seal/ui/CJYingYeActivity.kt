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
import dw.take.seal.model.OrganizationJianModel
import dw.take.seal.utils.ImgUtils
import kotlinx.android.synthetic.main.ac_legal_person.*
import kotlinx.android.synthetic.main.activity_cjying_ye.*
import wai.gr.cla.base.BaseActivity
import wai.gr.cla.method.Utils
import java.io.File

class CJYingYeActivity : BaseActivity() {
    private var path = StringBuffer()
    var orgModel: OrganizationJianModel? = null
    var isfaren: Int = 0  //0法人1经办人2
    var isPai: Boolean = false;
    override fun initEvents() {

        ying_iv_farenz.setOnClickListener {
            //拍照
            startCamera(11)
        }
        ying_next_btn.setOnClickListener {
            //下一步
            if (isPai) {
                startActivity(Intent(this, StepThreeActivity::class.java).putExtra("isFaRen", false))
            } else {
                toast("请先上传营业执照照片")
            }
        }
        ying_close_btn.setOnClickListener { finish() }
    }

    override fun initViews() {
        setContentView(R.layout.activity_cjying_ye)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 12 && resultCode == 12) {
            var mypath = data!!.getStringExtra("PATH")
            var photo = Utils.getimage(200, mypath.toString())
            //val zhengbm = Utils.centerSquareScaleBitmap(photo, 100)
            photo = Utils.rotaingImageView(-90, photo)
            ying_iv_farenz!!.setImageBitmap(photo)
            isPai = true
        }
    }

    //开启拍照
    fun startCamera(type: Int) {
        // 利用系统自带的相机应用:拍照
        val pm = packageManager
        val hasACamera = pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)
                || pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)
                || Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD
                || Camera.getNumberOfCameras() > 0
        if (hasACamera) {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            path = StringBuffer()
            path.append(this@CJYingYeActivity.getExternalFilesDir(null)).append("/header3.jpg")
            val file = File(path.toString())
            val uri = Uri.fromFile(file)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
            startActivityForResult(intent, type)
        } else {
            Toast.makeText(this@CJYingYeActivity, "请检查相机功能是否正常!", Toast.LENGTH_SHORT).show()
        }
    }
}
