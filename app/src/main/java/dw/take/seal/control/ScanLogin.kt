package dw.take.seal.control

import com.lzy.okgo.OkGo
import dw.take.seal.callback.JsonCallback
import dw.take.seal.callback.LzyResponse
import dw.take.seal.model.OrganizationModel
import dw.take.seal.model.app_url
import dw.take.seal.utils.Sha
import okhttp3.Call
import okhttp3.Response

/**
 * Created by lenovo on 2017/7/16.
 */
interface IScanLogin : IBaseInter {
    fun scan_result(model: LzyResponse<OrganizationModel>)
}

class ScanLogin {
    fun scan_login(json: String, login: IScanLogin) {
        var tel = "SystemLimited"
        var pwd = "BuiltInLimited"
        var login_type = "SystemAccount"//登录类型
        OkGo.get(app_url.normal_url+"/Service/User.aspx?Action=GetRandomNumber")
                .execute(object : JsonCallback<LzyResponse<String>>() {
                    override fun onSuccess(s: LzyResponse<String>, call: Call, response: Response) {
                        if (s.Success) {
                            val pwd = Sha.Sha1(Sha.Sha1(Sha.Sha1(pwd).toUpperCase()).toUpperCase() + s.RandomNumber.toUpperCase()).toUpperCase()
                            OkGo.post(app_url.normal_url+"/Service/User.aspx?Action=Login")
                                    .tag(this)
                                    .params("AccountId", tel)
                                    .params("Password", pwd)
                                    .params("AccountType", login_type)
                                    .execute(object : JsonCallback<LzyResponse<String>>() {
                                        override fun onSuccess(s: LzyResponse<String>, call: Call, response: Response) {
                                            if (s.Success) {
                                                scan_img(json,login)//执行扫描操作
                                            } else {
                                                login.error_toast("扫描失败，请联系工作人员")
                                            }
                                        }

                                        override fun onError(call: Call?, response: Response?, e: Exception?) {
                                            login.error_net()
                                        }
                                    })
                        }
                    }

                    override fun onError(call: Call?, response: Response?, e: Exception?) {
                        login.error_net()
                    }
                })
    }

    /**
     * 后台扫描图片内容
     * */
    fun scan_img(base64_json: String,login: IScanLogin) {
        OkGo.post(app_url.url_scan_img + "BusinessRecognition")
                .params("SerializeType", "Json")
                .params("Command", base64_json)
                .execute(object : JsonCallback<LzyResponse<OrganizationModel>>() {
                    override fun onSuccess(model: LzyResponse<OrganizationModel>, call: Call, response: Response) {
                        login.scan_result(model)
                    }

                    override fun onError(call: Call?, response: Response?, e: Exception?) {
                        login.error_net()
                    }
                })
    }
}