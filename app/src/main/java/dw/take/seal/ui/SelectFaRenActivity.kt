package dw.take.seal.ui

import android.content.Intent
import dw.take.seal.R
import kotlinx.android.synthetic.main.ac_select_faren.*
import wai.gr.cla.base.BaseActivity
import wai.gr.cla.model.key

/**
 * 选择法人
 * Created by Administrator on 2017/8/7.
 */

class SelectFaRenActivity : BaseActivity(){
    override fun initEvents() {
        faren_next_btn.setOnClickListener {
            var it: Intent = Intent()
            if(faren_rb_fa.isChecked){
                //是法人本人
                it.setClass(this,LegalPersonActivity::class.java)
                dw.take.seal.utils.Utils().WriteString(key.KEY_TAKESEAL_ISFAREN,"1")
            }else{
                //经办人
                it.setClass(this,JBRActivity::class.java)
                dw.take.seal.utils.Utils().WriteString(key.KEY_TAKESEAL_ISFAREN,"0")
            }

            startActivity(it)
        }
    }

    override fun initViews() {
        setContentView(R.layout.ac_select_faren)
    }

}
