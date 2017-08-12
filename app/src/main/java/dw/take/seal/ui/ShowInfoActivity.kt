package dw.take.seal.ui

import android.app.ProgressDialog
import android.content.Intent
import dw.take.seal.R
import wai.gr.cla.base.BaseActivity
import java.util.*
import com.google.gson.Gson
import com.kaopiz.kprogresshud.KProgressHUD
import dw.take.seal.control.SubmitListener
import dw.take.seal.control.SubmitView
import dw.take.seal.method.CommonAdapter
import dw.take.seal.method.CommonViewHolder
import dw.take.seal.model.*
import kotlinx.android.synthetic.main.activity_show_info.*
import net.tsz.afinal.view.LoadingDialog
import wai.gr.cla.model.key

//信息确认
class ShowInfoActivity : BaseActivity(), SubmitView {
    var regionid: String = ""
    var adapter: CommonAdapter<SealModel>? = null
    var list: MutableList<SealModel>? = null
    var isFa: Boolean = true
    var pdialog: KProgressHUD? = null
    var orgs: MutableList<OrganizationJianModel>? = null//营业执照信息
    var cards: MutableList<CardInfoModel>? = null//如果是法人就是法人信息，不是法人就是经办人信息
    override fun getCertifyNumberResult(success: Boolean, result: String) {
        if (success) {
            regionid = result
            loadData()
            //是否成功界面
        } else {
            if (pdialog != null) {
                pdialog!!.dismiss()
            }
            toast("提交出错：" + result)
        }
    }

