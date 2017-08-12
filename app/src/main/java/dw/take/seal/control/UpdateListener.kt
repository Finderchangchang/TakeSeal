package dw.take.seal.control

import com.google.gson.Gson
import com.lzy.okgo.OkGo
import com.lzy.okgo.callback.StringCallback
import dw.take.seal.callback.LzyResponse
import dw.take.seal.model.app_url
import okhttp3.Call
import okhttp3.Response

/**
 * 软件更新
 * Created by Administrator on 2017/8/12.
 */

class UpdateListener {
    //获取版本号信息
    fun UpdateVersion(main: UpdateView) {
        OkGo.get(app_url.update + "&Type=Seal")
                .execute(object : StringCallback() {
                    override fun onSuccess(s: String, call: Call, response: Response) {
                        //var result=s
                        //result=s.replace("[{","[").replace("]}","]")
                        var res = Gson().fromJson(s, LzyResponse::class.java)
                        if (res.Success) {
                            main.UpdateVersionResult(true, res.Version!!, res.Path!!)
                        } else {
                            main.UpdateVersionResult(false, "0", "")
                        }
                    }

                    override fun onError(call: Call?, response: Response?, e: Exception?) {
                        super.onError(call, response, e)
                    }
                })
    }
}

interface UpdateView {
    fun UpdateVersionResult(success: Boolean, version: String, path: String)
}
