package dw.take.seal.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.design.widget.Snackbar
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.nanchen.compresshelper.CompressHelper
import dw.take.seal.R
import dw.take.seal.R.attr.num
import dw.take.seal.control.login
import dw.take.seal.control.mLogin
import dw.take.seal.control.mMain
import dw.take.seal.model.OrganizationModel
import dw.take.seal.model.ShopModel
import dw.take.seal.utils.ImgUtils
import kotlinx.android.synthetic.main.activity_mobilecheck.*
import kotlinx.android.synthetic.main.activity_step_two.*
import me.iwf.photopicker.PhotoPicker
import wai.gr.cla.base.BaseActivity
import wai.gr.cla.method.Utils
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.*
import android.os.Environment.DIRECTORY_PICTURES
import android.os.Environment.getExternalStoragePublicDirectory
import android.graphics.Bitmap.CompressFormat
import dw.take.seal.model.CardInfoModel
import dw.take.seal.model.OrganizationJianModel


class MobileCheckActivity : BaseActivity(), mMain, mLogin {


    //是否为法人
    var isFaRen: Boolean = true;
    var isChecked: Boolean = false;
    var isCheckedimg1: Boolean = false;
    var isCheckedimg2: Boolean = false;
    var isCheckedimg3: Boolean = false;
    var position = 0
    var urls = ArrayList<String>()
    var cardInfo:CardInfoModel?=null
    var jbCardInfo:CardInfoModel?=null
    var orgModel:OrganizationJianModel?=null
    override fun face_result(result: Boolean, compar: Boolean, mes: String) {
        //证照比对结果
        if(result){
            if(compar){
                tv_mes3.setText("证照比对成功")
            }else{
                tv_mes3.setText("人像比对失败，请重新拍照")
            }
        }else{
            tv_mes3.setText("数据异常："+mes)
        }
    }
    override fun card_info_result(result: Boolean, info: CardInfoModel, type: Boolean,mes:String) {
        if(type){
            //法人
            if(info!=null){
                cardInfo=info
            }
            //提示是否成功
            if(result){
                isCheckedimg1=true
                tv_mes1.setText("法人证件照片识别成功")
            }else{
                isCheckedimg1=false
                tv_mes1.setText("法人证件照片识别失败"+mes)
            }

            //对比法人姓名
//            if(!orgModel!!.organizationLeader!!.equals(cardInfo!!.personName!!)){
//                ch_iv_farenz.setImageResource(R.mipmap.moren)
//                tv_mes1.setText("不是法人本人证件")
//            }
        }else{
            if(info!=null){
                jbCardInfo=info
            }
            //经办人
            //提示是否成功
            if(result){
                isCheckedimg2=true
                tv_mes2.setText("经办人证件照片识别成功")
            }else{
                isCheckedimg2=false
                tv_mes2.setText("经办人证件照片识别失败"+mes)
            }
        }
    }

    override fun load(result: Int) {

    }


    override fun check_code_result(result: Boolean, toast: String) {
        isChecked = result;
        if (!result) {
            toast(toast);
        }
    }

    var task: TimerTask? = null
    internal var num = 600
    val timer = Timer()
    override fun show_shops(shops: List<ShopModel>) {

    }

    override fun send_code_result(result: Boolean, toast: String) {
        toast(toast)
        //Snackbar.make(get_code_btn, toast, Toast.LENGTH_SHORT).show()
        if (!result) {//发送失败，重置按钮
            no_internet()
            //验证
        }
    }

    override fun show_user_info(user: OrganizationModel) {
    }

