/**
 * @Description:
 * @Author:zls
 * @Date:2020年10月14日下午4:57:57
 **/
package nc.itf.psndoc.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.server.ISecurityTokenCallback;
import nc.bs.logging.Logger;
import nc.itf.bd.psn.psndoc.IPsndocService;
import nc.itf.org.IJobQryService;
import nc.itf.org.IPostQueryService;
import nc.itf.psndoc.util.ParamUtils;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.itf.uap.rbac.IUserManageQuery;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.pubitf.util.RequestUtil;
import nc.pubitf.util.Result;
import nc.search.ui.servlet.BaseServlet;
import nc.vo.bd.address.AddressVO;
import nc.vo.bd.psn.PsndocVO;
import nc.vo.bd.psn.PsnjobVO;
import nc.vo.bd.psnid.PsnIdtypeVO;
import nc.vo.itf.psndoc.ItfPsndocVO;
import nc.vo.itf.psndoc.ItfPsnjobVO;
import nc.vo.org.DeptVO;
import nc.vo.org.JobVO;
import nc.vo.org.OrgVO;
import nc.vo.org.PostVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFLiteralDate;
import nc.vo.sm.UserVO;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;

/**
 * 
 * @author zls
 * 
 */
@SuppressWarnings({ "restriction" })
public class NCCServiceAddPsndoc extends BaseServlet {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings({ "unchecked" })
	public void doAction(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		Logger.init("ncc_thirdinf");
		resp.setCharacterEncoding("UTF-8");
		PrintWriter out = resp.getWriter();
		String psncode = null;
		String param = RequestUtil.readAsChars(req);
		try {

			Logger.error("{0}同步NCC新增人员档案参数：" + param);
			System.out.println("同步NCC新增人员档案参数：" + param);

			// Map<String, Class<ItfPsnjobVO>> classMap = new HashMap<String,
			// Class<ItfPsnjobVO>>();
			// classMap.put("psnjobs", ItfPsnjobVO.class);

			ItfPsndocVO itfPsndocVO = JSONObject.parseObject(param).toJavaObject(ItfPsndocVO.class);
			ISecurityTokenCallback securityTokenCallback = NCLocator.getInstance().lookup(ISecurityTokenCallback.class);
			securityTokenCallback.token("NCSystem".getBytes(), "pfxx".getBytes());
			StringBuffer errHead = new StringBuffer();

			IUAPQueryBS queryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());

			String orgCode = itfPsndocVO.getPk_org();
			if (StringUtils.isEmpty(orgCode) || "null".equals(orgCode)) {
				Logger.error("同步NCC新增人员档案参数pk_org不能为空");
				errHead.append("参数pk_org不能为空;");
			}

			String pk_group = ParamUtils.getParameter("ncc.pk_group");
			String datasource = ParamUtils.getParameter("ncc.datasource");
			InvocationInfoProxy.getInstance().setGroupId(pk_group);
			InvocationInfoProxy.getInstance().setUserDataSource(datasource);
			IPsndocService psndocService = NCLocator.getInstance().lookup(IPsndocService.class);
			IUAPQueryBS qry = NCLocator.getInstance().lookup(IUAPQueryBS.class);
			List<OrgVO> orgVOs = (List<OrgVO>) qry.retrieveByClause(OrgVO.class, " 1 =1 and enablestate = 2 and isbusinessunit = 'Y' and pk_group = '" + pk_group + "' and code='"
					+ orgCode + "'  ");

			if (orgVOs == null || orgVOs.size() < 1) {
				Logger.error("NCC不存在编码为" + orgCode + "的业务单元");
				errHead.append("NCC不存在编码为" + orgCode + "的业务单元;");
			}

			OrgVO orgVO = orgVOs.get(0);
			psncode = itfPsndocVO.getCode();
			if (StringUtils.isEmpty(psncode) || "null".equals(psncode)) {
				Logger.error("同步NCC新增人员档案参数code不能为空");
				errHead.append("参数code不能为空;");
			}

			List<PsndocVO> psndocVOs = (List<PsndocVO>) qry.retrieveByClause(PsndocVO.class, " enablestate = 2 and pk_org = '" + orgVO.getPk_org() + "' and pk_group = '"
					+ pk_group + "' and code='" + psncode + "'  ");
			if (psndocVOs != null && psndocVOs.size() > 1) {
				Logger.error("业务单元" + orgVO.getName() + "已存在编码为" + psncode + "的人员");
				errHead.append("业务单元" + orgVO.getName() + "已存在编码为" + psncode + "的人员;");
			}

			String name = itfPsndocVO.getName();
			if (StringUtils.isEmpty(name) || "null".equals(name)) {
				Logger.error("参数name不能为空");
				errHead.append("参数name不能为空;");
			}
			PsndocVO psndocVO = new PsndocVO();
			psndocVO.setPk_group(pk_group);
			psndocVO.setPk_org(orgVO.getPk_org());
			psndocVO.setCode(psncode);
			psndocVO.setNickname(itfPsndocVO.getNickname());
			psndocVO.setName(name);

			Integer sex = itfPsndocVO.getSex();
			psndocVO.setSex(sex);
			String mobile = itfPsndocVO.getMobile();
			if (StringUtils.isNotEmpty(mobile) && !"null".equals(mobile)) {
				psndocVO.setMobile(mobile);
			}
			String psnnid = itfPsndocVO.getId();
			if (StringUtils.isNotEmpty(psnnid) && !"null".equals(psnnid)) {
				psndocVO.setId(psnnid);
			}
			String email = itfPsndocVO.getEmail();
			if (StringUtils.isNotEmpty(email) && !"null".equals(email)) {
				psndocVO.setEmail(email);
			}
			if (StringUtils.isNotEmpty(itfPsndocVO.getBirthdate()) && !"null".equals(itfPsndocVO.getBirthdate())) {
				psndocVO.setBirthdate(new UFLiteralDate(itfPsndocVO.getBirthdate()));
			}
			if (StringUtils.isNotEmpty(itfPsndocVO.getCreationtime()) && !"null".equals(itfPsndocVO.getCreationtime())) {
				psndocVO.setCreationtime(new UFDateTime(itfPsndocVO.getCreationtime()));
			} else {
				psndocVO.setCreationtime(new UFDateTime());
			}
			if (StringUtils.isNotEmpty(itfPsndocVO.getCreator()) && !"null".equals(itfPsndocVO.getCreator())) {
				IUserManageQuery query = (IUserManageQuery) NCLocator.getInstance().lookup(IUserManageQuery.class);
				UserVO userVO = query.findUserByCode(itfPsndocVO.getCreator(), datasource);
				if (userVO != null) {
					psndocVO.setCreator(userVO.getCuserid());
				}
			}
			psndocVO.setFirstname(itfPsndocVO.getFirstname());
			psndocVO.setHomephone(itfPsndocVO.getHomephone());
			if (StringUtils.isNotEmpty(itfPsndocVO.getIdtype()) && !"null".equals(itfPsndocVO.getIdtype())) {
				List<PsnIdtypeVO> psnIdtypeVOs = (List<PsnIdtypeVO>) queryBS.retrieveByClause(PsnIdtypeVO.class, " code ='" + itfPsndocVO.getIdtype() + "'");
				if (psnIdtypeVOs != null && psnIdtypeVOs.size() > 0) {
					psndocVO.setIdtype(psnIdtypeVOs.get(0).getPk_identitype());
				}
			}

			if (StringUtils.isNotEmpty(itfPsndocVO.getJoinworkdate()) && !"null".equals(itfPsndocVO.getJoinworkdate())) {
				psndocVO.setJoinworkdate(new UFLiteralDate(itfPsndocVO.getJoinworkdate()));
			}
			psndocVO.setLastname(itfPsndocVO.getLastname());
			psndocVO.setMnecode(itfPsndocVO.getMnecode());
			psndocVO.setOfficephone(itfPsndocVO.getOfficephone());
			psndocVO.setUsedname(itfPsndocVO.getUsedname());
			if (StringUtils.isNotEmpty(itfPsndocVO.getAddr()) && !"null".equals(itfPsndocVO.getAddr())) {
				AddressVO addressVO = new AddressVO();
				addressVO.setDetailinfo(itfPsndocVO.getAddr());
				IVOPersistence voPersistence = NCLocator.getInstance().lookup(IVOPersistence.class);
				String pk_address = voPersistence.insertVO(addressVO);
				psndocVO.setAddr(pk_address);
			}

			List<PsnjobVO> psnjobList = new ArrayList<PsnjobVO>();
			ItfPsnjobVO[] itfPsnjobVOs = itfPsndocVO.getPsnjobs();

			StringBuffer sb = new StringBuffer();
			boolean checkFlag = true;
			for (ItfPsnjobVO itfPsnjobVO : itfPsnjobVOs) {
				String deptCode = itfPsnjobVO.getPk_dept();
				String psnclCode = itfPsnjobVO.getPk_psncl();

				if (StringUtils.isEmpty(psnclCode) || "null".equals(psnclCode)) {
					Logger.error("参数pk_psncl不能为空");
					sb.append("参数pk_psncl不能为空;");
				}
				PsnjobVO psnJob = null;

				List<DeptVO> deptVOs = (List<DeptVO>) qry.retrieveByClause(DeptVO.class, " enablestate = 2 and pk_org = '" + orgVO.getPk_org() + "' and pk_group = '" + pk_group
						+ "' and code='" + deptCode + "'  ");
				if (deptVOs == null || deptVOs.size() < 1) {
					Logger.error("业务单元" + orgVO.getName() + "不存在编码为" + orgCode + "的部门");
					sb.append("业务单元" + orgVO.getName() + "不存在编码为" + orgCode + "的部门;");
				}

				psnJob = new PsnjobVO();
				psnJob.setPk_group(pk_group);
				psnJob.setPk_org(orgVO.getPk_org());
				psnJob.setPk_dept(deptVOs.get(0).getPk_dept());
				psnJob.setPsncode(psncode);
				String pk_psncl = (String) qry.executeQuery("select pk_psncl from bd_psncl where pk_group='" + pk_group + "' and code ='" + psnclCode
						+ "' and nvl(dr,0)=0 and enablestate=2", new ColumnProcessor());
				if (StringUtils.isEmpty(pk_psncl)) {
					Logger.error("NCC不存在编码为" + psnclCode + "的人员类别");
					sb.append("NCC不存在编码为" + psnclCode + "的人员类别;");
				}
				psnJob.setPk_psncl(pk_psncl);

				String indutydate = itfPsnjobVO.getIndutydate();
				if (StringUtils.isEmpty(indutydate) || "null".equals(indutydate)) {
					psnJob.setIndutydate(new UFLiteralDate());
				} else {
					psnJob.setIndutydate(new UFLiteralDate(indutydate));
				}
				// 主职
				psnJob.setIsmainjob(StringUtils.isEmpty(itfPsnjobVO.getIsmainjob()) ? UFBoolean.FALSE : ("Y".equals(itfPsnjobVO.getIsmainjob()) ? UFBoolean.TRUE : UFBoolean.FALSE));

				if (StringUtils.isNotEmpty(itfPsnjobVO.getEnddutydate()) && !"null".equals(itfPsnjobVO.getEnddutydate())) {
					psnJob.setIndutydate(new UFLiteralDate(itfPsnjobVO.getEnddutydate()));
				}
				// 岗位
				if (StringUtils.isNotEmpty(itfPsnjobVO.getPk_post()) && !"null".equals(itfPsnjobVO.getPk_post())) {
					IPostQueryService postQueryService = NCLocator.getInstance().lookup(IPostQueryService.class);
					PostVO[] postVOs = postQueryService.queryByOrgIDAndByDeptAndClause(psnJob.getPk_org(), psnJob.getPk_dept(), " postcode ='" + itfPsnjobVO.getPk_post() + "'");
					if (postVOs != null && postVOs.length > 0) {
						psnJob.setPk_post(postVOs[0].getPk_post());
					}
				}
				// 职务
				if (StringUtils.isNotEmpty(itfPsnjobVO.getPk_job()) && !"null".equals(itfPsnjobVO.getPk_job())) {
					IJobQryService jobQryService = NCLocator.getInstance().lookup(IJobQryService.class);
					JobVO[] jobVOs = jobQryService.queryJobVOSByClause(" jobcode ='" + itfPsnjobVO.getPk_job() + "'");
					if (jobVOs != null && jobVOs.length > 0) {
						psnJob.setPk_job(jobVOs[0].getPk_job());
						psnJob.setJobname(jobVOs[0].getJobname());
					}
				}
				psnjobList.add(psnJob);
				if (sb.length() > 0) {
					itfPsnjobVO.setErrMsg(sb.toString());
					checkFlag = false;
				}
			}

			if (!checkFlag || errHead.length() > 0) {
				itfPsndocVO.setErrMsg(errHead.toString());
				RequestUtil.response(out, Result.error("人员新增失败：", JSONObject.toJSON(itfPsndocVO).toString()));
				return;
			}
			if (psnjobList.size() > 0) {
				psndocVO.setPsnjobs(psnjobList.toArray(new PsnjobVO[psnjobList.size()]));
			} else {
				RequestUtil.response(out, Result.error("psnjobs参数不能为空", JSONObject.toJSON(itfPsndocVO).toString()));
				return;
			}
			PsndocVO newPsndocVO = psndocService.insertPsndoc(psndocVO, false);
			if (newPsndocVO != null) {
				RequestUtil.response(out, Result.ok("人员新增成功"));
				return;
				// IUserGroupQueryService userGroupQueryService =
				// NCLocator.getInstance().lookup(IUserGroupQueryService.class);
				// UserGroupVO[] UserGroupVOs =
				// userGroupQueryService.queryUserGroupBySqlWhere(" pk_org='" +
				// orgVO.getPk_org() + "' and nvl(dr,0)=0");
				// if (UserGroupVOs != null && UserGroupVOs.length > 0) {
				// // 人员新增成功以后，生成对应NC用户
				// PsndocExtend vo = new PsndocExtend();
				// vo.setPsnDocVo(newPsndocVO);
				// vo.setUser_code(psncode);
				// PsndocExtend[] nPsndocExtVO = psndocService.createUser(new
				// PsndocExtend[] { vo }, UserGroupVOs[0].getPk_usergroup(),
				// "password_default");
				//
				// if (nPsndocExtVO == null || nPsndocExtVO.length == 0) {
				// // result.put("result", "0");
				// // result.put("msg", "工号为" + psncode + "的人员新增成功");
				// }
				// } else {
				// // result.put("result", "0");
				// // result.put("msg", "工号为" + psncode +
				// // "的人员新增成功，人员所在组织未创建用户组，未生成NC用户");
				//
				// }
			}

		} catch (BusinessException e) {
			e.printStackTrace();
			Logger.error("人员新增失败：" + e.getMessage());
			RequestUtil.response(out, Result.error("人员新增失败：" + e.getMessage(), param));
			return;
		}
	}

	@Override
	public void doInit() {

	}

	@Override
	public void invoke(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doAction(req, resp);
	}

}
