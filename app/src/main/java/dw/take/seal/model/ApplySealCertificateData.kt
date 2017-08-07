package dw.take.seal.model

import java.io.Serializable

/**
 * 材料模型
 * Created by Administrator on 2017/8/5.
 */
class ApplySealCertificateData: Serializable {
    //材料类型，根据材料类型字典，网上申请需根据情况提交01，02，03，04（如法人本人办理提供01，02，否则4项全提供）。-->
    var SealCertificateType:String?=null
    //
    var SealCertificatePageNumber:String?=null
    //
    var SealCertificateName:String?=null
    //图片类型
    var SealCertificateSuffix:String="jpg"
    //图片Base64
    var SealCertificateImageString:String?=null

}