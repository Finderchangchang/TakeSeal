package dw.take.seal.base

import android.app.Application
import android.content.Context
import com.lzy.okgo.OkGo
import com.lzy.okgo.cookie.store.MemoryCookieStore
import com.uuzuche.lib_zxing.activity.ZXingLibrary


/**
 * Created by Finder丶畅畅 on 2017/1/14 21:25
 * QQ群481606175
 */

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        OkGo.init(this)
        OkGo.getInstance().setCookieStore(MemoryCookieStore())
        ZXingLibrary.initDisplayOpinion(this);
    }

    companion object {
        var context: Context? = null
    }
}
