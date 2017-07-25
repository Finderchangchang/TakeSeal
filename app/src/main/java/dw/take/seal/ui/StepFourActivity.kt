package dw.take.seal.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.widget.Toast
import dw.take.seal.R
import dw.take.seal.control.login
import dw.take.seal.control.mFour
import dw.take.seal.method.CommonAdapter
import dw.take.seal.method.CommonViewHolder
import dw.take.seal.model.SealTypeModel
import dw.take.seal.model.ShopModel
import kotlinx.android.synthetic.main.activity_step_three.*
import wai.gr.cla.base.BaseActivity
import java.util.*

class StepFourActivity : BaseActivity() ,mFour{
    override fun show_shops4(shops: ArrayList<ShopModel>) {
        seal_type_list=shops
        if(seal_type_list.size>0){
            seal_type_list[0].is_check=true
            seal_type_adapter!!.refresh(seal_type_list)
        }
    }
    //http://118.190.47.63:1399/Service/SealProcess.aspx?Action=ApplyFreshSealOnline
    internal var seal_type_list: MutableList<ShopModel> = ArrayList()//评论列表
    var seal_type_adapter: CommonAdapter<ShopModel>? = null//评论列表
    override fun initViews() {
        setContentView(R.layout.activity_step_four)
        seal_type_adapter = object : CommonAdapter<ShopModel>(this@StepFourActivity, seal_type_list, R.layout.item_shop) {
            override fun convert(holder: CommonViewHolder, model: ShopModel, position: Int) {
                holder.setText(R.id.company_name_tv, model.ShopName)
                holder.setText(R.id.company_address_tv, model.ShopAddress)
                holder.setOnClickListener(R.id.main_cb){//刷新checkbox点击状态
                    var seal=seal_type_list[position]
                    for(model in seal_type_list){
                        model.is_check=false
                    }
                    seal.is_check=true
                    seal_type_adapter!!.refresh(seal_type_list)
                }
            }
        }
        main_lv.adapter = seal_type_adapter
        login().getShops(this,this)//获得所有shop信息
    }


    override fun initEvents() {
    }
}
