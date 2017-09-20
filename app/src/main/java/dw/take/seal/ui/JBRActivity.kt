package dw.take.seal.ui

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.view.View
import com.kaopiz.kprogresshud.KProgressHUD
import dw.take.seal.R
import dw.take.seal.control.ZJSBListener
import dw.take.seal.control.card_view
import dw.take.seal.model.ApplySealCertificateData
import dw.take.seal.model.CardInfoModel
import dw.take.seal.model.OrganizationJianModel
import dw.take.seal.utils.ImgUtils
import kotlinx.android.synthetic.main.ac_legal_person.*
import kotlinx.android.synthetic.main.activity_jbr.*
import net.tsz.afinal.view.LoadingDialog
import wai.gr.cla.base.BaseActivity
import wai.gr.cla.method.Utils
import wai.gr.cla.model.key

/**
 * Created by Administrator on 2017/8/5.
 */

class JBRActivity : BaseActivity(), card_view {
    //图片保存路径
    private var path = StringBuffer()
    var cardInfo: CardInfoModel? = null
    var pdialog: KProgressHUD? = null
    var isSuccess: Boolean = false
    var isFa: Boolean = true
    var apply: ApplySealCertificateData = ApplySealCertificateData()
    var applybm: ApplySealCertificateData = ApplySealCertificateData()
    override fun card_info_view(result: Boolean, info: CardInfoModel?, mes: String) {
        //证件识别结果
        //toast(mes);
        if (pdialog != null) {
            pdialog!!.dismiss()
        }

        isSuccess = result
        if (result) {
            if (info != null) {
                cardInfo!!.isFaren = "false"
                cardInfo = info
                findb!!.save(cardInfo)
                jbr_tv_name.visibility = View.VISIBLE
                jbr_tv_cardid.visibility = View.VISIBLE
                jbr_tv_name.text = "姓名：" + info.personName
                jbr_tv_cardid.text = "身份证号码：" + info.identyNumber
            } else {
                jbr_tv_name.visibility = View.VISIBLE
                jbr_tv_name.text = mes;
                jbr_tv_cardid.visibility = View.GONE
                jbr_iv_farenz!!.setImageResource(R.mipmap.sfza)
            }
        } else {
            jbr_tv_name.visibility = View.VISIBLE
            jbr_tv_name.text = mes;
            jbr_tv_cardid.visibility = View.GONE
            jbr_iv_farenz!!.setImageResource(R.mipmap.sfza)
        }
    }

    override fun initEvents() {
        jbr_iv_farenz!!.setOnClickListener {
            startActivityForResult(Intent(this, CameraPersonActivity::class.java), 12)
        }
        jbr_next_btn.setOnClickListener {
            if (isSuccess) {
                if (applybm.SealCertificateImage == null) {
                    toast("请先上传经办人证件背面照片,验证通过才可进入下一步")
                } else {
                    apply.SealCertificateType = "03"
                    apply.SealCertificateName = "经办人身份证"
                    applybm.SealCertificateType = "27"
                    applybm.SealCertificateName = "经办人身份证背面"
                    findb!!.deleteByWhere(CardInfoModel::class.java, "isFaren='false'")
                    findb!!.deleteByWhere(ApplySealCertificateData::class.java, "SealCertificateType='27'")
                    findb!!.deleteByWhere(ApplySealCertificateData::class.java, "SealCertificateType='03'")
                    findb!!.save(apply)
                    findb!!.save(applybm)
                    cardInfo!!.isFaren = "false"
                    findb!!.save(cardInfo)
                    startActivity(Intent(this, FaceActivity::class.java))
                }
            } else {
                toast("请先上传经办人证件照片,验证通过才可进入下一步")
            }
        }
        jbr_close_btn.setOnClickListener {
            finish()
        }
        jbr_iv_farenbm.setOnClickListener {
            startActivityForResult(Intent(this, CameraPersonActivity::class.java), 13)
        }
    }

    override fun onDestroy() {
        findb!!.deleteByWhere(CardInfoModel::class.java, "isFaren='false'")
        findb!!.deleteByWhere(ApplySealCertificateData::class.java, "SealCertificateType='27'")
        super.onDestroy()
    }

    override fun initViews() {
        setContentView(R.layout.activity_jbr)
        isFa = dw.take.seal.utils.Utils(this).ReadString(key.KEY_TAKESEAL_ISFAREN).equals("1")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 11 && resultCode == Activity.RESULT_OK) {
            val photo = Utils.getimage(100, path.toString())
            //val zhengbm = Utils.centerSquareScaleBitmap(photo, 100)
            pdialog = KProgressHUD.create(this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("识别中，请稍后")
                    .setCancellable(false)
                    .setAnimationSpeed(2)
                    .setDimAmount(0.5f)
            pdialog!!.show()
            lp_iv_farenz!!.setImageBitmap(photo)
//            val bm = Utils.compressImagexin(photo, 200)
            ZJSBListener().cardRecognition_img(ImgUtils().bitmapToBase64(photo!!), this)
        } else if (requestCode == 12 && resultCode == 12) {
            var mypath = data!!.getStringExtra("PATH")
            val photo = Utils.getimage(200, mypath.toString())
            //val zhengbm = Utils.centerSquareScaleBitmap(photo, 100)
            pdialog = KProgressHUD.create(this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("识别中，请稍后")
                    .setCancellable(false)
                    .setAnimationSpeed(2)
                    .setDimAmount(0.5f)
            pdialog!!.show()
            jbr_iv_farenz!!.setImageBitmap(photo)
            cardInfo = CardInfoModel()
            cardInfo!!.personBaseImg = ImgUtils().bitmapToBase64(photo!!)
            apply.SealCertificateImage = cardInfo!!.personBaseImg

//            val bm = Utils.compressImagexin(photo, 200)
            ZJSBListener().cardRecognition_img(cardInfo!!.personBaseImg, this)

        } else if (requestCode == 13 && resultCode == 12) {
            var mypath = data!!.getStringExtra("PATH")
            val photo = Utils.getimage(100, mypath.toString())
            jbr_iv_farenbm!!.setImageBitmap(photo)
            applybm.SealCertificateImage = ImgUtils().bitmapToBase64(photo!!)
        }
    }

}