    override fun SubmitResult(success: Boolean, result: String) {
        if (pdialog != null) {
            pdialog!!.dismiss()
        }
        if (success) {
            val intent = Intent(this@ShowInfoActivity, CompleteActivity::class.java)
            startActivity(intent)
        } else {
            //toast("提交出错：" + result)
            val intent = Intent(this@ShowInfoActivity, CompleteErrorActivity::class.java)
            intent.putExtra("resultmes","提交出错：" +result)
            startActivity(intent)
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
        showinfo_close_btn.setOnClickListener { finish() }
        showinfo_next_btn.setOnClickListener {
            pdialog = KProgressHUD.create(this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("加载中")
                    .setCancellable(true)
                    .setAnimationSpeed(2)
                    .setDimAmount(0.5f)
            pdialog!!.show()
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

        orgs = findb!!.findAll(OrganizationJianModel::class.java)
        var where: String = "true"
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
        cards = findb!!.findAllByWhere(CardInfoModel::class.java, "isFaren='" + where + "'")
        if (cards!!.size > 0) {
            showinfo_tv_jingname.text = cards!![0].personName
            show_info_zj.text = cards!![0].identyNumber
            showinfo_tv_jmobile.text = dw.take.seal.utils.Utils(this).ReadString(key.KEY_MOBILE_NUMBER)
        }
        if (orgs!!.size > 0) {
            showinfo_tv_uc.text = orgs!![0].organizationUSCC
            showinfo_tv_cname.text = orgs!![0].organizationName
            showinfo_tv_daibiao.text = orgs!![0].organizationLeader
        } else {
            toast("加载数据失败")
            finish()
        }
    }

    fun loadData() {
        var command: ApplySealCommand = ApplySealCommand()
        var group: GroupModel = GroupModel();
        group.SealCertifyNumber = regionid
        if (isFa) {
            //法人证件信息
            group.OrganizationLeader = cards!![0].personName
            group.OrganizationLeaderCertNumber = cards!![0].identyNumber
            group.OrganizationLeaderMobileNumber = showinfo_tv_jmobile.text.toString()

            group.SealApplyer = cards!![0].personName
            group.SealApplyerCertNumber = cards!![0].identyNumber
            group.SealApplyerMobileNumber = showinfo_tv_jmobile.text.toString()
            group.OrganizationTelephoneNumber = showinfo_tv_jmobile.text.toString()
            group.SealRegister = cards!![0].personName
        } else {
            //经办人姓名
            group.SealApplyer = cards!![0].personName
            group.SealApplyerCertNumber = cards!![0].identyNumber
            group.SealApplyerMobileNumber = showinfo_tv_jmobile.text.toString()
            //加载法人信息
            var cardjings = findb!!.findAllByWhere(CardInfoModel::class.java, "isFaren='true'")
            //法人证件信息
            if (cardjings.size > 0) {
                group.OrganizationLeader = cardjings!![0].personName
                group.OrganizationLeaderCertNumber = cardjings!![0].identyNumber
                 group.OrganizationLeaderMobileNumber =showinfo_tv_jmobile.text.toString()
            }
            group.OrganizationTelephoneNumber = showinfo_tv_jmobile.text.toString()
            group.SealRegister = cards!![0].personName
        }
        //营业执照信息
        group.OrganizationRegionId = orgs!![0].organizationRegionId
        group.OrganizationName = orgs!![0].organizationName
        group.OrganizationUSCC = orgs!![0].organizationUSCC
        group.OrganizationAddress = orgs!![0].organizationAddress
        group.OrganizationPostCode = "000000"
        group.SealContractShopId = dw.take.seal.utils.Utils(this).ReadString(key.KEY_SHOP_ID)

        group.SealRegisteDepartmentId = "123456789874"
        if (isFa) {
            group.SealIdentificationRemark = "法人证件识别,、法人人像比对" + dw.take.seal.utils.Utils(this).ReadString(key.KEY_TAKESEAL_XSD) + "。"
        } else {
            group.SealIdentificationRemark = "营业执照扫码验证、经办人证件识别、法人证件识别、经办人人像比对，" + dw.take.seal.utils.Utils(this).ReadString(key.KEY_TAKESEAL_XSD) + "。"
        }


        command.Group = group
        command.Seals = ArrayList<ApplySealData>()

        if (list!!.size > 0) {
            for (i in 0..list!!.size - 1) {
                var apply: SealModel = list!![i]
                if (apply.num > 0) {
                    for (j in 0..apply.num - 1) {
                        var applymodel: ApplySealData = ApplySealData()
                        var name: String = ""
                        if (j > 0) {
                            name = " ("+j.toString()+")"
                        }
                        if(apply.SealType.equals("04")){
                            applymodel.SealContent = apply.SealContent + " " + apply.SealTypeName + name+" "+group.OrganizationUSCC
                        }else{
                            applymodel.SealContent = apply.SealContent + " " + apply.SealTypeName + name
                        }

                        applymodel.SealType = apply.SealType
                        applymodel.SealMaterial = apply.SealSpecificationId
                        applymodel.SealSpecificationId = apply.SealGGId
                        command.Seals!!.add(applymodel)
                    }
                }
            }
        }
//        var apply: ApplySealData = ApplySealData()
//        apply.SealContent = "中信银行股份有限公司沧州分行"
//        apply.SealType = "01"
//        apply.SealMaterial = "04"
//        apply.SealSpecificationId = "13090002"
//        command.Seals!!.add(apply)
//        var apply1: ApplySealData = ApplySealData()
//        apply1.SealContent = "中信银行股份有限公司沧州分行1"
//        apply1.SealType = "02"
//        apply1.SealMaterial = "03"
//        apply1.SealSpecificationId = "13090002"
//        command.Seals!!.add(apply1)

//        var certificate: ApplySealCertificateData = ApplySealCertificateData()
//        certificate.SealCertificateType = "01"
//        certificate.SealCertificateName = "营业执照"
//        val bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.yinyezhizhao)
//
//        certificate.SealCertificateImageString = ImgUtils().bitmapToBase64(bitmap)

//        var certificate1: ApplySealCertificateData = ApplySealCertificateData()
//        certificate1.SealCertificateType = "02"
//        certificate1.SealCertificateName = "法人代表人身份证（董事长护照)"
//        //val bitmap1 = BitmapFactory.decodeResource(this.getResources(), R.drawable.yinyezhizhao)
//        certificate1.SealCertificateImageString = ImgUtils().bitmapToBase64(bitmap)
//        var certificate2: ApplySealCertificateData = ApplySealCertificateData()
//        certificate2.SealCertificateType = "03"
//        certificate2.SealCertificateName = "经办人身份证"
//        //val bitmap2 = BitmapFactory.decodeResource(this.getResources(), R.drawable.yinyezhizhao)
//        certificate2.SealCertificateImageString = ImgUtils().bitmapToBase64(bitmap)
//
//
//        var certificate3: ApplySealCertificateData = ApplySealCertificateData()
//        certificate3.SealCertificateType = "04"
//        certificate3.SealCertificateName = "委托书（法人/乡政府委托书/首席代表)"
//        //val bitmap3 = BitmapFactory.decodeResource(this.getResources(), R.drawable.yinyezhizhao)
//        certificate3.SealCertificateImageString = ImgUtils().bitmapToBase64(bitmap)


        command.Certificates = ArrayList<ApplySealCertificateData>()
        command.Certificates = findb!!.findAll(ApplySealCertificateData::class.java) as ArrayList<ApplySealCertificateData>
//        command.Certificates!!.add(certificate)
//        command.Certificates!!.add(certificate1)
//        command.Certificates!!.add(certificate2)
//        command.Certificates!!.add(certificate3)

        SubmitListener().Submit(Gson().toJson(command), this)
    }
}
