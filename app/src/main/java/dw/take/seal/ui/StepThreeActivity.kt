package dw.take.seal.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.widget.Toast
import dw.take.seal.R
import dw.take.seal.method.CommonAdapter
import dw.take.seal.method.CommonViewHolder
import dw.take.seal.model.SealTypeModel
import kotlinx.android.synthetic.main.activity_step_three.*
import wai.gr.cla.base.BaseActivity
import java.util.*

/**
 * 第三布操作
 * */
class StepThreeActivity : BaseActivity() {
    internal var seal_type_list: MutableList<SealTypeModel> = ArrayList()//评论列表
    var seal_type_adapter: CommonAdapter<SealTypeModel>? = null//评论列表
    override fun initViews() {
        setContentView(R.layout.activity_step_three)
    }

    override fun initEvents() {
        seal_type_adapter = object : CommonAdapter<SealTypeModel>(this@StepThreeActivity, seal_type_list, R.layout.item_seal_type) {
            override fun convert(holder: CommonViewHolder, model: SealTypeModel, position: Int) {
                holder.setCbText(R.id.main_cb, model.seal_name, model.is_check)
                holder.setText(R.id.num_tv, model.count.toString())
                holder.setOnClickListener(R.id.main_cb){//刷新checkbox点击状态
                    var seal=seal_type_list[position]
                    seal.is_check=!seal.is_check
                    seal_type_adapter!!.refresh(seal_type_list)
                }
                holder.setOnClickListener(R.id.add_iv){
                    var seal=seal_type_list[position]
                    if(seal.count==10){
                        Snackbar.make(main_lv, "本组印章个数不能超过10个", Toast.LENGTH_SHORT).show()
                    }else {
                        seal.count++
                        seal_type_adapter!!.refresh(seal_type_list)
                    }
                }
                holder.setOnClickListener(R.id.del_iv){
                    var seal=seal_type_list[position]
                    if(seal.count>0) {
                        seal.count--
                        seal_type_adapter!!.refresh(seal_type_list)
                    }else{
                        Snackbar.make(main_lv, "当前数量为0", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        main_lv.adapter = seal_type_adapter
        seal_type_list.add(SealTypeModel("单位专用章", true, 1))
        seal_type_list.add(SealTypeModel("财务专用章", false, 1))
        seal_type_list.add(SealTypeModel("发票专用章", false, 1))
        seal_type_list.add(SealTypeModel("合同专用章", false, 1))
        seal_type_list.add(SealTypeModel("法定人名代表章", false, 1))
        seal_type_adapter!!.refresh(seal_type_list)
        next_btn.setOnClickListener {
            startActivity(Intent(this,StepFourActivity::class.java))
        }
    }
}
