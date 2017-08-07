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
import dw.take.seal.model.OrganizationJianModel
import dw.take.seal.model.OrganizationModel
import dw.take.seal.model.ShopModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_show_info.*
import wai.gr.cla.base.BaseActivity
import wai.gr.cla.model.key

class MainActivity : BaseActivity(){
    var org:OrganizationJianModel?=null;
    var isFa: Boolean = true
    override fun initViews() {
        setContentView(R.layout.activity_main)
    }

    override fun initEvents() {
        isFa = dw.take.seal.utils.Utils(this).ReadString(key.KEY_TAKESEAL_ISFAREN).equals("1")
        if (isFa) {
            main_title.text = "第七步"
        } else {
            main_title.text = "第九步"
        }
        /**
         * 第一个下一步点击事件
         * */
        next_btn.setOnClickListener {
            if (yes_rb.isChecked) {
                org = intent.getSerializableExtra("OrgModel") as OrganizationJianModel?
                val intent = Intent(this, OrganizationActivity::class.java)
                intent.putExtra("OrgModel",org)
                startActivity(intent)
               // startActivity(Intent(this,OrganizationActivity::class.java),putExtraData("OrgModel",org))
            } else {
                Snackbar.make(next_btn, "请点击同意备案协议！", Toast.LENGTH_SHORT).show()
            }
        }
        finish_btn.setOnClickListener { finish() }
    }
}
