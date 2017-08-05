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
class GetSpecificationCodesListener {
    /**
     * 获得当前印章规格的列表
     * @main this
     * @search_type 查询的字典类型（1，规格。2，材质）
     * @type 印章类型
     * */
    fun getSpecificationSeal(main: GetSpecificationCodesView, search_type: String, type: String) {
        var url = ""
        when (search_type) {
            "1" -> url = app_url.url_get_code + "GetSpecificationCodes&sealType=" + type
            else -> url = app_url.url_get_code + "GetCodes&CodeName=SealMaterial"
        }
        OkGo.get(url)
                .execute(object : JsonCallback<LzyResponse<CodeModel>>() {
                    override fun onSuccess(s: LzyResponse<CodeModel>, call: Call, response: Response) {
                        if (s.Success) {
                            main.GetSpecificationCodesResult(true,s.Codes as ArrayList<CodeModel>,"")
                        }else{
                            main.GetSpecificationCodesResult(false,s.Codes as ArrayList<CodeModel>,s.Message)
                        }
                    }

                    override fun onError(call: Call?, response: Response?, e: Exception?) {
                        super.onError(call, response, e)
                        main.error_net()
                    }
                })
    }

}

interface GetSpecificationCodesView : IBaseInter {
    /**
     * 获得字典列表
     * type 1.规格 2.材质
     * */
    fun GetSpecificationCodesResult(success: Boolean, list: ArrayList<CodeModel>?, type: String)
}