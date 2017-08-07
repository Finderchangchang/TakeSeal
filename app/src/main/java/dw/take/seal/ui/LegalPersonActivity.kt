package dw.take.seal.ui

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView

import dw.take.seal.R
import dw.take.seal.control.ZJSBListener
import dw.take.seal.control.card_view
import dw.take.seal.model.CardInfoModel
import dw.take.seal.utils.ImgUtils
import dw.take.seal.view.CameraSurfaceView
import kotlinx.android.synthetic.main.ac_legal_person.*
import wai.gr.cla.base.BaseActivity
import wai.gr.cla.method.Utils
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.hardware.Camera
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import dw.take.seal.model.OrganizationJianModel
import net.tsz.afinal.view.LoadingDialog
import java.io.File


/**
 * 法人证件照片上传
 * Created by Administrator on 2017/8/2.
 */

class LegalPersonActivity : BaseActivity(), card_view {
    //图片保存路径
    private var path = StringBuffer()
    var cardInfo: CardInfoModel? = null
    var pdialog: ProgressDialog? = null
    var isSuccess: Boolean = false
    override fun card_info_view(result: Boolean, info: CardInfoModel, mes: String) {
        //证件识别结果
        //toast(mes);
        if (pdialog != null) {
            pdialog!!.dismiss()
        }
        if (result) {
            if (info != null) {
                cardInfo = info
                cardInfo!!.faren = true
                findb!!.save(cardInfo)
                lp_tv_name.visibility = View.VISIBLE
                lp_tv_cardid.visibility = View.VISIBLE
                lp_tv_name.text = "姓名：" + info.personName
                lp_tv_cardid.text = "身份证号码：" + info.identyNumber
            } else {
                lp_tv_name.visibility = View.VISIBLE
                lp_tv_name.text = mes;
                lp_tv_cardid.visibility = View.GONE
            }
        } else {
            lp_tv_name.visibility = View.VISIBLE
            lp_tv_name.text = mes;
            lp_tv_cardid.visibility = View.GONE
        }
    }


    override fun initViews() {
        setContentView(R.layout.ac_legal_person)


    }

    override fun initEvents() {
        btn_upload_faren!!.setOnClickListener {
            startActivityForResult(Intent(this, CameraPersonActivity::class.java), 12)
        }
        lp_next_btn.setOnClickListener {
            if (isSuccess) {
                startActivity(Intent(this, FaceActivity::class.java))
            }
        }
        lp_close_btn.setOnClickListener {
            findb!!.deleteByWhere(CardInfoModel::class.java,"isFaren=true")
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 11 && resultCode == Activity.RESULT_OK) {
            val photo = Utils.getimage(200, path.toString())
            //val zhengbm = Utils.centerSquareScaleBitmap(photo, 100)
            pdialog = LoadingDialog(this)
            pdialog!!.show()
            lp_iv_farenz!!.setImageBitmap(photo)
//            val bm = Utils.compressImagexin(photo, 200)
            ZJSBListener().cardRecognition_img(ImgUtils().bitmapToBase64(photo!!), this)
        } else if (requestCode == 12 && resultCode == 12) {
            var mypath = data!!.getStringExtra("PATH")
            val photo = Utils.getimage(200, mypath.toString())
            //val zhengbm = Utils.centerSquareScaleBitmap(photo, 100)
            pdialog = LoadingDialog(this)
            pdialog!!.show()
            lp_iv_farenz!!.setImageBitmap(photo)
//            val bm = Utils.compressImagexin(photo, 200)
            cardInfo= CardInfoModel()
            cardInfo!!.personBaseImg=ImgUtils().bitmapToBase64(photo!!)
            ZJSBListener().cardRecognition_img(cardInfo!!.personBaseImg, this)
        }
    }

}
