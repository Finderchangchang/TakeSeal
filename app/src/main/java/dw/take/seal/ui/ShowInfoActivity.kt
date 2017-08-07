package dw.take.seal.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import dw.take.seal.R
import dw.take.seal.utils.ImgUtils
import wai.gr.cla.base.BaseActivity
import wai.gr.cla.method.Utils
import java.util.*
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import com.google.gson.Gson
import dw.take.seal.control.SubmitListener
import dw.take.seal.control.SubmitView
import dw.take.seal.model.*


class ShowInfoActivity : BaseActivity(),SubmitView {
    override fun SubmitResult(success: Boolean, result: String) {
       // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun initEvents() {
       // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun initViews() {
        setContentView(R.layout.activity_show_info)
        var models: MutableList<OrganizationJianModel>? =findb!!.findAll(OrganizationJianModel::class.java)
        if(models!!.size>0){
            SubmitListener().getCertifyNumber(this,models[0].organizationRegionId)
        }

      //  loadData()

    }

    fun loadData(){
        var command:ApplySealCommand= ApplySealCommand()
        var group:GroupModel= GroupModel();

        group.SealApplyer="胡海珍"
        group.SealApplyerCertNumber="130682199206033463"
        group.SealCertifyNumber=""
        group.SealApplyerMobileNumber="15132210143"
        group.OrganizationRegionId="130903"
        group.OrganizationName="中信银行股份有限公司沧州分行"
        group.OrganizationUSCC="91130900083779987J"
        group.OrganizationLeader="冯永成"
        group.OrganizationLeaderCertNumber="130682199206033463"
        group.OrganizationLeaderMobileNumber="151225584587"
        group.OrganizationAddress="河北省沧州市运河区"
        group.OrganizationPostCode="061001"
        group.OrganizationTelephoneNumber="15933317777"
        group.SealRegister="耿建"
        group.SealContractShopId="130903000001"
        group.SealIdentificationRemark="测试数据"
        command.Group=group
        command.Seals=ArrayList<ApplySealData>()
        var apply:ApplySealData= ApplySealData()
        apply.SealContent="中信银行股份有限公司沧州分行"
        apply.SealType="01"
        apply.SealMaterial="04"
        apply.SealSpecificationId="13090002"
        command.Seals!!.add(apply)
        var apply1:ApplySealData= ApplySealData()
        apply1.SealContent="中信银行股份有限公司沧州分行1"
        apply1.SealType="02"
        apply1.SealMaterial="03"
        apply1.SealSpecificationId="13090002"
        command.Seals!!.add(apply1)

        var certificate:ApplySealCertificateData= ApplySealCertificateData()
        certificate.SealCertificateType="01"
        certificate.SealCertificateName="营业执照"
        val bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.yinyezhizhao)

        certificate.SealCertificateImageString= ImgUtils().bitmapToBase64(bitmap)
        command.Certificates=ArrayList<ApplySealCertificateData>()
        command.Certificates!!.add(certificate)
        SubmitListener().Submit(Gson().toJson(command),this)
    }
}
