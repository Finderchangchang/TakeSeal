package dw.take.seal.model

import java.io.Serializable
import java.util.*

/**
 * 提交数据模型
 * Created by Administrator on 2017/8/7.
 */

class ApplySealCommand : Serializable {
    var Group: GroupModel? = null
    var Seals: ArrayList<ApplySealData>? = null
    var Certificates: ArrayList<ApplySealCertificateData>? = null
}

