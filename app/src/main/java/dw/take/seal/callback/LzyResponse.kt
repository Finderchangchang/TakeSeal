package dw.take.seal.callback

import dw.take.seal.model.OrganizationJianModel
import dw.take.seal.model.OrganizationModel
import dw.take.seal.model.ShopModel
import java.io.Serializable

/**
 * ================================================
 * 作    者：jeasonlzy（廖子尧）Github地址：https://github.com/jeasonlzy
 * 版    本：1.0
 * 创建日期：16/9/28
 * 描    述：
 * 修订历史：
 * ================================================
 */
class LzyResponse<T> : Serializable {
    var total: Int = 0
    var code: Int = 0
    var msg:String=""
    var Message: String = ""
    var Organizations: List<OrganizationModel>? = null
    var Shops: List<ShopModel>? = null
    var RandomNumber: String=""
    var Success: Boolean = false
    var time: T?=null
    var remindCount: String=""
    var Data:T?=null
    var Codes:List<T>?=null
}