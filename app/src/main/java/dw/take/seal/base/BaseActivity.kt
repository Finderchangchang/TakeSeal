package wai.gr.cla.base

import android.app.ProgressDialog
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import dw.take.seal.R
import dw.take.seal.control.IBaseInter

/**
 * BaseActivity声明相关通用方法
 *
 *
 * Created by LiuWeiJie on 2015/7/22 0022.
 * Email:1031066280@qq.com
 */
abstract class BaseActivity : AppCompatActivity(), IBaseInter {
    internal var dialog: ProgressDialog? = null


    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        initViews()
        initEvents()
    }

    abstract fun initViews()

    abstract fun initEvents()

    private var toast: Toast? = null

    fun toast(msg: String) {
        if (toast == null) {
            toast = Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT)
        } else {
            toast!!.setText(msg)
        }
        toast!!.show()
    }

    override fun error_net() {
        toast(getString(R.string.error_net))
    }

    override fun error_toast(msg: String) {
        toast(msg)
    }
}
