package dw.take.seal.control

import com.lzy.okgo.OkGo
import dw.take.seal.callback.JsonCallback
import dw.take.seal.callback.LzyResponse
import dw.take.seal.model.CardInfoModel
import dw.take.seal.model.app_url
import okhttp3.Call
import okhttp3.Response

/**
 * Created by Administrator on 2017/8/3.
 */
/**
 * 身份证照片识别图片内容 1.法人2.经办人
 * */
interface card_view {
    fun card_info_view(result: Boolean, info: CardInfoModel, mes: String);
}

class ZJSBListener {
    //身份证识别
    fun cardRecognition_img(img: String, main: card_view) {
        OkGo.post(app_url.url_scan_img + "CardRecognition")
                .params("Image", img)
                .execute(object : JsonCallback<LzyResponse<CardInfoModel>>() {
                    override fun onSuccess(s: LzyResponse<CardInfoModel>, call: Call, response: Response) {
                        if (s.Data != null) {
                            main.card_info_view(s.Success, s.Data!!, "证件识别成功")
                        } else if (s.Data == null) {
                            main.card_info_view(s.Success, CardInfoModel(), s.Message!!)
                        } else {
                            main.card_info_view(s.Success, s.Data!!, "")
                        }
                    }

                    override fun onError(call: Call?, response: Response?, e: Exception?) {
                        super.onError(call, response, e)
                        var model: CardInfoModel? = null
                        main.card_info_view(false, model!!, "网络错误")
                    }
                })
    }
}