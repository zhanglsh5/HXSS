/**
 * @Description:
 * @Author:zls
 * @Date:2021年10月29日下午4:50:51
 **/
package nc.vo.itf.psndoc;

import java.io.Serializable;

/**
 * @author zls
 * 
 */
public class ItfPsnStateVO implements Serializable {
	private static final long serialVersionUID = 1228997684159438644L;
	// 人员任职集团
	private String pk_group;
	// 人员任职组织
	private String pk_org;
	// 人员编号编号
	private String code;
	// 人员状态
	private Integer enablestate;
	// 是否同步
	private String lockUser;

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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getEnablestate() {
		return enablestate;
	}

	public void setEnablestate(Integer enablestate) {
		this.enablestate = enablestate;
	}

	public String getLockUser() {
		return lockUser;
	}

	public void setLockUser(String lockUser) {
		this.lockUser = lockUser;
	}

}
