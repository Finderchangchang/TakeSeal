package dw.take.seal.control

import com.lzy.okgo.OkGo
import dw.take.seal.callback.JsonCallback
import dw.take.seal.callback.LzyResponse
import dw.take.seal.model.CodeModel
import dw.take.seal.model.ShopModel
import dw.take.seal.model.app_url
import okhttp3.Call
import okhttp3.Response
import java.util.*

/**
 *获取本辖区所有刻制社
 * Created by Administrator on 2017/8/4.
 */
class GetShopsListener{
    /**
     * 获得当前shop的列表
     * */
    fun getShops(main: GetShopsView) {
        OkGo.get(app_url.url_get_code + "GetShopCodes&ApplyChooseShop=true")
                .execute(object : JsonCallback<LzyResponse<CodeModel>>() {
                    override fun onSuccess(s: LzyResponse<CodeModel>, call: Call, response: Response) {
                        if (s.Success) {
                            if(s.Codes!=null){
                                main.GetshopResult(true,s.Codes as ArrayList<CodeModel>,"")
                            }else{
                                main.GetshopResult(true,null,s.Message)
                            }

                        }else{
                            main.GetshopResult(false,null,s.Message)
                        }
                    }
                    override fun onError(call: Call?, response: Response?, e: Exception?) {
                        super.onError(call, response, e)
                        var model:ArrayList<CodeModel>?=null;
                        main.GetshopResult(true,model,e!!.message.toString())
                    }
                })
    }
}
interface GetShopsView{
    fun GetshopResult(success:Boolean, list: ArrayList<CodeModel>?, mes:String)
}