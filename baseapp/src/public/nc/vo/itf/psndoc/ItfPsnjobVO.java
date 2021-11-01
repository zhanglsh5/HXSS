/**
 * @Description:
 * @Author:zls
 * @Date:2021年10月26日上午10:47:06
 **/
package nc.vo.itf.psndoc;

import java.io.Serializable;

/**
 * @author zls
 * 
 */
public class ItfPsnjobVO implements Serializable {

	private static final long serialVersionUID = 8044450134241368128L;

	// 人员所属集团
	private String pk_maingroup;
	// 人员所属组织
	private String pk_mainorg;
	// 人员编码
	private String code;
	private String pk_psndoc;
	private String pk_psnjob;
	// 人员任职集团
	private String pk_group;
	// 人员任职组织
	private String pk_org;
	// 员工编号
	private String psncode;
	private String pk_psncl;
	private String pk_dept;
	private String ismainjob;
	private String indutydate;
	private String enddutydate;
	private String pk_job;
	private String jobname;
	private String pk_post;
	private String def1;
	private String def2;
	private String def3;
	private String def4;
	private String def5;
	private String def6;
	private String def7;
	private String def8;
	private String def9;
	private String def10;
	private String def11;
	private String def12;
	private String def13;
	private String def14;
	private String def15;
	private String def16;
	private String def17;
	private String def18;
	private String def19;
	private String def20;
	private Integer dataoriginflag;
	private Integer showorder;
	private Integer dr = Integer.valueOf(0);
	private String ts;
	private String errMsg;

	public String getPk_psndoc() {
		return pk_psndoc;
	}

	public void setPk_psndoc(String pk_psndoc) {
		this.pk_psndoc = pk_psndoc;
	}

	public String getPk_psnjob() {
		return pk_psnjob;
	}

	public void setPk_psnjob(String pk_psnjob) {
		this.pk_psnjob = pk_psnjob;
	}

	public String getPk_group() {
		return pk_group;
	}

	public void setPk_group(String pk_group) {
		this.pk_group = pk_group;
	}

	public String getPk_org() {
		return pk_org;
	}

	public void setPk_org(String pk_org) {
		this.pk_org = pk_org;
	}

	public String getPsncode() {
		return psncode;
	}

	public void setPsncode(String psncode) {
		this.psncode = psncode;
	}

	public String getPk_psncl() {
		return pk_psncl;
	}

	public void setPk_psncl(String pk_psncl) {
		this.pk_psncl = pk_psncl;
	}

	public String getPk_dept() {
		return pk_dept;
	}

	public void setPk_dept(String pk_dept) {
		this.pk_dept = pk_dept;
	}

	public String getIsmainjob() {
		return ismainjob;
	}

	public void setIsmainjob(String ismainjob) {
		this.ismainjob = ismainjob;
	}

	public String getIndutydate() {
		return indutydate;
	}

	public void setIndutydate(String indutydate) {
		this.indutydate = indutydate;
	}

	public String getEnddutydate() {
		return enddutydate;
	}

	public void setEnddutydate(String enddutydate) {
		this.enddutydate = enddutydate;
	}

	public String getPk_job() {
		return pk_job;
	}

	public void setPk_job(String pk_job) {
		this.pk_job = pk_job;
	}

	public String getJobname() {
		return jobname;
	}

	public void setJobname(String jobname) {
		this.jobname = jobname;
	}

	public String getPk_post() {
		return pk_post;
	}

	public void setPk_post(String pk_post) {
		this.pk_post = pk_post;
	}

	public String getDef1() {
		return def1;
	}

	public void setDef1(String def1) {
		this.def1 = def1;
	}

	public String getDef2() {
		return def2;
	}

	public void setDef2(String def2) {
		this.def2 = def2;
	}

	public String getDef3() {
		return def3;
	}

	public void setDef3(String def3) {
		this.def3 = def3;
	}

	public String getDef4() {
		return def4;
	}

	public void setDef4(String def4) {
		this.def4 = def4;
	}

	public String getDef5() {
		return def5;
	}

	public void setDef5(String def5) {
		this.def5 = def5;
	}

	public String getDef6() {
		return def6;
	}

	public void setDef6(String def6) {
		this.def6 = def6;
	}

	public String getDef7() {
		return def7;
	}

	public void setDef7(String def7) {
		this.def7 = def7;
	}

	public String getDef8() {
		return def8;
	}

	public void setDef8(String def8) {
		this.def8 = def8;
	}

	public String getDef9() {
		return def9;
	}

	public void setDef9(String def9) {
		this.def9 = def9;
	}

	public String getDef10() {
		return def10;
	}

	public void setDef10(String def10) {
		this.def10 = def10;
	}

	public String getDef11() {
		return def11;
	}

	public void setDef11(String def11) {
		this.def11 = def11;
	}

	public String getDef12() {
		return def12;
	}

	public void setDef12(String def12) {
		this.def12 = def12;
	}

	public String getDef13() {
		return def13;
	}

	public void setDef13(String def13) {
		this.def13 = def13;
	}

	public String getDef14() {
		return def14;
	}

	public void setDef14(String def14) {
		this.def14 = def14;
	}

	public String getDef15() {
		return def15;
	}

	public void setDef15(String def15) {
		this.def15 = def15;
	}

	public String getDef16() {
		return def16;
	}

	public void setDef16(String def16) {
		this.def16 = def16;
	}

	public String getDef17() {
		return def17;
	}

	public void setDef17(String def17) {
		this.def17 = def17;
	}

	public String getDef18() {
		return def18;
	}

	public void setDef18(String def18) {
		this.def18 = def18;
	}

	public String getDef19() {
		return def19;
	}

	public void setDef19(String def19) {
		this.def19 = def19;
	}

	public String getDef20() {
		return def20;
	}

	public void setDef20(String def20) {
		this.def20 = def20;
	}

	public Integer getDataoriginflag() {
		return dataoriginflag;
	}

	public void setDataoriginflag(Integer dataoriginflag) {
		this.dataoriginflag = dataoriginflag;
	}

	public Integer getShoworder() {
		return showorder;
	}

	public void setShoworder(Integer showorder) {
		this.showorder = showorder;
	}

	public Integer getDr() {
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
	}

	public String getTs() {
		return ts;
	}

	public void setTs(String ts) {
		this.ts = ts;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public String getPk_maingroup() {
		return pk_maingroup;
	}

	public void setPk_maingroup(String pk_maingroup) {
		this.pk_maingroup = pk_maingroup;
	}

	public String getPk_mainorg() {
		return pk_mainorg;
	}

	public void setPk_mainorg(String pk_mainorg) {
		this.pk_mainorg = pk_mainorg;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
