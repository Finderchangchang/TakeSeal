package wai.gr.cla.base

import android.app.ProgressDialog
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import dw.take.seal.R
import dw.take.seal.control.IBaseInter
import net.tsz.afinal.FinalDb
import android.content.Intent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import wai.gr.cla.base.BaseActivity.ExitReceiver






/**
 * BaseActivity声明相关通用方法
 *
 *
 * Created by LiuWeiJie on 2015/7/22 0022.
 * Email:1031066280@qq.com
 */
abstract class BaseActivity : AppCompatActivity(), IBaseInter {
    internal var dialog: ProgressDialog? = null
    var findb:FinalDb?=null
    private val EXITACTION = "action.exit"

    private val exitReceiver = ExitReceiver()


    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        findb=FinalDb.create(this,"taskseal.db");
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制竖屏
        val filter = IntentFilter()
        filter.addAction(EXITACTION)
        registerReceiver(exitReceiver, filter)

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

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(exitReceiver)
    }

    internal inner class ExitReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            this@BaseActivity.finish()
        }

    }

}
