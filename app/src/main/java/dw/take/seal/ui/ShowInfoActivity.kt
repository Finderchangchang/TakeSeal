package dw.take.seal.ui

import android.app.ProgressDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import dw.take.seal.R
import dw.take.seal.utils.ImgUtils
import wai.gr.cla.base.BaseActivity
import wai.gr.cla.method.Utils
import java.util.*
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.support.design.widget.Snackbar
import android.util.Base64
import android.widget.Toast
import com.google.gson.Gson
import dw.take.seal.control.SubmitListener
import dw.take.seal.control.SubmitView
import dw.take.seal.method.CommonAdapter
import dw.take.seal.method.CommonViewHolder
import dw.take.seal.model.*
import kotlinx.android.synthetic.main.activity_show_info.*
import kotlinx.android.synthetic.main.activity_step_three.*
import net.tsz.afinal.view.LoadingDialog
import wai.gr.cla.model.key

//信息确认
class ShowInfoActivity : BaseActivity(), SubmitView {
    var regionid: String = ""
    var adapter: CommonAdapter<SealModel>? = null
    var list: MutableList<SealModel>? = null
    var isFa: Boolean = true
    var pdialog: ProgressDialog? = null
    override fun getCertifyNumberResult(success: Boolean, result: String) {
        if (pdialog != null) {
            pdialog!!.dismiss()
        }
        if (success) {
            regionid = result
            loadData()
            //是否成功界面
        } else {
            toast("提交出错：" + result)
        }

    }

    override fun SubmitResult(success: Boolean, result: String) {
        if (success) {
            val intent = Intent(this@ShowInfoActivity, ShowInfoActivity::class.java)
            startActivity(intent)
        } else {
            toast("提交出错：" + result)
        }
    }

    override fun initEvents() {
        adapter = object : CommonAdapter<SealModel>(this@ShowInfoActivity, list, R.layout.item_seal_info) {
            override fun convert(holder: CommonViewHolder?, t: SealModel?, position: Int) {
                holder!!.setText(R.id.item_seal_info_type, t!!.SealTypeName)
                holder!!.setText(R.id.item_seal_info_cz, t!!.SealGGName)
                holder!!.setText(R.id.item_seal_info_num, t!!.num.toString())
                holder!!.setText(R.id.item_seal_info_gg, t!!.SealSpecificationName)
            }
        }
        showinfo_next_btn.setOnClickListener {
            pdialog = LoadingDialog(this);
            pdialog!!.show();
            var models: MutableList<OrganizationJianModel>? = findb!!.findAll(OrganizationJianModel::class.java)
            if (models!!.size > 0) {
                SubmitListener().getCertifyNumber(this, models[0].organizationRegionId)
            }
        }
        showinfo_lv.adapter = adapter
    }

    override fun initViews() {
        setContentView(R.layout.activity_show_info)
        list = findb!!.findAll(SealModel::class.java)

        var orgs: MutableList<OrganizationJianModel>? = findb!!.findAll(OrganizationJianModel::class.java)
        var where = "true"
        isFa = dw.take.seal.utils.Utils(this).ReadString(key.KEY_TAKESEAL_ISFAREN).equals("1")
        if (isFa) {
            where = "true"
            showinfo_tv_faren.text = "法人信息"
            showinfo_tv_title.text = "第十步"
        } else {
            where = "false"
            showinfo_tv_faren.text = "经办人信息"
            showinfo_tv_title.text = "第十二步"
        }
        //加载法人信息
        var cards: MutableList<CardInfoModel>? = findb!!.findAllByWhere(CardInfoModel::class.java, "isfaren=" + where + "")
        if (cards!!.size > 0) {
            showinfo_tv_jingname.text = cards[0].personName
            show_info_zj.text = cards[0].identyNumber
            showinfo_tv_jmobile.text = dw.take.seal.utils.Utils(this).ReadString(key.KEY_MOBILE_NUMBER)
        }
        if (orgs!!.size > 0) {
            showinfo_tv_uc.text = orgs[0].organizationUSCC
            showinfo_tv_cname.text = orgs[0].organizationName
            showinfo_tv_daibiao.text = orgs[0].organizationLeader
        } else {
            toast("加载数据失败")
            finish()
        }

    }

