package dw.take.seal.ui

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Camera
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import com.bumptech.glide.util.Util
import com.nanchen.compresshelper.CompressHelper
import dw.take.seal.R
import dw.take.seal.control.FaceListener
import dw.take.seal.control.FaceView
import dw.take.seal.model.ApplySealCertificateData
import dw.take.seal.model.CardInfoModel
import dw.take.seal.model.OrganizationJianModel
import dw.take.seal.utils.ImgUtils
import kotlinx.android.synthetic.main.activity_face.*
import net.tsz.afinal.view.LoadingDialog
import org.jetbrains.annotations.Mutable
import wai.gr.cla.base.BaseActivity
import wai.gr.cla.method.Utils
import wai.gr.cla.model.key
import java.io.File
import java.util.*

/**
 * 自拍照片
 * Created by Administrator on 2017/8/3.
 */
class FaceActivity : BaseActivity(), FaceView {
    //图片保存路径
    var path = StringBuffer()
    var isSuccess: Boolean = false;
    var facemodel: ApplySealCertificateData = ApplySealCertificateData()
    override fun face_result(result: Boolean, mes: String) {

        if (pdialog != null) {
            pdialog!!.dismiss()
        }
        if(result){
            dw.take.seal.utils.Utils(this).WriteString(key.KEY_TAKESEAL_XSD,mes)
        }else{
            dw.take.seal.utils.Utils(this).WriteString(key.KEY_TAKESEAL_XSD,"")
        }
        isSuccess = result
        face_tv_name.visibility = View.VISIBLE
        face_tv_name.text = mes
    }

    var orgModel: OrganizationJianModel? = null
    var isfaren: Boolean = true
    var pdialog: ProgressDialog? = null
    var carInfofa: CardInfoModel? = null
    override fun initEvents() {
        facemodel.SealCertificateName = face_tv_title.text.toString().trim()
        facemodel.SealCertificateType = ""
        btn_face_upload_faren.setOnClickListener {
            //            //拍照
//            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//            intent.putExtra("android.intent.extras.CAMERA_FACING", 1)
//            // 调用前置摄像头
//            startActivityForResult(intent, CAMERA_REQUEST)
            //startCamera(11)
            startActivityForResult(Intent(this, FaceCameraActivity::class.java), 12)
        }
        face_next_btn.setOnClickListener {

            //手机验证码
            if (isSuccess) {
                startActivity(Intent(this, MySealActivity::class.java).putExtra("isFaRen", false).putExtra("OrgModel", orgModel))
            } else {
                toast("请先上传您的自拍照片,验证通过才可进入下一步")
            }
        }
        face_close_btn.setOnClickListener {
            finish()
        }

    }

    override fun initViews() {
        setContentView(R.layout.activity_face)
        isfaren = dw.take.seal.utils.Utils(this).ReadString(key.KEY_TAKESEAL_ISFAREN).equals("1")
        carInfofa = intent.getSerializableExtra("CardInfo") as CardInfoModel?
        var list: MutableList<CardInfoModel>
        if (isfaren) {
            list = findb!!.findAllByWhere(CardInfoModel::class.java, "isfaren=true")
            face_tv_title.text = "法人自拍"
            face_tv_title1.text="第四步"
        } else {
            face_tv_title.text = "经办人自拍"
            face_tv_title1.text="第五步"
            list = findb!!.findAllByWhere(CardInfoModel::class.java, "isfaren=false")

        }

        if (list.size > 0) {
            carInfofa = list[0]
        } else {
            carInfofa = null
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 12 && resultCode == 12) {
            var mmypath = data!!.getStringExtra("PATH")
            pdialog = LoadingDialog(this);
            pdialog!!.show();
            // val newFile = CompressHelper.getDefault(this).compressToFile(File(mmypath))
            var bm = dw.take.seal.utils.Utils.getimage(this, mmypath)
//            var bos = BufferedOutputStream(FileOutputStream(newFile))
//            bm.compress(Bitmap.CompressFormat.JPEG, 100, bos)
//            bos.flush()
//            bos.close()
            // var jiaodu = Utils.readPictureDegree(mmypath)
            bm = Utils.rotaingImageView(-90, bm)
            face_iv_farenz!!.setImageBitmap(bm)
            facemodel.SealCertificateImageString = ImgUtils().bitmapToBase64(bm!!)
            if (carInfofa != null) {
                FaceListener().card_fackRecognition_img(facemodel.SealCertificateImageString!!, carInfofa!!.personFaceImage, this)
            } else {
                if (pdialog != null) {
                    pdialog!!.dismiss()
                }
                toast("已超时，请重新登录")
                finish()
            }
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
            path.append(this@FaceActivity.getExternalFilesDir(null)).append("/header1.jpg")
            val file = File(path.toString())
            val uri = Uri.fromFile(file)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
            startActivityForResult(intent, type)
        } else {
            Toast.makeText(this@FaceActivity, "请检查相机功能是否正常!", Toast.LENGTH_SHORT).show()
        }
    }

}
