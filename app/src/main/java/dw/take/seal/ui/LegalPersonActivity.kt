package dw.take.seal.ui

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView

import dw.take.seal.R
import dw.take.seal.control.ZJSBListener
import dw.take.seal.control.card_view
import dw.take.seal.model.CardInfoModel
import dw.take.seal.utils.ImgUtils
import dw.take.seal.view.CameraSurfaceView
import kotlinx.android.synthetic.main.ac_legal_person.*
import wai.gr.cla.base.BaseActivity
import wai.gr.cla.method.Utils
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.hardware.Camera
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import dw.take.seal.model.OrganizationJianModel
import net.tsz.afinal.view.LoadingDialog
import java.io.File


/**
 * 法人证件照片上传
 * Created by Administrator on 2017/8/2.
 */

class LegalPersonActivity : BaseActivity(), card_view {
    //图片保存路径
    private var path = StringBuffer()
    var orgModel:OrganizationJianModel?=null
    var isfaren:Int=0  //0法人1经办人2
    val CAMERA_REQUEST = 8888
    var cardInfo:CardInfoModel?=null
    var pdialog: ProgressDialog?=null
    override fun card_info_view(result: Boolean, info: CardInfoModel, type: Boolean, mes: String) {
        //证件识别结果
        //toast(mes);
        if(pdialog!=null){
            pdialog!!.dismiss()
        }
        if (result) {
            if (info != null) {
                cardInfo=info
                lp_tv_name.visibility = View.VISIBLE
                lp_tv_cardid.visibility = View.VISIBLE
                lp_tv_name.text = "姓名：" + info.personName
                lp_tv_cardid.text = "身份证号码：" + info.identyNumber
            } else {
                lp_tv_name.visibility = View.VISIBLE
                lp_tv_name.text = mes;
                lp_tv_cardid.visibility = View.GONE
            }
        } else {
            lp_tv_name.visibility = View.VISIBLE
            lp_tv_name.text = mes;
            lp_tv_cardid.visibility = View.GONE
        }
    }



    override fun initViews() {
        setContentView(R.layout.ac_legal_person)
        orgModel= intent.getSerializableExtra("OrgModel") as OrganizationJianModel?
        isfaren=intent.getIntExtra("isFaRen",0)
    }

    override fun initEvents() {
        btn_upload_faren!!.setOnClickListener {
            //拍照
//            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//            intent.putExtra("android.intent.extras.CAMERA_FACING", 1)
//            // 调用前置摄像头
//            startActivityForResult(intent, CAMERA_REQUEST)
          //  startCamera(11)
            startActivityForResult(Intent(this, CameraPersonActivity::class.java),12)
        }
        lp_next_btn.setOnClickListener {
            if(isfaren==0) {
                //提示是否为本人
                AlertDialog.Builder(this@LegalPersonActivity).setTitle("提示")//设置对话框标题

                        .setMessage("是否为法人本人？")//设置显示的内容

                        .setPositiveButton("确定", DialogInterface.OnClickListener { dialog, which ->
                            //添加确定按钮
                            //跳转到自拍界面
                            startActivity(Intent(this, FaceActivity::class.java).putExtra("isFaRen", true).putExtra("OrgModel", orgModel).putExtra("CardInfo",cardInfo))
                            //确定按钮的响应事件
                            finish()
                        }).setNegativeButton("返回", DialogInterface.OnClickListener { dialog, which ->
                    //添加返回按钮
                    //响应事件
                    startActivity(Intent(this, LegalPersonActivity::class.java).putExtra("isFaRen", 1).putExtra("OrgModel", orgModel))
                    Log.i("alertdialog", " 请保存数据！")
                }).show()//在按键响应事件中显示此对话框
            }else{
                startActivity(Intent(this, FaceActivity::class.java).putExtra("isFaRen", false).putExtra("OrgModel", orgModel).putExtra("CardInfo",cardInfo))
            }
        }
        lp_close_btn.setOnClickListener { finish() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 11 && resultCode == Activity.RESULT_OK) {
            val photo =Utils.getimage(200, path.toString())
            //val zhengbm = Utils.centerSquareScaleBitmap(photo, 100)
            pdialog= LoadingDialog(this)
            pdialog!!.show()
            lp_iv_farenz!!.setImageBitmap(photo)
//            val bm = Utils.compressImagexin(photo, 200)
            ZJSBListener().cardRecognition_img(ImgUtils().bitmapToBase64(photo!!), this, isfaren==0)
        }else if(requestCode == 12 && resultCode == 12){
            var mypath=data!!.getStringExtra("PATH")
            val photo =Utils.getimage(200, mypath.toString())
            //val zhengbm = Utils.centerSquareScaleBitmap(photo, 100)
            pdialog= LoadingDialog(this)
            pdialog!!.show()
            lp_iv_farenz!!.setImageBitmap(photo)
//            val bm = Utils.compressImagexin(photo, 200)
            ZJSBListener().cardRecognition_img(ImgUtils().bitmapToBase64(photo!!), this, isfaren==0)
        }
    }

    //开启拍照
    fun startCamera(type: Int) {
        // 利用系统自带的相机应用:拍照
        val pm = packageManager
        val hasACamera = pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)
                || pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)
                || Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD
                || Camera.getNumberOfCameras() > 0
        if (hasACamera) {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            path = StringBuffer()
            path.append(this@LegalPersonActivity.getExternalFilesDir(null)).append("/header.jpg")
            val file = File(path.toString())
            val uri = Uri.fromFile(file)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
            startActivityForResult(intent, type)
        } else {
            Toast.makeText(this@LegalPersonActivity, "请检查相机功能是否正常!", Toast.LENGTH_SHORT).show()
        }
    }
}