    fun loadData() {
        var command: ApplySealCommand = ApplySealCommand()
        var group: GroupModel = GroupModel();

        group.SealApplyer = "胡海珍"
        group.SealApplyerCertNumber = "130682199206033463"
        group.SealCertifyNumber = regionid
        group.SealApplyerMobileNumber = "15132210143"
        group.OrganizationRegionId = "130903"
        group.OrganizationName = "中信银行股份有限公司沧州分行"
        group.OrganizationUSCC = "91130900083779987J"
        group.OrganizationLeader = "冯永成"
        group.OrganizationLeaderCertNumber = "130682199206033463"
        group.OrganizationLeaderMobileNumber = "151225584587"
        group.OrganizationAddress = "河北省沧州市运河区"
        group.OrganizationPostCode = "061001"
        group.OrganizationTelephoneNumber = "15933317777"
        group.SealRegister = "耿建"
        group.SealContractShopId = "130903000001"


        group.SealRegisteDepartmentId = "123456789874"


        group.SealIdentificationRemark = "测试数据"
        command.Group = group
        command.Seals = ArrayList<ApplySealData>()
        var apply: ApplySealData = ApplySealData()
        apply.SealContent = "中信银行股份有限公司沧州分行"
        apply.SealType = "01"
        apply.SealMaterial = "04"
        apply.SealSpecificationId = "13090002"
        command.Seals!!.add(apply)
        var apply1: ApplySealData = ApplySealData()
        apply1.SealContent = "中信银行股份有限公司沧州分行1"
        apply1.SealType = "02"
        apply1.SealMaterial = "03"
        apply1.SealSpecificationId = "13090002"
        command.Seals!!.add(apply1)

        var certificate: ApplySealCertificateData = ApplySealCertificateData()
        certificate.SealCertificateType = "01"
        certificate.SealCertificateName = "营业执照"
        val bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.yinyezhizhao)

        certificate.SealCertificateImageString = ImgUtils().bitmapToBase64(bitmap)

        var certificate1: ApplySealCertificateData = ApplySealCertificateData()
        certificate1.SealCertificateType = "02"
        certificate1.SealCertificateName = "法人代表人身份证（董事长护照)"
        //val bitmap1 = BitmapFactory.decodeResource(this.getResources(), R.drawable.yinyezhizhao)
        certificate1.SealCertificateImageString = ImgUtils().bitmapToBase64(bitmap)
        var certificate2: ApplySealCertificateData = ApplySealCertificateData()
        certificate2.SealCertificateType = "03"
        certificate2.SealCertificateName = "经办人身份证"
        //val bitmap2 = BitmapFactory.decodeResource(this.getResources(), R.drawable.yinyezhizhao)
        certificate2.SealCertificateImageString = ImgUtils().bitmapToBase64(bitmap)


        var certificate3: ApplySealCertificateData = ApplySealCertificateData()
        certificate3.SealCertificateType = "04"
        certificate3.SealCertificateName = "委托书（法人/乡政府委托书/首席代表)"
        //val bitmap3 = BitmapFactory.decodeResource(this.getResources(), R.drawable.yinyezhizhao)
        certificate3.SealCertificateImageString = ImgUtils().bitmapToBase64(bitmap)


        command.Certificates = ArrayList<ApplySealCertificateData>()
        command.Certificates!!.add(certificate)
        command.Certificates!!.add(certificate1)
        command.Certificates!!.add(certificate2)
        command.Certificates!!.add(certificate3)
        SubmitListener().Submit(Gson().toJson(command), this)
    }
}
