package dw.take.seal.ui

import android.content.Intent
import android.text.TextUtils
import dw.take.seal.R
import dw.take.seal.control.login
import dw.take.seal.control.mLogin
import kotlinx.android.synthetic.main.activity_login.*
import me.iwf.photopicker.PhotoPicker
import wai.gr.cla.base.BaseActivity

/***
 * 登录
 */
class LoginActivity : BaseActivity(), mLogin {
    override fun initViews() {
        setContentView(R.layout.activity_login)
    }

    override fun initEvents() {
        /***
         * 用户密码登录
         */
        login_btn.setOnClickListener {
            var count_et = count_et.text.toString().trim()
            var pwd_et = pwd_et.text.toString().trim()
            if (TextUtils.isEmpty(count_et)) {
                toast("登录账号不能为空")
            } else if (TextUtils.isEmpty(pwd_et)) {
                toast("密码不能为空")
            } else {
                login().user_login(count_et, pwd_et, this, this)
            }
        }
        /**
         * 扫描营业执照登录
         * */
        scan_login_btn.setOnClickListener {
            startActivity(Intent(this, ScanLoginActivity::class.java))
        }
    }

    /**
     * 登录结果处理 true：登录成功
     * */
    override fun load(result: Int) {
        if (result == 1) {
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }
    }
}
