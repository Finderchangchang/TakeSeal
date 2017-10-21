package dw.take.seal.model

/**
 * Created by lenovo on 2017/7/4.
 */
object app_url{
    val normal_url="http://192.168.1.115:6006"
    val base_url1="http://118.190.47.63:1399"
    val base_urlceshi="http://www.hbgdfw.com"
    val base_urlqinghai="https://seal.hbgdfw.com:6006"//正式
    var base_url="https://seal.hbgdfw.com:11001"//青海

    var url_organizations=base_url+"/Service/Organization.aspx?Action="//获得当前登录账号信息
    var url_user=base_url+"/Service/User.aspx?Action="//获得当前登录账号信息
    var url_get_code=base_url+"/Service/Code.aspx?Action="//获得当前shop列表
    var url_get_shop=base_url+"/Service/Shop.aspx?Action="//获得当前shop列表
    var url_get_code_by_tel=base_url+"/Service/Message.aspx?Action="//获得验证码
    var url_scan_img=base_url+"/Service/Recognition.aspx?Action="
    var url_upload_img= base_url+"/Service/SealProcess.aspx?Action="//提交图片接口
    //扫码识别营业执照
    var scan_code= base_url+"/Service/Recognition.aspx?Action=BusinessAnalysis"
    var scan_code_xin=base_url+"/Service/User.aspx?Action=BusinessAnalysisLogin"
    var url_get_certifynumber=base_url+"/Service/Common.aspx?Action="
    var update= base_url+"/Service/Manage.aspx?Action=GetAndroidVersion"
}