    override fun initEvents() {
        for (i in 0..4) {
            urls.add("")
        }
        //输入验证码完成验证验证码是否正确
        mcjb_tel_code_et.setOnFocusChangeListener { view, b ->
            if (b) {
                //此处为得到焦点时的处理内容
            } else {
                //此处为失去焦点的处理
                login().Check_code(mcjb_tel_code_et.text.toString().trim(), this@MobileCheckActivity)
            }
        }

        ch_iv_farenz.setOnClickListener {
            //法人身份证照片
            position = 1

            if (urls.size > 1) {
                tackphoto(urls[0]);
            } else {
                toast("异常")
            }

        }
        ch_iv_jbrz.setOnClickListener {
            //经办人身份证照片
            position = 2
            if (urls.size > 2) {
                tackphoto(urls[1]);
            } else {
                toast("异常")
            }
        }
        mc_iv_brz.setOnClickListener {
            //本人照片
            position = 3
            if (urls.size > 3) {
                tackphoto(urls[2]);
            } else {
                toast("异常")
            }
        }

        mcget_code_btn.setOnClickListener {
            val tel = mcjb_tel_et.text.toString().trim()
            if (TextUtils.isEmpty(tel) || !Utils.isMobileNo(tel)) {
                Snackbar.make(mcget_code_btn, "请输入正确的手机号", Toast.LENGTH_SHORT).show()
            } else {
                num = 60
                task = object : TimerTask() {
                    override fun run() {
                        if (num >= 0) {
                            val message = Message()
                            message.what = 1
                            handler.sendMessage(message)
                        }
                    }
                }
                timer.schedule(task, 1000 * 1, 1000 * 1)
                login().send_code(tel, this)
            }
        }
        mcclose_btn.setOnClickListener { finish() }
        mcnext_btn.setOnClickListener {
            if (!isChecked) {
                toast("验证码错误！")
            } else if (!isCheckedimg1) {
                toast("法人证件照片错误")
            } else if (!isCheckedimg2) {
                toast("经办人证件照片错误")
            } else if (!isCheckedimg3) {
                toast("本人照片错误")
            } else {
                //验证验证码http://118.190.47.63:1399/Service/Message.aspx?Action=CheckVerificationCode&VerificationCode=819097
                startActivity(Intent(this@MobileCheckActivity, StepThreeActivity::class.java))
            }
        }
    }

    var handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (num > 0) {
                mcget_code_btn.isClickable = false
                mcget_code_btn.text = num.toString() + "s"
                num--
            } else {
                no_internet()
            }
        }
    }

    fun tackphoto(url: String) {
        var array = ArrayList<String>()
        array.add(url)
        PhotoPicker.builder()
                .setPhotoCount(1)
                .setPreviewEnabled(false)
                .setShowCamera(true)
                .setSelected(array)
                .start(this, PhotoPicker.REQUEST_CODE)
    }

    override fun initViews() {
        setContentView(R.layout.activity_mobilecheck)
        isFaRen = intent.getBooleanExtra("isFaRen", true)
        orgModel= intent.getSerializableExtra("OrgModel") as OrganizationJianModel?
        // login().cardRecognition_img("")
        if (isFaRen) {
            //隐藏经办人证件照片
            mc_ll_jbr.visibility = View.GONE
        } else {
            mc_ll_jbr.visibility = View.VISIBLE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode === Activity.RESULT_OK && requestCode === PhotoPicker.REQUEST_CODE) {
            if (data != null) {
                var img = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS)
                urls.add(position - 1, img[0])
                //http://118.190.47.63:1399/Service/SealProcess.aspx?Action=UploadCertificate&FileType=02&FileTypeName=法人身份证件正面(董事长护照)
                when (position) {
                    1 -> {
                        Glide.with(this).load(img[0]).error(R.mipmap.moren).into(ch_iv_farenz)
                        urls[0] = img[0]
                        var bm = Utils.getimage(100, img[0])
                        login().cardRecognition_img(ImgUtils().bitmapToBase64(bm!!),this,true)
                        tv_mes1.setText("上传照片中...")
                    }
                    2 -> {
                        Glide.with(this).load(img[0]).error(R.mipmap.moren).into(ch_iv_jbrz)
                        urls[1] = img[0]
                        var bm = Utils.getimage(100, img[0])
                        login().cardRecognition_img(ImgUtils().bitmapToBase64(bm!!),this,false)
                        tv_mes1.setText("上传照片中...")
                    }
                    3 -> {
                        //本人照片和证件照片对比
                        Glide.with(this).load(img[0]).error(R.mipmap.moren).into(mc_iv_brz)
                        tv_mes1.setText("上传照片中...")
                        urls[3]=img[0]
                        var bm1:String?=null;
                        if(isFaRen){
                            if(cardInfo==null||cardInfo!!.personFaceImage==null) {
                                toast("请先上传法人证件照片")
                            }else{
                                bm1=cardInfo!!.personFaceImage
                            }
                        }else{
                            if(jbCardInfo==null||jbCardInfo!!.personFaceImage==null) {
                                toast("请先上传经办人证件照片")
                            }else{
                                bm1=jbCardInfo!!.personFaceImage
                            }
                        }
                        //本人现场照片
                        var bm = Utils.getimage(100, img[0])
                        //证件照片
                        login().card_fackRecognition_img(bm1!!, Utils.encodeBitmap(bm!!),this)
                    }
                }
            }
        }
    }

    /**
     * 无网络，error处理,重置按钮
     * */
    fun no_internet() {
        mcget_code_btn.isClickable = true
        mcget_code_btn.text = "重新发送"
        num = 0
    }
}
