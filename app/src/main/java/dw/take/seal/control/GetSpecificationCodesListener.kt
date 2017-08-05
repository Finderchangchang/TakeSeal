package dw.take.seal.control

import com.lzy.okgo.OkGo
import dw.take.seal.callback.JsonCallback
import dw.take.seal.callback.LzyResponse
import dw.take.seal.model.CodeModel
import dw.take.seal.model.app_url
import okhttp3.Call
import okhttp3.Response
import java.util.*

/**
 * 获取印章规格
 * Created by Administrator on 2017/8/4.
 */
class GetSpecificationCodesListener{
    /**
     * 获得当前印章规格的列表
     * */
    fun getSpecificationSeal(main: GetSpecificationCodesView,type:String) {
        OkGo.get(app_url.url_get_code + "GetSpecificationCodes&sealType="+type)
                .execute(object : JsonCallback<LzyResponse<CodeModel>>() {
                    override fun onSuccess(s: LzyResponse<CodeModel>, call: Call, response: Response) {
                        if (s.Success) {
                            main.GetSpecificationCodesResult(true,s.Codes as ArrayList<CodeModel>,type)
                        }else{
                            main.GetSpecificationCodesResult(false,null,s.Message)
                        }
                    }

                    override fun onError(call: Call?, response: Response?, e: Exception?) {
                        super.onError(call, response, e)
                        var model:ArrayList<CodeModel>?=null;
                        main.GetSpecificationCodesResult(true,model,e!!.message.toString())
                    }
                })
    }

}
interface GetSpecificationCodesView{
    fun GetSpecificationCodesResult(success:Boolean,list:ArrayList<CodeModel>?,mes:String)
}