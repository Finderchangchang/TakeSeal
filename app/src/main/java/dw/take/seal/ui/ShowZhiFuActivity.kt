package dw.take.seal.ui

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import dw.take.seal.R
import dw.take.seal.base.App
import dw.take.seal.control.ZhifuView
import dw.take.seal.control.ZhifuViewListener
import dw.take.seal.method.GlideCircleTransform
import dw.take.seal.utils.Utils
import kotlinx.android.synthetic.main.activity_zhifu_show.*
import wai.gr.cla.base.BaseActivity
import wai.gr.cla.model.key
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import android.os.Environment
import android.provider.MediaStore
import android.widget.ImageView
import pub.devrel.easypermissions.EasyPermissions


//显示支付图片和价格
class ShowZhiFuActivity : BaseActivity(), ZhifuView {
    override fun price_result(result: Boolean, mes: String?, price: Double?) {
        //总价格
        if (result) {
            zhifu_price.setText("印章总价格为：" + price + "元")
        } else {
            zhifu_price.setText(mes)
        }
    }

    override fun weixin_result(result: Boolean, mes: String, bitmap: Bitmap?) {
        //微信支付图片
        if (bitmap != null) {
            img_weixin.setImageBitmap(bitmap)
        }
    }

    override fun zhifu_result(result: Boolean, mes: String, bitmap: Bitmap?) {
        //支付宝支付图片
        if (bitmap != null) {
            img_zhifubao.setImageBitmap(bitmap)
        }
    }


    override fun initEvents() {
        zhifu_complete.setOnClickListener {
            val intent = Intent()
            intent.action = "action.exit"
            App.context!!.sendBroadcast(intent)
        }
        img_zhifubao.setOnLongClickListener {
            saveBitmap(img_zhifubao,Environment.getExternalStorageDirectory().toString())
            true
        }
        img_weixin.setOnLongClickListener {
            saveBitmap(img_weixin,Environment.getExternalStorageDirectory().toString())
            true
        }
    }

    override fun initViews() {
        setContentView(R.layout.activity_zhifu_show)
        var shopid = dw.take.seal.utils.Utils(this).ReadString(key.KEY_SHOP_ID)
        ZhifuViewListener().GetImageWeChatQRCode(shopid, this)
        ZhifuViewListener().GetImageAlipayQRCode(shopid, this)
        var sealgroupid = Utils(this).ReadString("SealGroupId")
        ZhifuViewListener().GetSumarySealPrice(sealgroupid, this)
        var shopname=Utils(this).ReadString(key.KEY_SHOP_Name)
        var phone= dw.take.seal.utils.Utils(this).ReadString(key.KEY_SHOP_PHONE)
        var address= dw.take.seal.utils.Utils(this).ReadString(key.KEY_SHOP_ADDRESS)
        zhifu_shop_phone.setText("联系电话："+phone)
        zhifu_shop_address.setText("联系地址："+address)
        zhifu_kezhishe.setText("收款单位："+shopname)
    }
    /**
     * 检测相机权限
     * */
    fun check_camera_permission(): Boolean {
        val perms = arrayOf<String>(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.MOUNT_FORMAT_FILESYSTEMS)
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
    fun saveBitmap(view: ImageView, filePath: String) {
        check_camera_permission()
        val drawable = view.getDrawable() ?: return
        var outStream: FileOutputStream? = null
        val file = File(filePath)

        if (!file.exists()) {
            file.mkdirs();
        }
        var imagename=System.currentTimeMillis().toString() + ".jpg"
        val newfile=File(filePath,imagename)
        try {
            outStream = FileOutputStream(newfile)
            val bitmap = (drawable as BitmapDrawable).bitmap
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream)
            outStream.flush()
            outStream.close()
            //把文件插入到系统图库
            MediaStore.Images.Media.insertImage(getContentResolver(), filePath+"/"+imagename, "title", "description")
            //保存图片后发送广播通知更新数据库
            sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(File(filePath+"/"+imagename))))
            toast("保存成功，保存路径为："+filePath)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
