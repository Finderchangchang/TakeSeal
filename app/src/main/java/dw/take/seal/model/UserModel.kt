package dw.take.seal.model

import dw.take.Seal.model.Seal

/**
 * 当前登录账户信息
 * Created by lenovo on 2017/7/4.
 */

class UserModel {

    /**
     * Group : {"OrganizationId":"","OrganizationName":"广州融融居餐饮有限公司","OrganizationNationName":"","OrganizationForeignName":"","OrganizationType":"02","OrganizationTypeName":"企业单位","OrganizationRegionId":"440106","OrganizationRegionName":"天河区公安局","OrganizationUSCC":"91440101321103449X","OrganizationLeader":"何蓉","OrganizationLeaderCertType":"1","OrganizationLeaderCertTypeName":"居民身份证","OrganizationLeaderCertNumber":"132902197311095612","OrganizationLeaderMobileNumber":"","OrganizationAddress":"广州市越秀区永福路9号自编217室","OrganizationOpenState":"1","OrganizationOpenStateName":"正常","OrganizationPostCode":"100020","OrganizationTelephoneNumber":"18231857375","OrganizationApproveDepartmentId":"","OrganizationCreateTime":"2017-06-30 00:00","OrganizationApproveDepartmentName":"","OrganizationShareType":"工商数据共享（统一社会信用代码）","$id":"corp","SealContractShopId":"440106000001","SealApplyer":"柳伟杰","SealApplyerCertNumber":"131182199302281014","SealApplyerMobileNumber":"17093215800","VerifyCode":"123","IsVerify":true}
     * Seals : [{"SealContent":"广州融融居餐饮有限公司 ","SealType":"01","SealSpecificationId":"44010001","SealTypeName":"单位专用章"},{"SealContent":"广州融融居餐饮有限公司 财务专用章","SealType":"02","SealSpecificationId":"44010010","SealTypeName":"财务专用章"},{"SealContent":"广州融融居餐饮有限公司 发票专用章","SealType":"03","SealSpecificationId":"44010011","SealTypeName":"发票专用章"},{"SealContent":"广州融融居餐饮有限公司 合同专用章","SealType":"04","SealSpecificationId":"44010004","SealTypeName":"合同专用章"}]
     */

    var group: OrganizationModel? = null
    var seals: List<Seal>? = null

}
