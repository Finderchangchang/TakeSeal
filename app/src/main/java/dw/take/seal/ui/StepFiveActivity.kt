package dw.take.seal.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import dw.take.seal.R
import dw.take.seal.control.GetShopInfoListener
import dw.take.seal.control.GetShopView
import dw.take.seal.model.ShopModel
import wai.gr.cla.base.BaseActivity

/***
 * 提交完成以后的处理结果
 */
class StepFiveActivity : BaseActivity(),GetShopView {
    override fun GetShopResult(success: Boolean, shop: ShopModel, mes: String) {
        //获取刻制社详细信息
    }

    override fun initViews() {
        setContentView(R.layout.activity_step_five)
        GetShopInfoListener().Getshopinfo(this,"shopid")
    }

    override fun initEvents() {

    }
}
