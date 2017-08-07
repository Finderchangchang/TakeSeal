package dw.take.seal.model

import java.io.Serializable

/**
 * 字典
 * Created by Administrator on 2017/8/4.
 */

class CodeModel : Serializable {
    var Key: String=""
    var Value: String=""
    var Parameter: String=""
    var is_check: Boolean = false
}
