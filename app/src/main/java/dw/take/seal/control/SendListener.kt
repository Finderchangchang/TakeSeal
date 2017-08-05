package dw.take.seal.control

import com.lzy.okgo.OkGo
import dw.take.seal.callback.JsonCallback
import dw.take.seal.callback.LzyResponse
import dw.take.seal.model.app_url
import okhttp3.Call
import okhttp3.Response

/**
 * Created by Administrator on 2017/8/4.
 */
class SendListener{
    /**
     * 发送验证码功能
     * */
    fun send_code(tel: String, main: SendCodeView) {
        OkGo.get(app_url.url_get_code_by_tel + "SendVerificationCode&MobileNumber=" + tel)
                .execute(object : JsonCallback<LzyResponse<String>>() {
                    override fun onSuccess(model: LzyResponse<String>, call: Call, response: Response) {
                        var toast_result = ""
                        if (model.Success) {
                            toast_result = "发送成功"
                        } else {
                            toast_result = "发送失败，请稍后重试"
                        }
                        main.send_code_result(model.Success, toast_result)
                    }

                    override fun onError(call: Call?, response: Response?, e: Exception?) {
                        main.send_code_result(false, "请检查网络连接")
                    }
                })
    }
    /**
     * 验证验证码功能
     * */
    fun Check_code(code: String, main: SendCodeView) {
        OkGo.get(app_url.url_get_code_by_tel + "CheckVerificationCode&VerificationCode=" + code)
                .execute(object : JsonCallback<LzyResponse<String>>() {
                    override fun onSuccess(model: LzyResponse<String>, call: Call, response: Response) {
                        var toast_result = ""
                        if (model.Success) {
                            toast_result = "验证码验证成功"
                        } else {
                            toast_result = model.Message!!
                        }
                        main.yan_code_result(model.Success, toast_result)
                    }

                    override fun onError(call: Call?, response: Response?, e: Exception?) {
                        main.yan_code_result(false, "请检查网络连接")
                    }
                })
    }
}
interface SendCodeView{
  fun send_code_result(result: Boolean, toast: String)
    fun yan_code_result(result: Boolean, toast: String)
}
