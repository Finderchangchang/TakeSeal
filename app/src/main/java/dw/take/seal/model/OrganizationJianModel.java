package dw.take.seal.model;

import java.io.Serializable;

/**
 * 扫描二维码识别营业执照
 * Created by Administrator on 2017/7/31.
 */

public class OrganizationJianModel implements Serializable {
    //91130602MA0832AR0J
    //信用代码
    private String OrganizationUSCC;
    //保定栎拓商贸有限公司
    //公司名称
    private String OrganizationName;
    //王广跃
    //法定代表人
    private String OrganizationLeader;
    //保定市竞秀区韩村乡万和公寓2204室商用
    //住所
    private String OrganizationAddress;
    //2016-12-21 00:00 成立时间
    private String OrganizationEstablishDate;

    public String getOrganizationUSCC() {
        return OrganizationUSCC;
    }

    public void setOrganizationUSCC(String organizationUSCC) {
        OrganizationUSCC = organizationUSCC;
    }

    public String getOrganizationName() {
        return OrganizationName;
    }

    public void setOrganizationName(String organizationName) {
        OrganizationName = organizationName;
    }

    public String getOrganizationLeader() {
        return OrganizationLeader;
    }

    public void setOrganizationLeader(String organizationLeader) {
        OrganizationLeader = organizationLeader;
    }

    public String getOrganizationAddress() {
        return OrganizationAddress;
    }

    public void setOrganizationAddress(String organizationAddress) {
        OrganizationAddress = organizationAddress;
    }

    public String getOrganizationEstablishDate() {
        return OrganizationEstablishDate;
    }

    public void setOrganizationEstablishDate(String organizationEstablishDate) {
        OrganizationEstablishDate = organizationEstablishDate;
    }
}
