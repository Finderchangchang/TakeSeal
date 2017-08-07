package dw.take.seal.control

import com.lzy.okgo.OkGo
import dw.take.seal.callback.JsonCallback
import dw.take.seal.callback.LzyResponse
import dw.take.seal.model.FaceCompare
import dw.take.seal.model.app_url
import okhttp3.Call
import okhttp3.Response

/**
 * Created by Administrator on 2017/8/3.
 */
interface FaceView {
    fun face_result(result: Boolean, mes: String)
}

class FaceListener {
    /**
     * 身份证照片识别+本人照片比较相似度图片内容
     * */
    fun card_fackRecognition_img(img: String, faceimg: String, main: FaceView) {
        OkGo.post(app_url.url_scan_img + "FaceRecognition")
                .params("imageA", faceimg)//返回的头像
                .params("imageB", img)//本人照片
                .execute(object : JsonCallback<LzyResponse<FaceCompare>>() {
                    override fun onSuccess(s: LzyResponse<FaceCompare>, call: Call, response: Response) {
                        if (s.Success) {
                            if (s.Data!!.isSamePerson) {
                                main.face_result(true, "证照比对成功，比对分值为"+s.Data!!.faceScore)
                            } else {
                                main.face_result(false, "证照人像比对失败，相似度太低"+s.Data!!.faceScore)
                            }
                        } else {
                            if (s.Message != null) {
                                main.face_result(false, "证照比对失败：" + s.Message)
                            } else {
                                main.face_result(false, "证照比对失败：数据错误")
                            }
                        }
                    }

                    override fun onError(call: Call?, response: Response?, e: Exception?) {
                        super.onError(call, response, e)
                        main.face_result(false, "证照比对失败：" + e!!.message)
                    }
                })
    }
}