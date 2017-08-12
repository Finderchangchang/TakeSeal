package dw.take.seal.model

import java.io.Serializable

/**
 * Created by Administrator on 2017/8/2.
 */

class GroupModel : Serializable {
    //<!--默认为1。-->
    var SealApplyType: String = "1"
    //上传前调用GetCertifyNumber接口获取
    var SealCertifyNumber: String? = null
    //默认3
    var SealApplyMethod: String = "3"
    //默认1
    var SealUsageMode: String = "1"
    //经办人姓名
    var SealApplyer: String? = null
    //经办人证件类型（默认1身份证）。-->
    var SealApplyerCertType: String = "1"
    //经办人证件号码
    var SealApplyerCertNumber: String? = null
    //经办人手机号码（填写短信验证码手机号）。-->
    var SealApplyerMobileNumber: String? = null
    //<!--根据BusinessAnalysisLogin返回填写，可为空。-->
    var OrganizationId: String? = null
    //营业执照截取
    var OrganizationRegionId: String? = null
    //根据BusinessAnalysisLogin返回填写;中信银行股份有限公司沧州分行
    var OrganizationName: String? = null
    //始终默认02
    var OrganizationType: String = "02"
    //根据BusinessAnalysisLogin返回填写
    var OrganizationUSCC: String? = null
    //根据BusinessAnalysisLogin返回填写;冯永成
    var OrganizationLeader: String? = null
    //法人证件类型（默认1身份证）。-->
    var OrganizationLeaderCertType: String = "1"
    //法人身份证。-->
    var OrganizationLeaderCertNumber: String? = null
    //法人移动电话（填写短信验证码手机号）。-->
    var OrganizationLeaderMobileNumber: String? = null
    //根据BusinessAnalysisLogin返回填写
    var OrganizationAddress: String? = null

    //同OrganizationRegionId
    var OrganizationPostCode: String? = null
    //同经办人手机号码
    var OrganizationTelephoneNumber: String? = null
    //始终为false
    var OrganizationNameChanged: Boolean = false
    //始终为false
    var OrganizationLeaderChanged: Boolean = false
    //同经办人姓名
    var SealRegister: String? = null
    //选择的印章刻制单位GetShopCodes接口调取
    var SealContractShopId: String? = null
    //始终填1
    var SealIdentificationType: String = "2"
    //内容根据人像识别结果构建
    var SealIdentificationRemark: String? = null

    //使用为false。
    var SealPayment: Boolean = false
    //始终为0
    var SealPaymentMethod: String = "0"
    //始终为false。
    var SealDeliverDepartmentEnable: Boolean = false
    //始终为0。
    var SealMediumCount: Int = 0
    var SealRegisteDepartmentId:String?=null
}
