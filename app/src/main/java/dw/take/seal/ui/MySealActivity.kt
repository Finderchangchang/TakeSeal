package dw.take.seal.ui

import android.content.Intent
import android.support.design.widget.Snackbar
import android.widget.Toast
import dw.take.seal.R
import dw.take.seal.control.GetSpecificationCodesListener
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
    var selectType: String = "01"
    var guige: String = ""

    override fun GetSpecificationCodesResult(success: Boolean, list: ArrayList<CodeModel>?, mes: String) {
        //印章规格
        if (success) {
            if (mes.equals("01")) {
                // list[1].
            }
        }
    }

    var list: ArrayList<SealModel>? = null
    var adapter: CommonAdapter<SealModel>? = null
    var click_position: Int = 0
    override fun initViews() {
        setContentView(R.layout.activity_myseal)
        LoadData()
        //GetSpecificationCodesListener().getSpecificationSeal(this, selectType)
    }

    override fun initEvents() {
        adapter = object : CommonAdapter<SealModel>(this@MySealActivity, list, R.layout.item_seal) {
            override fun convert(holder: CommonViewHolder, model: SealModel, position: Int) {
                holder.setText(R.id.is_tv_type, model.SealTypeName)
                holder.setText(R.id.is_tv_num, "数量：" + model.num)
                holder.setText(R.id.is_tv_guige, "规格：" + "40行政章")
                holder.setText(R.id.is_tv_content, "内容：" + model.SealContent)
                holder.setText(R.id.is_tv_caizhi, "材质：未设置")
                holder.setSelect(R.id.seal_ck, model.isSelect)
                if (position == 0 || position == 1) {//控制checkbox是否可以点击
                    holder.setEnable(R.id.seal_ck, false)
                } else {
                    holder.setEnable(R.id.seal_ck, true)
                }
                holder.setOnClickListener(R.id.is_tv_edit) {
                    click_position = position//当前点击的列表位置
                    //跳转到编辑界面
                    startActivityForResult(Intent(this@MySealActivity, SelectSealActivity::class.java).putExtra("SealModel", model), 0)
                }
                holder.setOnClickListener(R.id.seal_ck) {
                    //选择
                    list!![position].isSelect = !model.isSelect
                }
            }
        }
        myseal_lv.adapter = adapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //选择完以后的处理结果
        if (requestCode == 0 && resultCode == 1) {
            var code = data!!.getSerializableExtra("code") as CodeModel
            var specifi = data!!.getSerializableExtra("specifi") as CodeModel
            var num = data!!.getStringExtra("num")
            list!![click_position].SealSpecificationId = specifi.Key
            list!![click_position].SealSpecificationName = specifi.Value
            list!![click_position].SealSpecificationId = specifi.Key
            list!![click_position].SealSpecificationName = specifi.Value
        }
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
