package dw.take.seal.control

import com.lzy.okgo.OkGo
import com.lzy.okgo.callback.StringCallback
import dw.take.seal.model.ShopModel
import dw.take.seal.model.app_url
import okhttp3.Call
import okhttp3.Response

/**
 * 获取刻制社详细信息
 * Created by Administrator on 2017/8/4.
 */
class GetShopInfoListener {
    fun Getshopinfo(main:GetShopView,shopid:String) {
        OkGo.get(app_url.url_get_shop + "GetShop")
                .params("ShopId",shopid)
                .params("ContainConfig",false)
                .execute(object : StringCallback() {
                    override fun onSuccess(t: String?, call: Call?, response: Response?) {
                        var s = t;
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                });
//                .execute(object : JsonCallback<LzyResponse<String>>() {
//                    override fun onSuccess(s: LzyResponse<String>, call: Call, response: Response) {
//                        if (s.Success) {
//                            main.show_shops4(s.Shops!! as ArrayList<ShopModel>)
//                        }
//                    }
//
//                    override fun onError(call: Call?, response: Response?, e: Exception?) {
//                        super.onError(call, response, e)
//                        var model:ArrayList<ShopModel>?=null;
//                        main.show_shops4(model!!)
//                    }
//                })

    }
}

    interface GetShopView {
        fun GetShopResult(success: Boolean, shop: ShopModel, mes: String)
    }