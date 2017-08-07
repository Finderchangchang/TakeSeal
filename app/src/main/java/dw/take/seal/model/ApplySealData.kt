package dw.take.seal.model

/**
 * 印章内容
 * Created by Administrator on 2017/8/7.
 */
class ApplySealData {
    //印章内容，根据指定规则构建
    var SealContent: String? = null
    //印章类型，根据指定规则构建
    var SealType: String? = null
    //章体材料，根据指定规则构建
    var SealMaterial: String? = null
    //印章规格，根据GetSpecificationCodes接口调取
    var SealSpecificationId: String? = null
    //始终为1
    var SealUsageMode: String = "1"
    //始终为0
    var SealMediumCount: String = "0"
    //始终为0。-->
    var SealUsageYear: String = "0"
    //备注
    var SealRemark:String?=null
}