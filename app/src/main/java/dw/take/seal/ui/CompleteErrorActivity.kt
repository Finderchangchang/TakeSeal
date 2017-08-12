package dw.take.seal.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import dw.take.seal.R
import kotlinx.android.synthetic.main.activity_complete_error.*
import wai.gr.cla.base.BaseActivity

class CompleteErrorActivity : BaseActivity() {
    var result:String=""
    override fun initEvents() {

    }
    override fun initViews() {
        setContentView(R.layout.activity_complete_error)
        result=intent.getStringExtra("resultmes")
        complete_error_mes.text=result
    }


}
