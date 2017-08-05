package dw.take.seal.control

import com.google.gson.Gson
import com.lzy.okgo.OkGo
import com.lzy.okgo.callback.StringCallback
import dw.take.seal.callback.JsonCallback
import dw.take.seal.callback.LzyResponse
import dw.take.seal.model.OrganizationJianModel
import dw.take.seal.model.OrganizationModel
import dw.take.seal.model.app_url
import dw.take.seal.utils.Sha
import okhttp3.Call
import okhttp3.Response

/**
 * 扫描二维码识别内容
 * Created by Administrator on 2017/7/28.
 */
interface IScan_result : IBaseInter {
    fun scan_result(istrue: Boolean, model: OrganizationJianModel, result: String)
}

class ScanCodeLogin {
    fun scan_login(url: String, scan: IScan_result) {
        OkGo.get(app_url.scan_code)
                .params("businessUrl", url)
                .execute(object : JsonCallback<LzyResponse<OrganizationJianModel>>() {
                    override fun onSuccess(model: LzyResponse<OrganizationJianModel>, call: Call, response: Response) {
                        if (model.Data == null) {
                            scan.scan_result(false, OrganizationJianModel(), "数据异常")
                        } else {
                            //var org= Gson().fromJson(model.Data,OrganizationJianModel::class.java)
                            scan.scan_result(model.Success!!,model.Data!!, "")
                        }
                    }

                    override fun onError(call: Call?, response: Response?, e: Exception?) {
                        scan.error_net()
                    }
                })
    }
    fun scan_login_xin(url: String, scan: IScan_result) {
        OkGo.get(app_url.scan_code_xin)
                .params("businessUrl", url)
                .execute(object : JsonCallback<LzyResponse<OrganizationJianModel>>() {
                    override fun onSuccess(model: LzyResponse<OrganizationJianModel>, call: Call, response: Response) {
                        if (model.Data == null) {
                            scan.scan_result(false, OrganizationJianModel(), "数据异常")
                        } else {
                           // v//ar org= Gson().fromJson(model.Data,OrganizationJianModel::class.java)
                            scan.scan_result(model.Success!!, model.Data!!, "")
                        }
                    }

                    override fun onError(call: Call?, response: Response?, e: Exception?) {
                        scan.error_net()
                    }
                })
    }
}