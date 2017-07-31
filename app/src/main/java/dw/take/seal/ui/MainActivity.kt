package dw.take.seal.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.View
import android.widget.Toast

import dw.take.seal.R
import dw.take.seal.control.login
import dw.take.seal.control.mMain
import dw.take.seal.model.OrganizationModel
import dw.take.seal.model.ShopModel
import kotlinx.android.synthetic.main.activity_main.*
import wai.gr.cla.base.BaseActivity

class MainActivity : BaseActivity(){
    override fun initViews() {
        setContentView(R.layout.activity_main)
    }

    override fun initEvents() {
        /**
         * 第一个下一步点击事件
         * */
        next_btn.setOnClickListener {
            if (yes_rb.isChecked) {
                startActivity(Intent(this,OrganizationActivity::class.java))
            } else {
                Snackbar.make(next_btn, "请点击同意备案协议！", Toast.LENGTH_SHORT).show()
            }
        }
        finish_btn.setOnClickListener { finish() }
    }
}
