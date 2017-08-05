package dw.take.seal.control

import android.widget.Toast
import com.lzy.okgo.OkGo
import com.lzy.okgo.callback.StringCallback
import dw.take.seal.base.App
import dw.take.seal.model.app_url
import okhttp3.Call
import okhttp3.Response

/**
 * Created by Administrator on 2017/8/5.
 */

class SubmitListener {
    fun Submit(json: String) {
        OkGo.post(app_url.url_upload_img + "ApplySeal")
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
}

interface SubmitView {
    fun SubmitResult(success: Boolean, result: String)
}
