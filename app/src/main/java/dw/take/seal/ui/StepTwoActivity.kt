package dw.take.seal.ui

import android.app.Activity
import android.content.Intent
import android.os.Handler
import android.os.Message
import android.support.design.widget.Snackbar
import android.text.TextWatcher
import dw.take.seal.R
import dw.take.seal.control.login
import dw.take.seal.control.mMain
import dw.take.seal.model.OrganizationModel
import dw.take.seal.model.ShopModel
import kotlinx.android.synthetic.main.activity_step_two.*
import wai.gr.cla.base.BaseActivity
import android.widget.Toast
import android.text.Editable
import android.text.TextUtils
import android.view.View
import com.bumptech.glide.Glide
import dw.take.seal.model.JsonModel
import me.iwf.photopicker.PhotoPicker
import wai.gr.cla.method.Utils
import java.util.*

class StepTwoActivity : BaseActivity(), mMain {
    override fun send_code_result(result: Boolean, toast: String) {
        Snackbar.make(get_code_btn, toast, Toast.LENGTH_SHORT).show()
        if (!result) {//发送失败，重置按钮
            no_internet()
        }
    }

    internal var num = 60
    val timer = Timer()
    var task: TimerTask? = null
    var user_info: OrganizationModel? = null
    var user_address = ""
    var json:JsonModel= JsonModel()
    /**
     * 加载当前用户数据
     * */
    override fun show_user_info(user: OrganizationModel) {
        user_info = user
        code_id_tv.text = user.OrganizationUSCC
        name_tv.text = user.OrganizationName
        qy_tv.text = user.OrganizationRegionName
        fr_tv.text = user.OrganizationLeader
        address_tv.text = user.OrganizationAddress
        user_address = user.OrganizationLeaderCertNumber.toString()
    }

    override fun show_shops(shops: List<ShopModel>) {

    }

    override fun initViews() {
        setContentView(R.layout.activity_step_two)
    }

    var position = 0
    override fun initEvents() {
        for(i in 0..4){
            urls.add("")
        }
        login().get_user_info(this, this)//获得当前登录账号信息
        /**
         * 监听输入手机号如果相同隐藏 经办人和委托书选择
         * */
        jb_id_card_et.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString()!!.equals(user_info!!.OrganizationLeaderCertNumber)) {
                    //相同：隐藏
                    ll2.visibility = View.GONE
                } else {//不同：显示
                    ll2.visibility = View.VISIBLE
                }
            }
        })
        /**
         * 获得验证码按钮
         * */
        get_code_btn.setOnClickListener {
            val tel = jb_tel_code_et.text.toString().trim()
            if (TextUtils.isEmpty(tel) || !Utils.isMobileNo(tel)) {
                Snackbar.make(get_code_btn, "请输入正确的手机号", Toast.LENGTH_SHORT).show()
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
        iv1.setOnClickListener {
            position = 1
            var array = ArrayList<String>()
            if (urls.size > 0) {
                array.add(urls[0])
            }
            PhotoPicker.builder()
                    .setPhotoCount(1)
                    .setPreviewEnabled(false)
                    .setShowCamera(true)
                    .setSelected(array)
                    .start(this, PhotoPicker.REQUEST_CODE)
        }
        iv2.setOnClickListener {
            position = 2
            var array = ArrayList<String>()
            if (urls.size > 1) {
                array.add(urls[1])
            }
            PhotoPicker.builder()
                    .setPhotoCount(1)
                    .setPreviewEnabled(false)
                    .setShowCamera(true)
                    .setSelected(array)
                    .start(this, PhotoPicker.REQUEST_CODE)
        }
        iv3.setOnClickListener {
            position = 3
            var array = ArrayList<String>()
            if (urls.size > 2) {
                array.add(urls[2])
            }
            PhotoPicker.builder()
                    .setPhotoCount(1)
                    .setPreviewEnabled(false)
                    .setShowCamera(true)
                    .setSelected(array)
                    .start(this, PhotoPicker.REQUEST_CODE)
        }
        iv4.setOnClickListener {
            position = 4
            var array = ArrayList<String>()
            if (urls.size > 3) {
                array.add(urls[3])
            }
            PhotoPicker.builder()
                    .setPhotoCount(1)
                    .setPreviewEnabled(false)
                    .setShowCamera(true)
                    .setSelected(array)
                    .start(this, PhotoPicker.REQUEST_CODE)
        }
        iv5.setOnClickListener {
            position = 5
            var array = ArrayList<String>()
            if (urls.size >4) {
                array.add(urls[4])
            }
            PhotoPicker.builder()
                    .setPhotoCount(1)
                    .setPreviewEnabled(false)
                    .setShowCamera(true)
                    .setSelected(array)
                    .start(this, PhotoPicker.REQUEST_CODE)
        }
        /**
         * 上一步
         * */
        close_btn.setOnClickListener { finish() }
        next_btn.setOnClickListener {
            //验证验证码http://118.190.47.63:1399/Service/Message.aspx?Action=CheckVerificationCode&VerificationCode=819097
            startActivity(Intent(this@StepTwoActivity,StepThreeActivity::class.java).putExtra("model",json)) }
    }

    var urls = ArrayList<String>()
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode === Activity.RESULT_OK && requestCode === PhotoPicker.REQUEST_CODE) {
            if (data != null) {
                var img = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS)
                urls.add(position-1, img[0])
                //http://118.190.47.63:1399/Service/SealProcess.aspx?Action=UploadCertificate&FileType=02&FileTypeName=法人身份证件正面(董事长护照)
                when (position) {
                    1 -> Glide.with(this).load(img[0]).error(R.mipmap.ic_launcher).into(iv1)
                    2 -> Glide.with(this).load(img[0]).error(R.mipmap.ic_launcher).into(iv2)
                    3 -> Glide.with(this).load(img[0]).error(R.mipmap.ic_launcher).into(iv3)
                    4 -> Glide.with(this).load(img[0]).error(R.mipmap.ic_launcher).into(iv4)
                    5 -> Glide.with(this).load(img[0]).error(R.mipmap.ic_launcher).into(iv5)
                }
            }
        }
    }

    var handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (num > 0) {
                get_code_btn.isClickable = false
                get_code_btn.text = num.toString() + "s"
                num--
            } else {
                no_internet()
            }
        }
    }

    /**
     * 无网络，error处理,重置按钮
     * */
    fun no_internet() {
        get_code_btn.isClickable = true
        get_code_btn.text = "重新发送"
        num = 0
    }

}
