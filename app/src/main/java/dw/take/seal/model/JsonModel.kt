package dw.take.seal.model

import dw.take.seal.model.SealModel
import java.io.Serializable

/**
 * Created by lenovo on 2017/7/17.
 */

class JsonModel:Serializable{
    var Group: OrganizationModel? = null
    var Seals: List<SealModel>? = null
}
