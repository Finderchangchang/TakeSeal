package dw.take.seal.ui

import android.content.Intent
import android.view.WindowManager
import android.widget.Button
import com.kaopiz.kprogresshud.KProgressHUD
import dw.take.seal.R
import dw.take.seal.view.CameraQianSurfaceView
import kotlinx.android.synthetic.main.activity_camera.*
import kotlinx.android.synthetic.main.activity_qian_camera.*
import net.tsz.afinal.view.LoadingDialog
import wai.gr.cla.base.BaseActivity

/**
 * 自拍照
 * Created by Administrator on 2017/8/4.
 */

class FaceCameraActivity : BaseActivity(), CameraQianSurfaceView.onScan {
    override fun get(url: String?) {
        //返回人员头像
        if (url != null) {
            System.out.println("path:::" + url)
            //返回人员头像
            var myintent = Intent(this, FaceActivity::class.java)
            myintent.putExtra("PATH", url)
            setResult(12, myintent)
            if (pdialog != null) {
                pdialog!!.dismiss()
            }
        } else {
            if (pdialog != null) {
                pdialog!!.dismiss()
            }
            toast("图片保存失败")
        }
        finish()
    }

    var pdialog: KProgressHUD? = null
    override fun initViews() {
        /**
         * 设置全屏显示
         * */
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_qian_camera)
        qian_tv.text = "请拍摄您的正脸"
        //点击拍照执行的操作。
        qian_take_pic_btn.setOnClickListener {
            pdialog = KProgressHUD.create(this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("加载中")
                    .setCancellable(false)
                    .setAnimationSpeed(2)
                    .setDimAmount(0.5f)
            pdialog!!.show()
            qian_main_cv.takePicture(this)
//            qian_main_cv.takePicture {
//                System.out.println("path:::" + result)
//                //返回人员头像
//                var myintent = Intent(this, FaceActivity::class.java)
//                myintent.putExtra("PATH", result)
//                setResult(12, myintent)
//                if (pdialog != null) {
//                    pdialog!!.dismiss()
//                }
//                finish()
//            }
        }
    }

    override fun initEvents() {

    }
}
