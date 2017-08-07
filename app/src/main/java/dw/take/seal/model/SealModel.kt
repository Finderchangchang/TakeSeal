package dw.take.seal.model

import java.io.Serializable

/**
 * Created by lenovo on 2017/7/9.
 */

class SealModel : Serializable {
    var id:Int?=null
    var SealContent: String = ""
    var SealType: String = ""
    var SealGGId: String = ""//印章规格ID
    var SealGGName: String = ""//印章规格
    var SealSpecificationId: String = ""
    var SealSpecificationName: String = ""
    var SealTypeName: String = ""
    var num: Int = 1;
    var isSelect: Boolean = false;
}
