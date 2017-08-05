package dw.take.seal.ui

import android.content.Intent
import android.view.WindowManager
import dw.take.seal.R
import kotlinx.android.synthetic.main.activity_camera.*
import kotlinx.android.synthetic.main.activity_qian_camera.*
import net.tsz.afinal.view.LoadingDialog
import wai.gr.cla.base.BaseActivity

/**
 * 自拍照
 * Created by Administrator on 2017/8/4.
 */

class FaceCameraActivity : BaseActivity() {
    var pdialog: LoadingDialog?=null
    override fun initViews() {
        /**
         * 设置全屏显示
         * */
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_qian_camera)

        //select_pic_btn.visibility= View.INVISIBLE
        qian_tv.text="请拍摄您的正脸"
        //点击拍照执行的操作。
        qian_take_pic_btn.setOnClickListener {
            pdialog= LoadingDialog(this)
            pdialog!!.show()
            qian_main_cv.takePicture { result ->
                System.out.println("path:::"+result)
                //返回人员头像
                if(pdialog!=null){
                    pdialog!!.dismiss()
                }
                var myintent= Intent(this,FaceActivity::class.java)
                myintent.putExtra("PATH",result)
                setResult(12,myintent)
                finish()
            }
        }
    }

    override fun initEvents() {

    }
}
