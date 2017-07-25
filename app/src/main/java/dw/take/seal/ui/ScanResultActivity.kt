package dw.take.seal.ui

import dw.take.seal.R
import dw.take.seal.model.OrganizationModel
import wai.gr.cla.base.BaseActivity

/***
 * 扫描图片结果纠正页面
 */
class ScanResultActivity : BaseActivity() {
    var model:OrganizationModel?=null//扫描传递过来的结果
    override fun initViews() {
        setContentView(R.layout.activity_scan_result)
    }

    override fun initEvents() {

    }
}
