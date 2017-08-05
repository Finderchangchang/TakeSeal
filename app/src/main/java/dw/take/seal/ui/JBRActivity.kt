package dw.take.seal.ui

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.view.View
import dw.take.seal.R
import dw.take.seal.control.ZJSBListener
import dw.take.seal.control.card_view
import dw.take.seal.model.CardInfoModel
import dw.take.seal.model.OrganizationJianModel
import dw.take.seal.utils.ImgUtils
import kotlinx.android.synthetic.main.ac_legal_person.*
import kotlinx.android.synthetic.main.activity_jbr.*
import net.tsz.afinal.view.LoadingDialog
import wai.gr.cla.base.BaseActivity
import wai.gr.cla.method.Utils

/**
 * Created by Administrator on 2017/8/5.
 */

class JBRActivity : BaseActivity(), card_view {
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
        isSuccess=result
        if (result) {
            if (info != null) {
                cardInfo = info
                jbr_tv_name.visibility = View.VISIBLE
                jbr_tv_cardid.visibility = View.VISIBLE
                jbr_tv_name.text = "姓名：" + info.personName
                jbr_tv_cardid.text = "身份证号码：" + info.identyNumber
            } else {
                jbr_tv_name.visibility = View.VISIBLE
                jbr_tv_name.text = mes;
                jbr_tv_cardid.visibility = View.GONE
            }
        } else {
            jbr_tv_name.visibility = View.VISIBLE
            jbr_tv_name.text = mes;
            jbr_tv_cardid.visibility = View.GONE
        }
    }

    override fun initEvents() {

        jbr_iv_farenz!!.setOnClickListener {
            startActivityForResult(Intent(this, CameraPersonActivity::class.java), 12)
        }
        jbr_next_btn.setOnClickListener {
            if (isSuccess) {
                startActivity(Intent(this, FaceActivity::class.java).putExtra("ISFAREN",false))
            }else{
                toast("请先上传经办人证件照片,验证通过才可进入下一步")
            }
        }
        jbr_close_btn.setOnClickListener { finish() }

    }

    override fun initViews() {
        setContentView(R.layout.activity_jbr)

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
            jbr_iv_farenz!!.setImageBitmap(photo)
//            val bm = Utils.compressImagexin(photo, 200)
            ZJSBListener().cardRecognition_img(ImgUtils().bitmapToBase64(photo!!), this)
        }
    }

}
