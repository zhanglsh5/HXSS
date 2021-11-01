/**
 * @Description:
 * @Author:zls
 * @Date:2021��10��29������4:50:51
 **/
package nc.vo.itf.psndoc;

import java.io.Serializable;

/**
 * @author zls
 * 
 */
public class ItfPsnStateVO implements Serializable {
	private static final long serialVersionUID = 1228997684159438644L;
	// ��Ա��ְ����
	private String pk_group;
	// ��Ա��ְ��֯
	private String pk_org;
	// ��Ա��ű��
	private String code;
	// ��Ա״̬
	private Integer enablestate;
	// �Ƿ�ͬ��
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
