package dw.take.seal.ui

import android.content.Intent
import android.provider.MediaStore
import android.view.View
import android.view.WindowManager
import com.kaopiz.kprogresshud.KProgressHUD
import dw.take.seal.R
import dw.take.seal.model.OrganizationJianModel
import dw.take.seal.view.CameraQianSurfaceView
import dw.take.seal.view.CameraSurfaceView
import kotlinx.android.synthetic.main.activity_camera.*
import net.tsz.afinal.view.LoadingDialog
import wai.gr.cla.base.BaseActivity

/**正方形框图片
 *
 * Created by Administrator on 2017/8/3.
 */

class CameraPersonActivity : BaseActivity(), CameraSurfaceView.onScan {
    override fun get(url: String?) {
        //返回人员头像
        if (pdialog != null) {
            pdialog!!.dismiss()
        }
        if (url != null) {
            var myintent = intent
            myintent.putExtra("PATH", url)
            setResult(12, myintent)


        } else {
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
        setContentView(R.layout.activity_camera)
        textView2.text = "请将您的材料与四个角对齐"
        //select_pic_btn.visibility= View.INVISIBLE
        ///textView2.text="请拍摄您的正脸"
        //点击拍照执行的操作。
        fangtake_pic_btn.setOnClickListener {
            pdialog = KProgressHUD.create(this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("加载中")
                    .setCancellable(true)
                    .setAnimationSpeed(2)
                    .setDimAmount(0.5f)
            pdialog!!.show()
            main_cv.takePicture(this)
        }
    }

    override fun initEvents() {
    }
}
