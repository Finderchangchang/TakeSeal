package dw.take.seal.ui

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import dw.take.seal.R
import dw.take.seal.control.GetSpecificationCodesListener
import dw.take.seal.control.GetSpecificationCodesView
import dw.take.seal.model.CodeModel
import dw.take.seal.view.SpinnerDialog
import kotlinx.android.synthetic.main.activity_select_seal.*
import wai.gr.cla.base.BaseActivity
import java.util.*

class SelectSealActivity : BaseActivity(), GetSpecificationCodesView {
    var listtype: ArrayList<CodeModel>? = null
    var list: ArrayList<CodeModel>? = null
    override fun GetSpecificationCodesResult(success: Boolean, list: ArrayList<CodeModel>?, mes: String) {
        //印章规格
    }

    override fun initEvents() {
        seal_ll_type.setOnClickListener {
            //选择印章类型

        }
        seal_ll_guige.setOnClickListener {
            //选择印章规格
            GetSpecificationCodesListener().getSpecificationSeal(this, "")
        }
    }

    override fun initViews() {
        setContentView(R.layout.activity_select_seal)

    }

    fun LoadData() {
        listtype
    }


    fun ShowMyDialog(list: List<CodeModel>, dialog: SpinnerDialog, view: TextView) {
        var dialog = dialog
        var listBlue: ArrayList<CodeModel>? = null
        if (listBlue!!.size > 0) {
            dialog = SpinnerDialog(this)
            dialog.setListView(listBlue)
            dialog.show()
            dialog.setOnItemClick { position, model ->

                view.setText(`model`.value)
            }

        }
    }
}
