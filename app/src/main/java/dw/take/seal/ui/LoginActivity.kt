package dw.take.seal.ui

import android.Manifest
import android.Manifest.permission.CAMERA
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.uuzuche.lib_zxing.activity.CaptureActivity
import com.uuzuche.lib_zxing.activity.CodeUtils
import dw.take.seal.R
import kotlinx.android.synthetic.main.activity_login.*
import wai.gr.cla.base.BaseActivity
import pub.devrel.easypermissions.EasyPermissions
import android.widget.Button
import com.kaopiz.kprogresshud.KProgressHUD
import dw.take.seal.control.*
import dw.take.seal.model.ApplySealCertificateData
import dw.take.seal.model.CardInfoModel
import dw.take.seal.model.OrganizationJianModel
import dw.take.seal.model.SealModel
import dw.take.seal.view.UpdateManager
import net.tsz.afinal.view.LoadingDialog
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.AppSettingsDialog
import wai.gr.cla.method.Utils

/***
 * 登录
 */
class LoginActivity : BaseActivity(), mLogin, IScan_result, EasyPermissions.PermissionCallbacks, UpdateView  {
    var apkpath: String? = null
    override fun UpdateVersionResult(success: Boolean, version: String, path: String) {
        //获取最新版本号
        if (true) {
            if (!version.equals(Utils.version)) {
                apkpath = path
                runOnUiThread(update)
            }
        }
    }
    var mUpdateManager: UpdateManager? = null
    internal var update: Runnable = Runnable {
        mUpdateManager = UpdateManager(this@LoginActivity, apkpath)
        mUpdateManager!!.checkUpdateInfo()
    }
    var pdialog: KProgressHUD? = null
    companion object {
        var loginmin: LoginActivity? = null
    }

    override fun scan_result(sucess: Boolean, model: OrganizationJianModel?, result: String) {
        if (pdialog != null) {
            pdialog!!.dismiss()
        }
        if (sucess) {
            //营业执照识别成功，跳页显示营业执照内容
            findb!!.deleteAll(OrganizationJianModel::class.java)
            findb!!.save(model)
            val intent = Intent(this@LoginActivity, OrganizationActivity::class.java)
            //val intent = Intent(this@LoginActivity, ShowZhiFuActivity::class.java)
            intent.putExtra("OrgModel", model)
            startActivity(intent)
        } else {
            toast(result)
        }
    }



    /**
     * 获得的权限
     * */
    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>?) {
//        val intent = Intent(this@LoginActivity, CaptureActivity::class.java)
//        startActivityForResult(intent, 1)
//        var btn: Button? = null
//        btn!!.setOnClickListener { view ->
//
//        }
    }

    override fun initViews() {
        loginmin = this
        setContentView(R.layout.activity_login)
        //获取最新版本号
        //UpdateListener().UpdateVersion(this)
    }
    /**
     * 请求CAMERA权限码
     */
    internal var REQUEST_CAMERA_PERM = 101

    override fun onPermissionsDenied(requestCode: Int, perms: List<kotlin.String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this, "当前App需要申请camera权限,需要打开设置页面么?")
                    .setTitle("权限申请")
                    .setPositiveButton("确认")
                    .setNegativeButton("取消", null /* click listener */)
                    .setRequestCode(REQUEST_CAMERA_PERM)
                    .build()
                    .show()
        }
    }
    override fun initEvents() {
        /***
         * 用户密码登录
         */
        login_btn.setOnClickListener {
            var count_et = count_et.text.toString().trim()
            var pwd_et = pwd_et.text.toString().trim()
            if (TextUtils.isEmpty(count_et)) {
                toast("登录账号不能为空")
            } else if (TextUtils.isEmpty(pwd_et)) {
                toast("密码不能为空")
            } else {
                login().user_login(count_et, pwd_et, this, this)
            }
        }
        /**
         * 拍摄营业执照登录
         * */
        scan_login_btn.setOnClickListener {
            startActivity(Intent(this, ScanLoginActivity::class.java))
        }
        //扫描营业执照
        scan_code_login_btn.setOnClickListener {
            findb!!.deleteAll(CardInfoModel::class.java)
            findb!!.deleteAll(SealModel::class.java)
            findb!!.deleteAll(OrganizationJianModel::class.java)
            findb!!.deleteAll(ApplySealCertificateData::class.java)

            cameraTask()
            //cameraTask()
            // check_camera_permission()
            //删除多余的信息

//            if (cameraTask()) {
//                val intent = Intent(this@LoginActivity, CaptureActivity::class.java)
//                startActivityForResult(intent, 1)
//            }else{
//                toast("无启用相机权限")
//            }
        }
        test_btn.setOnClickListener {
            check_camera_permission()
            findb!!.deleteAll(CardInfoModel::class.java)
            findb!!.deleteAll(SealModel::class.java)
            findb!!.deleteAll(OrganizationJianModel::class.java)
            findb!!.deleteAll(ApplySealCertificateData::class.java)
            ScanCodeLogin().scan_login_xin("http://www.hebscztxyxx.gov.cn/noticehb/query/queryEntInfoMain.do?uuid=XVd2MWuqNrcg8lrqJP.32zDEvtmAo694", this)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        /**
         * 处理二维码扫描结果
         */
        if (requestCode == 1) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                pdialog = KProgressHUD.create(this)
                        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                        .setLabel("识别中，请稍后")
                        .setCancellable(false)
                        .setAnimationSpeed(2)
                        .setDimAmount(0.5f)
                pdialog!!.show()
                var bundle: Bundle? = data.extras ?: return;
                //toast(bundle.toString())
                if (bundle!!.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    var result = bundle.getString(CodeUtils.RESULT_STRING);
                    ScanCodeLogin().scan_login_xin(result, this)
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    if (pdialog != null) {
                        pdialog!!.dismiss()
                    }
                    Toast.makeText(this@LoginActivity, "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //把申请权限的回调交由EasyPermissions处理
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    /**
     * 检测相机权限
     * */
    fun check_camera_permission(): Boolean {
        val perms = arrayOf<String>(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.MOUNT_FORMAT_FILESYSTEMS)
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
    @AfterPermissionGranted(101)
    fun cameraTask() {

        var array= arrayOf<String>(Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
        if (EasyPermissions.hasPermissions(this,*array)) {
            val intent = Intent(this@LoginActivity, CaptureActivity::class.java)
            startActivityForResult(intent, 1)
        } else {
            EasyPermissions.requestPermissions(this, "需要请求camera权限",
                    REQUEST_CAMERA_PERM, *array)
//            EasyPermissions.requestPermissions(this, "需要请求camera权限",
//                    1, Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.MOUNT_FORMAT_FILESYSTEMS)
        }
    }
    /**
     * 登录结果处理 true：登录成功
     * */
    override fun load(result: Int) {
        if (result == 1) {
            startActivity(Intent(this@LoginActivity, MySealActivity::class.java))
            finish()
    }
    }
}
