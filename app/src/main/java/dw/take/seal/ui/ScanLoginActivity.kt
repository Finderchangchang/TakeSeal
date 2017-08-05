package dw.take.seal.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Looper
import android.view.WindowManager
import com.google.gson.Gson
import com.nanchen.compresshelper.CompressHelper
import dw.take.seal.R
import dw.take.seal.callback.LzyResponse
import dw.take.seal.control.IScanLogin
import dw.take.seal.control.ScanLogin
import dw.take.seal.model.ImgModel
import dw.take.seal.model.OrganizationModel
import dw.take.seal.utils.ImgUtils
import dw.take.seal.utils.Utils
import dw.take.seal.view.CameraSurfaceView
import kotlinx.android.synthetic.main.activity_camera.*
import me.iwf.photopicker.PhotoPicker
import wai.gr.cla.base.BaseActivity
import java.io.*


/***
 *扫描营业执照登录
 */
class ScanLoginActivity : BaseActivity(), IScanLogin {
    override fun scan_result(model: LzyResponse<OrganizationModel>) {
        if (model.Success) {
            toast("扫描成功")
            var organization=model.Organizations!![0]//获得扫描的结果
            //扫描成功跳转到扫描结果确认页
            startActivity(Intent(this,ScanResultActivity::class.java).putExtra("model",organization))
        } else {
            toast(model.Message)
        }
    }


    override fun initViews() {
        /**
         * 设置全屏显示
         * */
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_camera)
        //点击拍照执行的操作。
        take_pic_btn.setOnClickListener {
            main_cv.takePicture { result ->
                System.out.println("path:::"+result)
                scan123123(result)
            }
        }
//        select_pic_btn.setOnClickListener {
//            PhotoPicker.builder()
//                    .setPhotoCount(1)
//                    .setPreviewEnabled(false)
//                    .start(this, PhotoPicker.REQUEST_CODE);
//        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == PhotoPicker.REQUEST_CODE) {
            if (data != null) {
                val photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS)
                scan123123(photos[0])
            }
        }
    }

    /**
     * 扫描图片并读取出里面的内容
     * @path 需要读取的图片路径
     * */
    fun scan123123(path: String) {
        val newFile = CompressHelper.getDefault(this).compressToFile(File(path))
        var bm = Utils.getimage(this, path)
        var bos = BufferedOutputStream(FileOutputStream(newFile))
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bos)
        bos.flush()
        bos.close()
        //iv.setImageBitmap(bm)

        ScanLogin().scan_login(Gson().toJson(ImgModel(ImgUtils().bitmapToBase64(bm))), this)

    }

    override fun initEvents() {

    }

}
