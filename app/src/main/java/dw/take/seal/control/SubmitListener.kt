package dw.take.seal.control

import android.widget.Toast
import com.lzy.okgo.OkGo
import com.lzy.okgo.callback.StringCallback
import dw.take.seal.base.App
import dw.take.seal.callback.JsonCallback
import dw.take.seal.callback.LzyResponse
import dw.take.seal.model.OrganizationJianModel
import dw.take.seal.model.app_url
import okhttp3.Call
import okhttp3.Response

/**
 * Created by Administrator on 2017/8/5.
 */

class SubmitListener {
    fun Submit(json: String,main:SubmitView) {
        OkGo.post(app_url.url_upload_img + "ApplySeal")
                .params("SerializeType", "Json")
                .params("Command", json)
                .execute(object : JsonCallback<LzyResponse<String>>() {
                    override fun onSuccess(t: LzyResponse<String>?, call: Call?, response: Response?) {
                        if(t!!.Message!=null){
                            main.SubmitResult(t!!.Success,t.Message)
                        }else {
                            main.SubmitResult(t!!.Success,"保存成功")
                        }
                    }
                    override fun onError(call: Call?, response: Response?, e: Exception?) {
                        super.onError(call, response, e)
                        main.SubmitResult(false,"数据错误:"+e!!.message)
                    }
                })
    }
    fun getCertifyNumber(main:SubmitView,ID:String){
        OkGo.get(app_url.url_get_certifynumber + "GetCertifyNumber&RegionId="+ID)
                .execute(object : JsonCallback<LzyResponse<String>>() {
                    override fun onSuccess(t: LzyResponse<String>?, call: Call?, response: Response?) {
                       if(t!!.CertifyNumber!=null){
                           main.getCertifyNumberResult(t.Success,t.CertifyNumber!!)
                       }else{
                           main.getCertifyNumberResult(t.Success,t.Message)

                       }
                    }

                    override fun onError(call: Call?, response: Response?, e: Exception?) {
                        super.onError(call, response, e)
                    }
                })
    }
}

interface SubmitView {
    fun SubmitResult(success: Boolean, result: String)
    fun getCertifyNumberResult(success: Boolean, result: String)
}
