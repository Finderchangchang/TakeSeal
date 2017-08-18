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
import com.kaopiz.kprogresshud.KProgressHUD
import dw.take.seal.model.ApplySealCertificateData
import dw.take.seal.model.OrganizationJianModel
import kotlinx.android.synthetic.main.activity_face.*
import net.tsz.afinal.view.LoadingDialog
import wai.gr.cla.model.key
import java.io.File


/**
 * 法人证件照片上传
 * Created by Administrator on 2017/8/2.
 */

class LegalPersonActivity : BaseActivity(), card_view {
    //图片保存路径
    private var path = StringBuffer()
    var cardInfo: CardInfoModel? = null
    var pdialog: KProgressHUD? = null
    var isSuccess: Boolean = false
    var apply: ApplySealCertificateData = ApplySealCertificateData()
    var isFa: Boolean = true
    var org: OrganizationJianModel? = null
    override fun card_info_view(result: Boolean, info: CardInfoModel?, mes: String) {
        //证件识别结果
        //toast(mes);
        if (pdialog != null) {
            pdialog!!.dismiss()
        }
        isSuccess = result
        if (result) {
            if (info != null) {
                if (!org!!.organizationLeader.equals(info.personName)) {
                    isSuccess = false
                    lp_tv_name.visibility = View.VISIBLE
                    lp_tv_cardid.visibility = View.GONE
                    lp_tv_name.text = "不是法人本人身份证"
                    lp_iv_farenz!!.setImageResource(R.mipmap.shenfenzhong)
                } else {
                    cardInfo = info
                    cardInfo!!.isFaren = "true"
                    lp_tv_name.visibility = View.VISIBLE
                    lp_tv_cardid.visibility = View.VISIBLE
                    lp_tv_name.text = "姓名：" + info.personName
                    lp_tv_cardid.text = "身份证号码：" + info.identyNumber
                }
            } else {
                lp_tv_name.visibility = View.VISIBLE
                lp_tv_name.text = mes;
                lp_tv_cardid.visibility = View.GONE
                lp_iv_farenz!!.setImageResource(R.mipmap.shenfenzhong)
            }
        } else {
            lp_tv_name.visibility = View.VISIBLE
            lp_tv_name.text = mes;
            lp_tv_cardid.visibility = View.GONE
            lp_iv_farenz!!.setImageResource(R.mipmap.shenfenzhong)
        }
    }


    override fun initViews() {
        setContentView(R.layout.ac_legal_person)
        apply.SealCertificateType = "02"
        apply.SealCertificateName = "法人代表人身份证（董事长护照）"
        isFa = dw.take.seal.utils.Utils(this).ReadString(key.KEY_TAKESEAL_ISFAREN).equals("1")
        var orgs: MutableList<OrganizationJianModel>;
        orgs = findb!!.findAll(OrganizationJianModel::class.java)
        if (orgs.size > 0) {
            org = orgs[0]
        }
    }

    override fun initEvents() {
        lp_iv_farenz!!.setOnClickListener {
            startActivityForResult(Intent(this, CameraPersonActivity::class.java), 12)
        }
        lp_next_btn.setOnClickListener {
            if (isSuccess) {
                findb!!.deleteByWhere(CardInfoModel::class.java, "isFaren='true'")
                findb!!.deleteByWhere(ApplySealCertificateData::class.java, "SealCertificateType='02'")
                findb!!.save(apply)
                findb!!.save(cardInfo)
                if (isFa) {
                    startActivity(Intent(this, FaceActivity::class.java))
                } else {
                    startActivity(Intent(this, JBRActivity::class.java))
                }
            } else {
                toast("请先上传法人身份证照片")
            }
        }
        lp_close_btn.setOnClickListener {
            finish()
        }
    }

    override fun onDestroy() {
        findb!!.deleteByWhere(CardInfoModel::class.java, "isFaren='true'")
        findb!!.deleteByWhere(ApplySealCertificateData::class.java, "SealCertificateType='02'")
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 11 && resultCode == Activity.RESULT_OK) {
            val photo = Utils.getimage(200, path.toString())
            //val zhengbm = Utils.centerSquareScaleBitmap(photo, 100)
            pdialog = KProgressHUD.create(this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("识别中，请稍后")
                    .setCancellable(true)
                    .setAnimationSpeed(2)
                    .setDimAmount(0.5f)
            pdialog!!.show()
            lp_iv_farenz!!.setImageBitmap(photo)
//            val bm = Utils.compressImagexin(photo, 200)
            ZJSBListener().cardRecognition_img(ImgUtils().bitmapToBase64(photo!!), this)
        } else if (requestCode == 12 && resultCode == 12) {
            var mypath = data!!.getStringExtra("PATH")
//            System.out.println("path++++:" + mypath)
//            toast("path:" + mypath)
            var photo: Bitmap? = null
            photo = Utils.getimage(100, mypath.toString())
            //val zhengbm = Utils.centerSquareScaleBitmap(photo, 100)
            pdialog = KProgressHUD.create(this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("识别中，请稍后")
                    .setCancellable(false)
                    .setAnimationSpeed(2)
                    .setDimAmount(0.5f)
            pdialog!!.show()
            lp_iv_farenz!!.setImageBitmap(photo)
            //val bm = Utils.compressImagexin(photo, 200)
            cardInfo = CardInfoModel()
            apply.SealCertificateImage = ImgUtils().bitmapToBase64(photo!!)
            cardInfo!!.personBaseImg = apply.SealCertificateImage
            ZJSBListener().cardRecognition_img(cardInfo!!.personBaseImg, this)
        }
    }
}
