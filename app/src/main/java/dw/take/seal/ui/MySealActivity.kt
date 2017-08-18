package dw.take.seal.ui

import android.content.Intent
import android.support.design.widget.Snackbar
import android.text.TextUtils
import android.widget.Toast
import com.kaopiz.kprogresshud.KProgressHUD
import dw.take.seal.R
import dw.take.seal.control.GetSpecificationCodesListener
import dw.take.seal.control.GetSpecificationCodesView
import dw.take.seal.method.CommonAdapter
import dw.take.seal.method.CommonViewHolder
import dw.take.seal.model.CodeModel
import dw.take.seal.model.OrganizationJianModel
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
    var isfaren: Boolean = true
    //印章类型
    var list: ArrayList<SealModel>? = null
    var adapter: CommonAdapter<SealModel>? = null
    //当前点击的item的值
    var click_position: Int = 0
    //材质
    var speList: ArrayList<CodeModel>? = null
    var orgs: MutableList<OrganizationJianModel>? = null//营业执照信息
    //进度框
    var pdialog: KProgressHUD? = null
    //印章规格
    var code1: CodeModel? = null
    var code2: CodeModel? = null
    var code3: CodeModel? = null
    var code4: CodeModel? = null
    var code5: CodeModel? = null

    override fun GetSpecificationCodesResult(success: Boolean, list: ArrayList<CodeModel>?, mes: String) {
        //印章规格
        if (success) {
            if (mes.equals("2")) {
                speList = list
                GetSpecificationCodesListener().getSpecificationSeal(this, "1", "01")
            } else {
                if(list!=null&&list.size>0) {
                    if (mes.equals("01") ) {
                        code1 = list!![0]
                        GetSpecificationCodesListener().getSpecificationSeal(this, "1", "02")
                    } else if (mes.equals("02")) {
                        code2 = list!![0]
                        GetSpecificationCodesListener().getSpecificationSeal(this, "1", "03")
                    } else if (mes.equals("03")) {
                        code3 = list!![0]
                        GetSpecificationCodesListener().getSpecificationSeal(this, "1", "04")
                    } else if (mes.equals("04")) {
                        code4 = list!![0]
                        GetSpecificationCodesListener().getSpecificationSeal(this, "1", "05")
                    } else if (mes.equals("05")) {
                        code5 = list!![0]
                        if(pdialog!=null){
                            pdialog!!.dismiss()
                        }
                        LoadData()
                    }
                }else{
                    if(pdialog!=null){
                        pdialog!!.dismiss()
                    }

                }
            }
        }else{
            if(pdialog!=null){
                pdialog!!.dismiss()
            }
            toast(mes)
        }
    }


    override fun initViews() {
        setContentView(R.layout.activity_myseal)
        orgs = findb!!.findAll(OrganizationJianModel::class.java)
        isfaren = dw.take.seal.utils.Utils(this).ReadString(key.KEY_TAKESEAL_ISFAREN).equals("1")
        if (isfaren) {
            myseal_title.text = "第八步"
        } else {
            myseal_title.text = "第十步"
        }
        pdialog = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("识别中，请稍后")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
        pdialog!!.show()
        GetSpecificationCodesListener().getSpecificationSeal(this, "2", "")
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
                } else {
                    holder.setText(R.id.is_tv_caizhi, "材质：" + model.SealSpecificationName)
                }
                if (model.isSelect) {
                    holder.setImageResource(R.id.seal_ck, R.mipmap.xuanzhong)
                } else {
                    holder.setImageResource(R.id.seal_ck, R.mipmap.weixuanzhong)
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
        myseal_lv.setOnItemClickListener { adapterView, view, i, l ->
            if(i>0){
                if(list!![i].isSelect){
                    list!![i].isSelect=false
                    list!![i].num=0
                }else{
                    list!![i].isSelect=true
                    list!![i].num=1
                }
                adapter!!.refresh(list)
            }
        }
        select_close_btn.setOnClickListener {
            finish()
        }
        select_next_btn.setOnClickListener {
            if (list!![0].SealGGId.equals("") || list!![0].SealSpecificationId.equals("")) {
                toast("请先选择印章类型")
            } else {
                findb!!.deleteAll(SealModel::class.java)
                for (i in 0..list!!.size - 1) {
                    if (list!![i].num > 0) {
                        findb!!.save(list!![i])
                    }
                }
                startActivity(Intent(this@MySealActivity, ShopListActivity::class.java))
            }
        }
    }

    override fun onDestroy() {
        findb!!.deleteAll(SealModel::class.java)
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1 && resultCode == 1) {
            var code = data!!.getSerializableExtra("select_model") as SealModel
            list!![click_position] = code
            adapter!!.refresh(list)
        }
    }

    fun LoadData() {
        list = ArrayList()
        if (speList != null && speList!!.size > 0) {
            if (orgs!!.size > 0) {
                var org: OrganizationJianModel = orgs!![0]
                var model: SealModel = SealModel()
                model!!.SealContent = org.organizationName
                model!!.SealTypeName = "单位专用章"
                model!!.SealType = "01"
                model!!.isSelect = true
                model!!.num = 1
                model!!.SealSpecificationId = speList!![0].Key
                model!!.SealSpecificationName = speList!![0].Value
                if(code1!=null){
                    model!!.SealGGId=code1!!.Key
                    model!!.SealGGName=code1!!.Value
                }
                list!!.add(model)
                var model1: SealModel = SealModel()
                model1!!.SealContent = org.organizationName + " 财务专用章"
                model1!!.SealTypeName = "财务专用章"
                model1!!.isSelect = false
                model1!!.SealType = "02"
                model1!!.SealSpecificationId = speList!![0].Key
                model1!!.SealSpecificationName = speList!![0].Value
                model1!!.num = 0
                if(code2!=null){
                    model1!!.SealGGId=code2!!.Key
                    model1!!.SealGGName=code2!!.Value
                }
                list!!.add(model1)
                var model2: SealModel = SealModel()
                model2!!.SealContent = org.organizationName + " 发票专用章"
                model2!!.SealTypeName = "发票专用章"
                model2!!.isSelect = false
                model2!!.num = 0
                model2!!.SealSpecificationId = speList!![0].Key
                model2!!.SealSpecificationName = speList!![0].Value
                model2!!.SealType = "03"
                if(code3!=null){
                    model2!!.SealGGId=code3!!.Key
                    model2!!.SealGGName=code3!!.Value
                }
                list!!.add(model2)
                var model3: SealModel = SealModel()
                model3!!.SealContent = org.organizationName + " 合同专用章"
                model3!!.SealTypeName = "合同专用章"
                model3!!.isSelect = false
                model3!!.SealType = "04"
                model3!!.SealSpecificationId = speList!![0].Key
                model3!!.SealSpecificationName = speList!![0].Value
                model3!!.num = 0
                if(code3!=null){
                    model3!!.SealGGId=code3!!.Key
                    model3!!.SealGGName=code3!!.Value
                }
                list!!.add(model3)
                var mode14: SealModel = SealModel()
                mode14!!.SealContent = org.organizationLeader + "印"
                mode14!!.SealTypeName = "法定人名专用章"
                mode14!!.isSelect = false
                mode14!!.num = 0
                mode14!!.SealType = "05"
                mode14!!.SealSpecificationId = speList!![0].Key
                mode14!!.SealSpecificationName = speList!![0].Value
                if(code4!=null){
                    mode14!!.SealGGId=code4!!.Key
                    mode14!!.SealGGName=code4!!.Value
                }
                list!!.add(mode14)
                adapter!!.refresh(list)
            } else {
                toast("数据加载失败")
            }
        } else {
            toast("数据加载失败")
        }
    }
}
