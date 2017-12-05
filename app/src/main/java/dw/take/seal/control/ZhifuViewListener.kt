package dw.take.seal.control

import com.lzy.okgo.OkGo
import com.lzy.okgo.callback.BitmapCallback
import dw.take.seal.model.app_url
import okhttp3.Call
import okhttp3.Response
import android.graphics.Bitmap
import dw.take.seal.callback.JsonCallback
import dw.take.seal.callback.LzyResponse


/**
 * Created by Administrator on 2017/12/1.
 */
interface ZhifuView {
    fun zhifu_result(result: Boolean, mes: String,bitmap:Bitmap?)
    fun weixin_result(result: Boolean, mes: String,bitmap:Bitmap?)
    fun price_result(result: Boolean, mes: String?,price:Double?)
}
class ZhifuViewListener {
    //获取支付宝图片信息
    fun GetImageWeChatQRCode(shopid: String, main: ZhifuView) {
        OkGo.get(app_url.url_get_shop + "GetImageWeChatQRCode&documentType=WeChatQRCodeImagePreview&DocumentId="+shopid)
                .execute(object : BitmapCallback() {
                    override fun onSuccess(bitmap: Bitmap, call: Call, response: Response) {
                        main.weixin_result(true,"",bitmap)
                    }

                    override fun onError(call: Call?, response: Response?, e: Exception?) {
                            main.weixin_result(false, e!!.message!!,null)
                    }
                })
    }
    fun GetImageAlipayQRCode(shopid: String, main: ZhifuView){
        OkGo.get(app_url.url_get_shop + "GetImageAlipayQRCode&documentType=AlipayQRCodeImagePreview&DocumentId="+shopid)
                .execute(object : BitmapCallback() {
                    override fun onSuccess(bitmap: Bitmap, call: Call, response: Response) {
                        main.zhifu_result(true,"",bitmap)
                    }

                    override fun onError(call: Call?, response: Response?, e: Exception?) {
                        main.zhifu_result(false, e!!.message!!,null)
                    }
                })
    }
    fun GetSumarySealPrice(SealGroupId :String,main:ZhifuView){
        OkGo.post(app_url.url_get_code+"GetSumarySealPrice")
                .params("SealGroupId",SealGroupId)
                .execute(object : JsonCallback<LzyResponse<String>>() {
                    override fun onSuccess(t: LzyResponse<String>?, call: Call?, response: Response?) {
                        if(t!!.Success){
                            main.price_result(t!!.Success,"",t.SumaryPrice)
                        }else{
                            main.price_result(false,t.Message,0.0)
                        }
                    }
                    override fun onError(call: Call?, response: Response?, e: Exception?) {
                        super.onError(call, response, e)
                        main.price_result(false,"数据错误:"+e!!.message,0.0)
                    }
                })
    }
}