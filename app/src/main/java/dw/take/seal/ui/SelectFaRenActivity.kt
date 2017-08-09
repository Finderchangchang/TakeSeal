package dw.take.seal.ui

import android.content.Intent
import dw.take.seal.R
import dw.take.seal.R.style.BottomTabStyle
import kotlinx.android.synthetic.main.ac_select_faren.*
import wai.gr.cla.base.BaseActivity
import wai.gr.cla.model.key

/**
 * 选择法人
 * Created by Administrator on 2017/8/7.
 */

class SelectFaRenActivity : BaseActivity() {
    override fun initEvents() {
        faren_next_btn.setOnClickListener {
            var it: Intent = Intent()
            it.setClass(this, LegalPersonActivity::class.java)
            if(faren_rb_fa.isChecked){
                dw.take.seal.utils.Utils(this).WriteString(key.KEY_TAKESEAL_ISFAREN, "1")
            }else{
                dw.take.seal.utils.Utils(this).WriteString(key.KEY_TAKESEAL_ISFAREN, "2")
            }

            startActivity(it)
        }
    }

    override fun initViews() {
        setContentView(R.layout.ac_select_faren)
    }

}
