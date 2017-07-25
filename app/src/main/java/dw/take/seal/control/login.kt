package dw.take.seal.control

import android.app.Activity
import android.content.Context
import android.widget.Toast
import com.google.gson.Gson
import com.lzy.okgo.OkGo
import com.lzy.okgo.callback.StringCallback
import dw.take.seal.base.App
import dw.take.seal.callback.DialogCallback
import dw.take.seal.callback.JsonCallback
import dw.take.seal.callback.LzyResponse
import dw.take.seal.model.OrganizationModel
import dw.take.seal.model.ShopModel
import dw.take.seal.model.app_url
import dw.take.seal.utils.Sha
import okhttp3.Call
import okhttp3.Response
import java.util.*

/**
 * Created by lenovo on 2017/7/4.
 */
interface mLogin {
    fun load(result: Int)
}

interface mMain {
    fun show_user_info(user: OrganizationModel)
    fun show_shops(shops: List<ShopModel>)
    fun send_code_result(result: Boolean, toast: String)
}

interface mFour {
    fun show_shops4(shops: ArrayList<ShopModel>)
}

class login {
    /***
     *登录处理
     */
    fun user_login(tel: String, pwd: String, context: Activity, login: mLogin?) {
        var tel = tel
        var pwd = pwd
        var json = tel
        var login_type = "BusinessAccount"//登录类型
        OkGo.get(app_url.url_user + "GetRandomNumber")
                .execute(object : JsonCallback<LzyResponse<String>>() {
                    override fun onSuccess(s: LzyResponse<String>, call: Call, response: Response) {
                        if (s.Success) {
                            if (login == null) {
                                tel = "SystemInLimited"
                                pwd = "BuiltInLimited"
                                login_type = "SystemAccount"
                            }
                            val pwd = Sha.Sha1(Sha.Sha1(Sha.Sha1(pwd).toUpperCase()).toUpperCase() + s.RandomNumber.toUpperCase()).toUpperCase()
                            OkGo.post(app_url.url_user + "Login")
                                    .tag(this)
                                    .params("AccountId", tel)
                                    .params("Password", pwd)
                                    .params("AccountType", login_type)
                                    .execute(object : DialogCallback<LzyResponse<String>>(context) {
                                        override fun onSuccess(s: LzyResponse<String>, call: Call, response: Response) {
                                            if (s.Success) {
                                                if (login != null) {
                                                    login!!.load(1)
                                                } else {
                                                    scan_img(json)//执行扫描操作
                                                }
                                                toast("登录成功", context)
                                            } else {
                                                toast("用户名或者密码不正确，请重新输入", context)
                                            }
                                        }

                                        override fun onError(call: Call?, response: Response?, e: Exception?) {
                                            login!!.load(3)
                                            toast("请检查网络连接", context)
                                        }
                                    })
                        }
                    }

                    override fun onError(call: Call?, response: Response?, e: Exception?) {
                        login!!.load(3)
                    }
                })
    }

    /**
     * 获得当前用户信息
     * */
    fun get_user_info(context: Activity, main: mMain) {
        val url = app_url.url_organizations + "GetOrganizations&ApplyType=1"
        OkGo.get(app_url.url_organizations + "GetOrganizations&ApplyType=1")
                .execute(object : StringCallback() {
                    override fun onSuccess(s: String, call: Call, response: Response) {
                        //var result=s
                        //result=s.replace("[{","[").replace("]}","]")
                        var res = Gson().fromJson(s, LzyResponse::class.java)
                        res.Organizations
                        main.show_user_info(res.Organizations!![0])
                    }

                    override fun onError(call: Call?, response: Response?, e: Exception?) {
                        super.onError(call, response, e)
                    }
                })
    }

    /**
     * 获得当前shop的列表
     * */
    fun getShops(context: Activity, main: mFour) {
        OkGo.get(app_url.url_get_shops + "GetContractShops")
                .execute(object : JsonCallback<LzyResponse<String>>() {
                    override fun onSuccess(s: LzyResponse<String>, call: Call, response: Response) {
                        if (s.Success) {
                            main.show_shops4(s.Shops!! as ArrayList<ShopModel>)
                        }
                    }
                })
    }

    /**
     * 发送验证码功能
     * */
    fun send_code(tel: String, main: mMain) {
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
     * 后台扫描图片内容
     * */
    fun scan_img(json: String) {
        OkGo.post(app_url.url_scan_img + "BusinessRecognition")
                .params("SerializeType", "Json")
                .params("Command", json)
                .execute(object : StringCallback() {
                    override fun onSuccess(s: String, call: Call, response: Response) {
                        val a = ""
                        Toast.makeText(App.context, s, Toast.LENGTH_SHORT).show()
                    }

                    override fun onError(call: Call?, response: Response?, e: Exception?) {
                        super.onError(call, response, e)
                    }
                })
    }

    /**
     * toast内容
     * */
    fun toast(con: String, context: Activity) {
        Toast.makeText(context, con, Toast.LENGTH_SHORT).show()
    }

}