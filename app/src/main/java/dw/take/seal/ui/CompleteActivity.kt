package dw.take.seal.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import dw.take.seal.R
import dw.take.seal.control.GetShopInfoListener
import dw.take.seal.control.GetShopView
import dw.take.seal.method.CommonAdapter
import dw.take.seal.method.CommonViewHolder
import kotlinx.android.synthetic.main.activity_complete.*
import kotlinx.android.synthetic.main.activity_show_info.*
import wai.gr.cla.base.BaseActivity
import wai.gr.cla.method.Utils
import wai.gr.cla.model.key
import java.text.SimpleDateFormat
import java.util.*
import android.content.Intent
import dw.take.seal.base.App.Companion.context
import dw.take.seal.model.*


//申请完成
class CompleteActivity : BaseActivity(), GetShopView {
    var adapter: CommonAdapter<SealModel>? = null
    var list: MutableList<SealModel>? = null
    var isFa: Boolean = true
    override fun GetShopResult(success: Boolean, shop: ShopModel, mes: String) {
        if (success) {
            complete_mobile.text = shop.ShopLeaderMobileNumber
            complete_kaddress.text = shop.ShopAddress
            complete_kname.text = shop.ShopName
            dw.take.seal.utils.Utils(this).WriteString(key.KEY_SHOP_PHONE,shop.ShopLeaderMobileNumber)
            dw.take.seal.utils.Utils(this).WriteString(key.KEY_SHOP_ADDRESS,shop.ShopAddress)
        } else {
            toast(mes)
        }
    }

    override fun initEvents() {
        list = findb!!.findAll(SealModel::class.java)
        var orgs: MutableList<OrganizationJianModel>? = findb!!.findAll(OrganizationJianModel::class.java)
        var where = "true"
        isFa = dw.take.seal.utils.Utils(this).ReadString(key.KEY_TAKESEAL_ISFAREN).equals("1")
        if (isFa) {
            where = "true"
            complete_tvname.text = "法       人："
        } else {
            where = "false"
            complete_tvname.text = "经   办  人："
        }
        adapter = object : CommonAdapter<SealModel>(this@CompleteActivity, list, R.layout.item_seal_info) {
            override fun convert(holder: CommonViewHolder?, t: SealModel?, position: Int) {
                holder!!.setText(R.id.item_seal_info_type, t!!.SealTypeName)
                holder!!.setText(R.id.item_seal_info_cz, t!!.SealGGName)
                holder!!.setText(R.id.item_seal_info_num, t!!.num.toString())
                holder!!.setText(R.id.item_seal_info_gg, t!!.SealSpecificationName)
            }
        }
        //加载法人信息
        var cards: MutableList<CardInfoModel>? = findb!!.findAllByWhere(CardInfoModel::class.java, "isFaren='" + where + "'")
        if (cards!!.size > 0) {
            complete_person.text = cards[0].personName
        }
        if (orgs!!.size > 0) {
            complete_cname.text = orgs[0].organizationName
        } else {
            toast("加载数据失败")
            finish()
        }
        complete_date.text = getNowDate()
        complete_lv.adapter = adapter

        //清楚数据库和缓存数据
        findb!!.deleteAll(CardInfoModel::class.java)
        findb!!.deleteAll(SealModel::class.java)
        findb!!.deleteAll(OrganizationJianModel::class.java)
        findb!!.deleteAll(ApplySealCertificateData::class.java)

        //关闭，退出程序
        select_close_btn.setOnClickListener {
//            val intent = Intent()
//            intent.action = "action.exit"
//            context!!.sendBroadcast(intent)
            val intent = Intent(this@CompleteActivity, ShowZhiFuActivity::class.java)
            startActivity(intent)
        }
    }

    override fun initViews() {
        setContentView(R.layout.activity_complete)
        GetShopInfoListener().Getshopinfo(this, dw.take.seal.utils.Utils(this).ReadString(key.KEY_SHOP_ID))
    }

    //获取当前日期
    fun getNowDate(): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        val curDate = Date(System.currentTimeMillis())//获取当前时间
        val str = formatter.format(curDate)
        return str
    }
}
