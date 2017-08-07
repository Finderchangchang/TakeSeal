package dw.take.seal.ui

import android.content.Intent
import dw.take.seal.R
import dw.take.seal.control.GetShopsListener
import dw.take.seal.control.GetShopsView
import dw.take.seal.method.CommonAdapter
import dw.take.seal.method.CommonViewHolder
import dw.take.seal.model.CodeModel
import dw.take.seal.model.ShopModel
import kotlinx.android.synthetic.main.activity_step_four.*
import kotlinx.android.synthetic.main.activity_step_three.*
import wai.gr.cla.base.BaseActivity
import java.util.*

class ShopListActivity : BaseActivity(), GetShopsView {
    override fun GetshopResult(success: Boolean, list: List<CodeModel>?, mes: String) {
        if (list != null) {
            seal_type_list = list
            if (seal_type_list.size > 0) {
                seal_type_list[0].is_check = true
                seal_type_adapter!!.refresh(seal_type_list)
            }
        } else {
            toast("数据错误")
        }
    }

    //http://118.190.47.63:1399/Service/SealProcess.aspx?Action=ApplyFreshSealOnline
    internal var seal_type_list: List<CodeModel> = ArrayList()//评论列表
    var seal_type_adapter: CommonAdapter<CodeModel>? = null//评论列表
    override fun initViews() {
        setContentView(R.layout.activity_step_four)
        seal_type_adapter = object : CommonAdapter<CodeModel>(this@ShopListActivity, seal_type_list, R.layout.item_shop) {
            override fun convert(holder: CommonViewHolder, model: CodeModel, position: Int) {
                holder.setText(R.id.company_name_tv, model.Value)
                holder.setText(R.id.company_address_tv, model.Parameter)
                holder.setOnClickListener(R.id.shop_ll) {
                    //刷新checkbox点击状态
                    var seal = seal_type_list[position]
                    var i: Int = 0;
                    for (model in seal_type_list) {
                        seal_type_list[i].is_check = false
                        i++
                    }
                    seal_type_list[position].is_check = true
                    seal_type_adapter!!.refresh(seal_type_list)
                }
            }
        }
        shop_main_lv.adapter = seal_type_adapter
        //请求刻制社列表
        GetShopsListener().getShops(this);
        shop_close_btn.setOnClickListener { finish() }
        shop_next_btn.setOnClickListener {
            //信息展示
            val intent = Intent(this@ShopListActivity, ShopListActivity::class.java)
            intent.putExtra("OrgModel","")
            startActivity(intent)
        }
    }


    override fun initEvents() {
    }
}
