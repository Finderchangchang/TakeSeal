package dw.take.seal.ui

import android.content.Intent
import dw.take.seal.R
import dw.take.seal.control.GetShopsListener
import dw.take.seal.control.GetShopsView
import dw.take.seal.method.CommonAdapter
import dw.take.seal.method.CommonViewHolder
import dw.take.seal.model.CardInfoModel
import dw.take.seal.model.CodeModel
import dw.take.seal.model.ShopModel
import kotlinx.android.synthetic.main.activity_face.*
import kotlinx.android.synthetic.main.activity_step_four.*
import kotlinx.android.synthetic.main.activity_step_three.*
import wai.gr.cla.base.BaseActivity
import wai.gr.cla.model.key
import java.util.*

class ShopListActivity : BaseActivity(), GetShopsView {
    override fun GetshopResult(success: Boolean, list: ArrayList<CodeModel>?, mes: String) {
        if (list != null) {
            seal_type_list = list
            if (seal_type_list.size > 0) {
                seal_type_list[0].is_check = true

                var code:CodeModel= CodeModel()
                code.Key="1"
                code.Value="测试数据"
                code.Parameter="测试数据（**）"
                code.is_check=false
                seal_type_list.add(code)
                seal_type_adapter!!.refresh(seal_type_list)
            }
        } else {
            toast(mes)
        }
    }

    //http://118.190.47.63:1399/Service/SealProcess.aspx?Action=ApplyFreshSealOnline
    internal var seal_type_list: ArrayList<CodeModel> = ArrayList()//评论列表
    var seal_type_adapter: CommonAdapter<CodeModel>? = null//评论列表
    var isfaren: Boolean = true
    var selectindex:Int=0;
    override fun initViews() {
        setContentView(R.layout.activity_step_four)
        isfaren = dw.take.seal.utils.Utils(this).ReadString(key.KEY_TAKESEAL_ISFAREN).equals("1")
        if (isfaren) {
            shop_list_title.text="第九步"
        } else {
            shop_list_title.text="第十一步"
        }
        seal_type_adapter = object : CommonAdapter<CodeModel>(this@ShopListActivity, seal_type_list, R.layout.item_shop) {
            override fun convert(holder: CommonViewHolder, model: CodeModel, position: Int) {
                holder.setText(R.id.company_name_tv, model.Value)
                holder.setText(R.id.company_address_tv, model.Parameter)
                if(model.is_check){
                    holder.setImageResource(R.id.main_cb,R.mipmap.xuanzhong)
                }else{
                    holder.setImageResource(R.id.main_cb,R.mipmap.weixuanzhong)
                }


                holder.setOnClickListener(R.id.shop_ll) {
                    //刷新checkbox点击状态
                    selectindex=position
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
            dw.take.seal.utils.Utils(this).WriteString(key.KEY_SHOP_ID,seal_type_list[selectindex].Key)
            val intent = Intent(this@ShopListActivity, ShowInfoActivity::class.java)
            startActivity(intent)
        }
    }


    override fun initEvents() {
    }
}
