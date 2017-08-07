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
        seal_model = intent.getSerializableExtra("SealModel") as SealModel
        seal_tv_type.text = seal_model!!.SealTypeName
        var seal_num = seal_model!!.num.toString()
        if (TextUtils.isEmpty(seal_num)) {
            seal_tv_num.text = "1"
        } else {
            if (seal_num.toInt() == 0) {
                seal_num = "1"
            }
            seal_tv_num.text = seal_num
        }
        if (!TextUtils.isEmpty(seal_model!!.SealGGName)) {
            seal_tv_guige.text = seal_model!!.SealGGName
        }
        if (!TextUtils.isEmpty(seal_model!!.SealSpecificationName)) {
            seal_tv_caizhi.text = seal_model!!.SealSpecificationName
        }
        if (seal_model!!.num.toInt() > 0) {
            seal_tv_num.setText(seal_model!!.num.toString())
        }
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
        jia_iv.setOnClickListener {
            var num = seal_tv_num.text.toString().toInt()
            if (num < 10) {
                seal_tv_num.text = (num + 1).toString()
            }
        }
        jian_iv.setOnClickListener {
            var num = seal_tv_num.text.toString().toInt()
            if (num > 1) {
                seal_tv_num.text = (num - 1).toString()
            }
        }
    }

    override fun initViews() {
        setContentView(R.layout.activity_select_seal)
        save_btn.setOnClickListener {
            if (("请选择印章规格").equals(seal_tv_guige.text)) {
                toast("请选择印章规格")
            } else if (("请选择印章材质").equals(seal_tv_caizhi.text)) {
                toast("请选择印章材质")
            } else {
                var intent = Intent()
//                intent.putExtra("code", seal_code)
//                intent.putExtra("specifi", seal_specifi)
//                intent.putExtra("num", seal_tv_num.text.toString())
                var num = seal_tv_num.text.toString().toInt()
                seal_model!!.num = num
                seal_model!!.isSelect = true
                intent.putExtra("select_model", seal_model)
                setResult(1, intent)
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
                        //seal_code = model
                        seal_model!!.SealGGId = model.Key
                        seal_model!!.SealGGName = model.Value
                    }
                    else -> {
                        seal_tv_caizhi.text = `model`.Value
                        //seal_specifi = model
                        seal_model!!.SealSpecificationId = model.Key
                        seal_model!!.SealSpecificationName = model.Value
                    }
                }
            }
        }
    }
}
