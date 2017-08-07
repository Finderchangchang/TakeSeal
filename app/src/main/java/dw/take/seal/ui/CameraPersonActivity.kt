package dw.take.seal.ui

import android.content.Intent
import android.provider.MediaStore
import android.view.View
import android.view.WindowManager
import dw.take.seal.R
import dw.take.seal.model.OrganizationJianModel
import kotlinx.android.synthetic.main.activity_camera.*
import net.tsz.afinal.view.LoadingDialog
import wai.gr.cla.base.BaseActivity

/**正方形框图片
 *
 * Created by Administrator on 2017/8/3.
 */

class CameraPersonActivity : BaseActivity() {
    var orgModel:OrganizationJianModel?=null
    var pdialog: LoadingDialog?=null
    override fun initViews() {
        /**
         * 设置全屏显示
         * */
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_camera)

        //select_pic_btn.visibility= View.INVISIBLE
        ///textView2.text="请拍摄您的正脸"
        //点击拍照执行的操作。
        take_pic_btn.setOnClickListener {
            pdialog= LoadingDialog(this)
            pdialog!!.show()
            main_cv.takePicture { result ->
                System.out.println("path:::"+result)

                var myintent=intent
                myintent.putExtra("PATH",result)
                setResult(12,myintent)
                //返回人员头像
                if(pdialog!=null){
                    pdialog!!.dismiss()
                }
                finish()
            }
        }
    }

    override fun initEvents() {
    }
}
