package dw.take.seal.control

import com.google.gson.Gson
import com.lzy.okgo.OkGo
import com.lzy.okgo.callback.StringCallback
import dw.take.seal.callback.JsonCallback
import dw.take.seal.callback.LzyResponse
import dw.take.seal.model.ShopModel
import dw.take.seal.model.app_url
import okhttp3.Call
import okhttp3.Response
import java.util.*

/**
 * 获取刻制社详细信息
 * Created by Administrator on 2017/8/4.
 */
class GetShopInfoListener {
    fun Getshopinfo(main: GetShopView, shopid: String) {
        OkGo.get(app_url.url_get_shop + "GetShop")
                .params("ShopId", shopid)
                .params("ContainConfig", false)
//                .execute(object : StringCallback() {
//                    override fun onSuccess(s: String, call: Call, response: Response) {
//                        var result=s
//                        //result=s.replace("[{","[").replace("]}","]")
//                    }
//
//                    override fun onError(call: Call?, response: Response?, e: Exception?) {
//                        super.onError(call, response, e)
//                    }
//                })
                .execute(object : JsonCallback<LzyResponse<ShopModel>>() {
                    override fun onSuccess(s: LzyResponse<ShopModel>, call: Call, response: Response) {
                        if (s.Success) {
                            if (s.Shop != null) {
                                main.GetShopResult(s.Success, s.Shop!!, "")
                            } else {
                                if (s.Message != null) {
                                    main.GetShopResult(s.Success, s.Shop!!, s.Message)
                                } else {
                                    main.GetShopResult(s.Success, s.Shop!!, "数据异常")
                                }
                            }

                        }
                    }

                    override fun onError(call: Call?, response: Response?, e: Exception?) {
                        super.onError(call, response, e)
                        var model: ShopModel? = null;
                        main.GetShopResult(false, model!!, e!!.message!!)
                    }
                })

    }
}

interface GetShopView {
    fun GetShopResult(success: Boolean, shop: ShopModel, mes: String)
}