package dw.take.seal.ui

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.widget.TextView
import android.widget.Toast
import dw.take.seal.R
import dw.take.seal.control.GetSpecificationCodesListener
import dw.take.seal.control.GetSpecificationCodesView
import dw.take.seal.model.CodeModel
import dw.take.seal.model.SealModel
import dw.take.seal.view.SpinnerDialog
import kotlinx.android.synthetic.main.activity_select_seal.*
import wai.gr.cla.base.BaseActivity
import java.util.*

class SelectSealActivity : BaseActivity(), GetSpecificationCodesView {
    var listtype: ArrayList<CodeModel>? = null
    var list: ArrayList<CodeModel>? = null
    var seal_model: SealModel? = null
    var seal_code: CodeModel? = null
    var seal_specifi: CodeModel? = null
    /**
     * 获得印章规格列表，并显示
     * */
    override fun GetSpecificationCodesResult(success: Boolean, list: ArrayList<CodeModel>?, type: String) {
        if (success) {
            ShowMyDialog(list!!)
        } else {
            toast("加载字典失败")
        }
    }

    var now_click = 1//1.选择规格，2.选择材质
    override fun initEvents() {
        seal_tv_num.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                var txt = seal_tv_num.text.toString().toInt()
                if (txt > 10) {
                    seal_tv_num.setText("10")
                    toast("章的数量不能大于10")
                }
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {

            }
        })
        seal_model = intent.getSerializableExtra("SealModel") as SealModel
        seal_tv_type.text = seal_model!!.SealTypeName
        /**
         * 选择印章规格
         */
        seal_ll_guige.setOnClickListener {
            now_click = 1
            GetSpecificationCodesListener().getSpecificationSeal(this, "1", seal_model!!.SealType)
        }
        /**
         * 选择印章材质
         * */
        seal_ll_caizhi.setOnClickListener {
            now_click = 2
            GetSpecificationCodesListener().getSpecificationSeal(this, "2", "")
        }
    }

    override fun initViews() {
        setContentView(R.layout.activity_select_seal)
        save_btn.setOnClickListener {
            if (TextUtils.isEmpty(seal_tv_guige.text)) {
                toast("请选择印章规格")
            } else if (TextUtils.isEmpty(seal_tv_guige.text)) {
                toast("请选择印章材质")
            } else if (TextUtils.isEmpty(seal_tv_num.text)) {
                toast("请输入印章数量")
            } else {
                var intent = Intent()
                intent.putExtra("code", seal_code)
                intent.putExtra("specifi", seal_specifi)
                intent.putExtra("num", seal_tv_num.text)
                finish()
            }
        }
    }


    fun ShowMyDialog(listBlue: ArrayList<CodeModel>) {
        var dialog: SpinnerDialog? = null
        if (listBlue!!.size > 0) {
            dialog = SpinnerDialog(this)
            dialog.setListView(listBlue)
            dialog.show()
            dialog.setOnItemClick { position, model ->
                when (now_click) {
                    1 -> {
                        seal_tv_guige.text = `model`.Value
                        seal_code = model
                    }
                    else -> {
                        seal_tv_caizhi.text = `model`.Value
                        seal_specifi = model
                    }
                }
            }
        }
    }
}
