package dw.take.seal.ui

import android.content.Intent
import android.support.design.widget.Snackbar
import android.text.TextUtils
import android.widget.Toast
import dw.take.seal.R
import dw.take.seal.control.GetSpecificationCodesListener
import dw.take.seal.control.GetSpecificationCodesView
import dw.take.seal.method.CommonAdapter
import dw.take.seal.method.CommonViewHolder
import dw.take.seal.model.CodeModel
import dw.take.seal.model.SealModel
import kotlinx.android.synthetic.main.activity_myseal.*
import kotlinx.android.synthetic.main.activity_step_four.*
import wai.gr.cla.base.BaseActivity
import wai.gr.cla.model.key
import java.util.*

/**
 * 我的印章
 * Created by Administrator on 2017/8/4.
 */

class MySealActivity : BaseActivity(), GetSpecificationCodesView {
    var selectType: String = "01"
    var guige: String = ""
    var isfaren: Boolean = true

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
        isfaren = dw.take.seal.utils.Utils(this).ReadString(key.KEY_TAKESEAL_ISFAREN).equals("1")
        if (isfaren) {
            myseal_title.text="第八步"
        } else {
            myseal_title.text="第十步"
        }
        LoadData()
        //GetSpecificationCodesListener().getSpecificationSeal(this, selectType)
    }

    override fun initEvents() {
        adapter = object : CommonAdapter<SealModel>(this@MySealActivity, list, R.layout.item_seal) {
            override fun convert(holder: CommonViewHolder, model: SealModel, position: Int) {
                holder.setText(R.id.is_tv_type, model.SealTypeName)
                holder.setText(R.id.is_tv_num, "数量：" + model.num)
                if (TextUtils.isEmpty(model.SealGGName)) {
                    holder.setText(R.id.is_tv_guige, "规格：未选择")
                } else {
                    holder.setText(R.id.is_tv_guige, "规格：" + model.SealGGName)
                }
                if (TextUtils.isEmpty(model.SealContent)) {
                    holder.setText(R.id.is_tv_content, "内容：未选择")
                } else {
                    holder.setText(R.id.is_tv_content, "内容：" + model.SealContent)
                }
                if (TextUtils.isEmpty(model.SealSpecificationName)) {
                    holder.setText(R.id.is_tv_caizhi, "材质：未选择")
                }else{
                    holder.setText(R.id.is_tv_caizhi, "材质：" + model.SealSpecificationName)
                }
                if(model.isSelect){
                    holder.setImageResource(R.id.seal_ck,R.mipmap.xuanzhong)
                }else{
                    holder.setImageResource(R.id.seal_ck,R.mipmap.weixuanzhong)
                }
                holder.setOnClickListener(R.id.is_tv_edit) {
                    click_position = position//当前点击的列表位置
                    //跳转到编辑界面
                    startActivityForResult(Intent(this@MySealActivity, SelectSealActivity::class.java).putExtra("SealModel", model), 1)
                }
                holder.setOnClickListener(R.id.seal_ck) {
                    //选择
                    list!![position].isSelect = !model.isSelect
                }
            }
        }
        myseal_lv.adapter = adapter
        select_close_btn.setOnClickListener {
            //findb!!.deleteAll(SealModel::class.java)
            finish()
        }
        select_next_btn.setOnClickListener {
            if(list!![0].SealGGId.equals("")||list!![0].SealSpecificationId.equals("")||list!![1].SealGGId.equals("")||list!![1].SealSpecificationId.equals("")) {
                toast("请先选择印章类型")

            }else{
                for(i in 0..list!!.size-1){
                    if(list!![i].num>0){
                        findb!!.save(list!![i])
                    }
                }
                startActivity(Intent(this@MySealActivity, ShopListActivity::class.java))
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == 1 && resultCode == 1) {
//            var code = data!!.getSerializableExtra("code") as CodeModel
//            var specifi = data!!.getSerializableExtra("specifi") as CodeModel
//            var num = data!!.getStringExtra("num")
//            list!![click_position].SealGGId = code.Key
//            list!![click_position].SealGGName = code.Value
//            list!![click_position].SealSpecificationId = specifi.Key
//            list!![click_position].SealSpecificationName = specifi.Value
//            list!![click_position].num = num.toInt()
//            list!![click_position].isSelect = true
            var code = data!!.getSerializableExtra("select_model") as SealModel
            list!![click_position] = code
            adapter!!.refresh(list)
        }
    }

    fun LoadData() {
        list = ArrayList()
        var model: SealModel = SealModel()
        model!!.SealContent = "保定亮达机电设备安装服务有限公司"
        model!!.SealTypeName = "单位专用章"
        model!!.SealType="01"
        model!!.isSelect = true
        model!!.num = 1
        list!!.add(model)
        var model1: SealModel = SealModel()
        model1!!.SealContent = "保定亮达机电设备安装服务有限公司财务专用章"
        model1!!.SealTypeName = "财务专用章"
        model1!!.isSelect = true
        model1!!.SealType="02"
        model1!!.num = 1
        list!!.add(model1)
        var model2: SealModel = SealModel()
        model2!!.SealContent = "保定亮达机电设备安装服务有限公司发票专用章"
        model2!!.SealTypeName = "发票专用章"
        model2!!.isSelect = false
        model2!!.num = 0
        model2!!.SealType="03"
        list!!.add(model2)
        var model3: SealModel = SealModel()
        model3!!.SealContent = "保定亮达机电设备安装服务有限公司合同专用章"
        model3!!.SealTypeName = "合同专用章"
        model3!!.isSelect = false
        model3!!.SealType="04"
        model3!!.num = 0
        list!!.add(model3)
        var mode14: SealModel = SealModel()
        mode14!!.SealContent = "高春亮"
        mode14!!.SealTypeName = "法定人名专用章"
        mode14!!.isSelect = false
        mode14!!.num = 0
        mode14!!.SealType="05"
        list!!.add(mode14)

    }
}
