package dw.take.seal.ui

import android.Manifest
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.transition.Visibility
import android.view.View
import android.widget.Toast
import com.uuzuche.lib_zxing.activity.CaptureActivity
import com.uuzuche.lib_zxing.activity.CodeUtils
import dw.take.seal.R
import dw.take.seal.R.style.left_leb_style
import dw.take.seal.control.IScan_result
import dw.take.seal.control.ScanCodeLogin
import dw.take.seal.model.OrganizationJianModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_organization_view.*
import pub.devrel.easypermissions.EasyPermissions
import wai.gr.cla.base.BaseActivity

@Suppress("UNREACHABLE_CODE")
//第二步，扫描营业执照信息
class OrganizationActivity : BaseActivity(), IScan_result {
    override fun scan_result(istrue: Boolean, model: OrganizationJianModel, result: String) {

        if (istrue) {
            //营业执照识别成功，跳页显示营业执照内容
            code_id_tv.text = model.organizationUSCC
            name_tv.text = model.organizationName
            qy_tv.text = model.organizationEstablishDate
            fr_tv.text = model.organizationLeader
            org_ll_content.visibility = View.VISIBLE
            address_tv.text = model.organizationAddress
        } else {
            toast(result)
        }
    }

    override fun initEvents() {

        btn_org_saomiao.setOnClickListener {
            if (check_camera_permission()) {
                val intent = Intent(this@OrganizationActivity, CaptureActivity::class.java)
                startActivityForResult(intent, 1)
            }
        }
        org_next_btn.setOnClickListener {
            if (yes_fr.isChecked || no_jbr.isChecked) {
                startActivity(Intent(this, StepTwoActivity::class.java))
            } else {
                Snackbar.make(org_next_btn, "请选择办理人员！", Toast.LENGTH_SHORT).show()
            }
        }
        org_close_btn.setOnClickListener { finish() }
    }

    /**
     * 检测相机权限
     * */
    fun check_camera_permission(): Boolean {
        val perms = arrayOf<String>(Manifest.permission.CAMERA)
        if (EasyPermissions.hasPermissions(this, *perms)) {//检查是否获取该权限
            return true
        } else {
            //第二个参数是被拒绝后再次申请该权限的解释
            //第三个参数是请求码
            //第四个参数是要申请的权限
            EasyPermissions.requestPermissions(this, "必要的权限", 0, *perms)
        }
        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        /**
         * 处理二维码扫描结果
         */
        if (requestCode == 1) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                var bundle: Bundle? = data.extras ?: return;
                //toast(bundle.toString())
                if (bundle!!.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    var result = bundle.getString(CodeUtils.RESULT_STRING);
                    ScanCodeLogin().scan_login(result, this)
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(this@OrganizationActivity, "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }

    }

    override fun initViews() {
        setContentView(R.layout.activity_organization_view)
    }


}
