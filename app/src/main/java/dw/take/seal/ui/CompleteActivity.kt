package dw.take.seal.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import dw.take.seal.R
import dw.take.seal.control.GetShopInfoListener
import dw.take.seal.control.GetShopView
import dw.take.seal.method.CommonAdapter
import dw.take.seal.model.CardInfoModel
import dw.take.seal.model.OrganizationJianModel
import dw.take.seal.model.SealModel
import dw.take.seal.model.ShopModel
import kotlinx.android.synthetic.main.activity_complete.*
import kotlinx.android.synthetic.main.activity_show_info.*
import wai.gr.cla.base.BaseActivity
import wai.gr.cla.method.Utils
import wai.gr.cla.model.key
import java.text.SimpleDateFormat
import java.util.*

//申请完成
class CompleteActivity : BaseActivity(), GetShopView {
    var regionid: String = ""
    var adapter: CommonAdapter<SealModel>? = null
    var list: MutableList<SealModel>? = null
    var isFa: Boolean = true
    override fun GetShopResult(success: Boolean, shop: ShopModel, mes: String) {
        if (success) {
            complete_mobile.text = shop.ShopLeaderMobileNumber
            complete_kaddress.text = shop.ShopAddress
            complete_kname.text = shop.ShopName
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
            complete_tvname.text = "法      人："
        } else {
            where = "false"
            complete_tvname.text = "经  办  人："
        }
        //加载法人信息
        var cards: MutableList<CardInfoModel>? = findb!!.findAllByWhere(CardInfoModel::class.java, "isfaren=" + where + "")
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
