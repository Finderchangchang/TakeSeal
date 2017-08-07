package dw.take.seal.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import dw.take.seal.R
import dw.take.seal.control.SendCodeView
import dw.take.seal.control.SendListener
import dw.take.seal.control.login
import kotlinx.android.synthetic.main.activity_check_code.*
import kotlinx.android.synthetic.main.activity_mobilecheck.*
import wai.gr.cla.base.BaseActivity
import wai.gr.cla.method.Utils
import wai.gr.cla.model.key
import java.util.*

class CheckCodeActivity : BaseActivity(), SendCodeView {
    var task: TimerTask? = null
    internal var num = 600
    val timer = Timer()
    var isfaren:Boolean=true
    override fun yan_code_result(result: Boolean, toast: String) {

        if (result) {
            //验证成功就下一步
            //免责
            //方便测试就直接到选章

            startActivity(Intent(this, CJYingYeActivity::class.java))
        } else {
            toast(toast)
        }
    }

    override fun send_code_result(result: Boolean, toast: String) {
        toast(toast)
    }

    override fun initViews() {
        setContentView(R.layout.activity_check_code)
        isfaren = dw.take.seal.utils.Utils().ReadString(key.KEY_TAKESEAL_ISFAREN).equals("1")
        if(isfaren) {
            check_code_title.text = "第五步"
        }else{
            check_code_title.text = "第六步"
        }
    }

    override fun initEvents() {
        code_code_btn.setOnClickListener {
            //发送验证码
            var mobile = code_tel_et.text.toString().trim()
            if (TextUtils.isEmpty(mobile) || !Utils.isMobileNo(mobile)) {
                toast("请输入正确的手机号")
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
                SendListener().send_code(mobile, this@CheckCodeActivity)
            }

        }
        codenext_btn.setOnClickListener {
            //验证短信验证码
            var yan = code_tel_code_et.text.toString().trim()
            if (TextUtils.isEmpty(yan)) {
                toast("请输入验证码")
            } else {
                SendListener().Check_code(yan, this)
            }
        }
        codeclose_btn.setOnClickListener { finish() }
    }

    var handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (num > 0) {
                code_code_btn.isClickable = false
                code_code_btn.text = num.toString() + "s"
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
        code_code_btn.isClickable = true
        code_code_btn.text = "重新发送"
        num = 0
    }
}
