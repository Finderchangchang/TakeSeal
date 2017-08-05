package dw.take.seal.ui

import android.content.Intent
import android.support.design.widget.Snackbar
import android.widget.Toast
import dw.take.seal.R
import dw.take.seal.control.GetSpecificationCodesView
import dw.take.seal.method.CommonAdapter
import dw.take.seal.method.CommonViewHolder
import dw.take.seal.model.CodeModel
import dw.take.seal.model.SealModel
import kotlinx.android.synthetic.main.activity_myseal.*
import wai.gr.cla.base.BaseActivity
import java.util.*

/**
 * 我的印章
 * Created by Administrator on 2017/8/4.
 */

class MySealActivity : BaseActivity(), GetSpecificationCodesView {
    var selectType: Int = 1
    override fun GetSpecificationCodesResult(success: Boolean, list: ArrayList<CodeModel>?, mes: String) {
        //印章规格

    }

    var list: ArrayList<SealModel>? = null
    var adapter: CommonAdapter<SealModel>? = null
    override fun initViews() {
        setContentView(R.layout.activity_myseal)
        LoadData()
    }

    override fun initEvents() {
        adapter = object : CommonAdapter<SealModel>(this@MySealActivity, list, R.layout.item_seal) {
            override fun convert(holder: CommonViewHolder, model: SealModel, position: Int) {
                holder.setText(R.id.is_tv_type, model.SealTypeName)
                holder.setText(R.id.is_tv_num, "数量：" + model.num)
                holder.setText(R.id.is_tv_guige, "规格：" + model.SealSpecificationName)
                holder.setText(R.id.is_tv_content, "内容：" + model.SealContent)
                holder.setText(R.id.is_tv_caizhi, "未设置")
                holder.setSelect(R.id.seal_ck, model.isSelect)
                holder.setOnClickListener(R.id.is_tv_edit) {
                    //跳转到编辑界面
                    startActivity(Intent(this@MySealActivity, SelectSealActivity::class.java).putExtra("SealModel", model))
                }
                holder.setOnClickListener(R.id.seal_ck) {
                    //选择
                    if (model.isSelect) {
                        list!![position].isSelect = false
                    } else {
                        list!![position].isSelect = true
                    }
                }
            }
        }
        myseal_lv.adapter = adapter
    }

    fun LoadData() {
        list = ArrayList()
        var model: SealModel = SealModel()
        model!!.SealContent = "保定亮达机电设备安装服务有限公司"
        model!!.SealTypeName = "单位专用章"
        model!!.isSelect = true
        model!!.num = 1
        list!!.add(model)
        var model1: SealModel = SealModel()
        model1!!.SealContent = "保定亮达机电设备安装服务有限公司财务专用章"
        model1!!.SealTypeName = "财务专用章"
        model1!!.isSelect = true
        model1!!.num = 1
        list!!.add(model1)
        var model2: SealModel = SealModel()
        model2!!.SealContent = "保定亮达机电设备安装服务有限公司发票专用章"
        model2!!.SealTypeName = "发票专用章"
        model2!!.isSelect = false
        model2!!.num = 0
        list!!.add(model2)
        var model3: SealModel = SealModel()
        model3!!.SealContent = "保定亮达机电设备安装服务有限公司合同专用章"
        model3!!.SealTypeName = "合同专用章"
        model3!!.isSelect = false
        model3!!.num = 0
        list!!.add(model3)
        var mode14: SealModel = SealModel()
        mode14!!.SealContent = "高春亮"
        mode14!!.SealTypeName = "法定人名专用章"
        mode14!!.isSelect = false
        mode14!!.num = 0
        list!!.add(mode14)
        //adapter!!.notifyDataSetChanged()
    }
}
