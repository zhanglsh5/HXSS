/**
 * @Description:
 * @Author:zls
 * @Date:2021年10月29日下午5:18:54
 **/
package nc.vo.itf.marbasclass;

import java.io.Serializable;
import java.util.Map;

import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;

/**
 * @author zls
 * 
 */
public class ItfMarBasClassVO implements Serializable {

	private static final long serialVersionUID = -8009593532634699892L;
	private String pk_marbasclass;
	private String pk_group;
	private String pk_org;
	private String code;
	private String name;
	private String name2;
	private String name3;
	private String name4;
	private String name5;
	private String name6;
	private String pk_parent;
	private UFDouble avgprice;
	private UFDouble averagecost;
	private Integer averagepurahead;
	private Integer averagemmahead;
	private String pk_marasstframe;
	private Integer enablestate = Integer.valueOf(2);
	private String creator;
	private UFDateTime creationtime;
	private String modifier;
	private UFDateTime modifiedtime;
	private String innercode;
	private Integer dataoriginflag = Integer.valueOf(0);
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
	private Integer dr = Integer.valueOf(0);
	private UFDateTime ts;
	private Map<String, Object> extendObjMap;

	public String getPk_marbasclass() {
		return pk_marbasclass;
	}

	public void setPk_marbasclass(String pk_marbasclass) {
		this.pk_marbasclass = pk_marbasclass;
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName2() {
		return name2;
	}

	public void setName2(String name2) {
		this.name2 = name2;
	}

	public String getName3() {
		return name3;
	}

	public void setName3(String name3) {
		this.name3 = name3;
	}

	public String getName4() {
		return name4;
	}

	public void setName4(String name4) {
		this.name4 = name4;
	}

	public String getName5() {
		return name5;
	}

	public void setName5(String name5) {
		this.name5 = name5;
	}

	public String getName6() {
		return name6;
	}

	public void setName6(String name6) {
		this.name6 = name6;
	}

	public String getPk_parent() {
		return pk_parent;
	}

	public void setPk_parent(String pk_parent) {
		this.pk_parent = pk_parent;
	}

	public UFDouble getAvgprice() {
		return avgprice;
	}

	public void setAvgprice(UFDouble avgprice) {
		this.avgprice = avgprice;
	}

	public UFDouble getAveragecost() {
		return averagecost;
	}

	public void setAveragecost(UFDouble averagecost) {
		this.averagecost = averagecost;
	}

	public Integer getAveragepurahead() {
		return averagepurahead;
	}

	public void setAveragepurahead(Integer averagepurahead) {
		this.averagepurahead = averagepurahead;
	}

	public Integer getAveragemmahead() {
		return averagemmahead;
	}

	public void setAveragemmahead(Integer averagemmahead) {
		this.averagemmahead = averagemmahead;
	}

	public String getPk_marasstframe() {
		return pk_marasstframe;
	}

	public void setPk_marasstframe(String pk_marasstframe) {
		this.pk_marasstframe = pk_marasstframe;
	}

	public Integer getEnablestate() {
		return enablestate;
	}

	public void setEnablestate(Integer enablestate) {
		this.enablestate = enablestate;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public UFDateTime getCreationtime() {
		return creationtime;
	}

	public void setCreationtime(UFDateTime creationtime) {
		this.creationtime = creationtime;
	}

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public UFDateTime getModifiedtime() {
		return modifiedtime;
	}

	public void setModifiedtime(UFDateTime modifiedtime) {
		this.modifiedtime = modifiedtime;
	}

	public String getInnercode() {
		return innercode;
	}

	public void setInnercode(String innercode) {
		this.innercode = innercode;
	}

	public Integer getDataoriginflag() {
		return dataoriginflag;
	}

	public void setDataoriginflag(Integer dataoriginflag) {
		this.dataoriginflag = dataoriginflag;
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

	public Integer getDr() {
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
	}

	public UFDateTime getTs() {
		return ts;
	}

	public void setTs(UFDateTime ts) {
		this.ts = ts;
	}

	public Map<String, Object> getExtendObjMap() {
		return extendObjMap;
	}

	public void setExtendObjMap(Map<String, Object> extendObjMap) {
		this.extendObjMap = extendObjMap;
	}
}
