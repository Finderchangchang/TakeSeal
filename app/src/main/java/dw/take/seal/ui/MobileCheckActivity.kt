package dw.take.seal.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.design.widget.Snackbar
import android.text.TextUtils
import android.text.TextWatcher
import android.widget.Toast
import dw.take.seal.R
import dw.take.seal.R.attr.num
import dw.take.seal.control.login
import dw.take.seal.control.mMain
import dw.take.seal.model.OrganizationModel
import dw.take.seal.model.ShopModel
import kotlinx.android.synthetic.main.activity_mobilecheck.*
import kotlinx.android.synthetic.main.activity_step_two.*
import me.iwf.photopicker.PhotoPicker
import wai.gr.cla.base.BaseActivity
import wai.gr.cla.method.Utils
import java.util.*

class MobileCheckActivity : BaseActivity(), mMain {
    var isChecked: Boolean = false;
    var isCheckedimg1: Boolean = false;
    var isCheckedimg2: Boolean = false;
    var isCheckedimg3: Boolean = false;
    var position = 0
    var urls = ArrayList<String>()
    override fun check_code_result(result: Boolean, toast: String) {
        isChecked = result;
        if (!result) {
            toast(toast);
        }
    }

    var task: TimerTask? = null
    internal var num = 60
    val timer = Timer()
    override fun show_shops(shops: List<ShopModel>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun send_code_result(result: Boolean, toast: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        Snackbar.make(get_code_btn, toast, Toast.LENGTH_SHORT).show()
        if (!result) {//发送失败，重置按钮
            no_internet()
            //验证

        }
    }

    override fun show_user_info(user: OrganizationModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun initEvents() {
        for (i in 0..4) {
            urls.add("")
        }
        mcjb_tel_code_et
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
//        mcjb_tel_code_et.addTextChangedListener(TextWatcher(){
//
//        });
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

    fun tackphoto(url: String) {
        var array = ArrayList<String>()
        array.add(urls[4])
        PhotoPicker.builder()
                .setPhotoCount(1)
                .setPreviewEnabled(false)
                .setShowCamera(true)
                .setSelected(array)
                .start(this, PhotoPicker.REQUEST_CODE)
    }

    override fun initViews() {
        setContentView(R.layout.activity_mobilecheck)
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
