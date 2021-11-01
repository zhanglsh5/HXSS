package nc.itf.psndoc.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.server.ISecurityTokenCallback;
import nc.bs.logging.Logger;
import nc.itf.bd.psn.psndoc.IPsndocQueryService;
import nc.itf.bd.psn.psndoc.IPsndocService;
import nc.itf.psndoc.util.ParamUtils;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.itf.uap.rbac.IUserManageQuery;
import nc.pubitf.util.RequestUtil;
import nc.search.ui.servlet.BaseServlet;
import nc.vo.bd.address.AddressVO;
import nc.vo.bd.psn.PsndocVO;
import nc.vo.bd.psnid.PsnIdtypeVO;
import nc.vo.itf.psndoc.ItfPsndocVO;
import nc.vo.org.OrgVO;
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
public class NCCServiceEditPsndoc extends BaseServlet {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings({ "unchecked" })
	public void doAction(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		Logger.init("zhongzhou");
		Map<String, Object> result = new HashMap<String, Object>();
		resp.setCharacterEncoding("UTF-8");
		PrintWriter out = resp.getWriter();
		String psncode = null;
		try {
			String param = RequestUtil.readAsChars(req);
			Logger.error("修改人员基本信息同步NCC参数：" + param);
			System.out.println("修改人员基本信息同步NCC参数：" + param);
			ISecurityTokenCallback securityTokenCallback = NCLocator.getInstance().lookup(ISecurityTokenCallback.class);
			securityTokenCallback.token("NCSystem".getBytes(), "pfxx".getBytes());

			ItfPsndocVO itfPsndocVO = JSONObject.parseObject(param).toJavaObject(ItfPsndocVO.class);

			String orgCode = itfPsndocVO.getPk_org();
			if (StringUtils.isEmpty(orgCode) || "null".equals(orgCode)) {
				result.put("result", "1");
				result.put("msg", "参数pk_org不能为空");
				Logger.error("修改人员基本信息同步NCC pk_org不能为空");
				out.println(new JSONObject(result).toString());
				out.flush();
				out.close();
			}
			String pk_group = ParamUtils.getParameter("ncc.pk_group");
			String datasource = ParamUtils.getParameter("ncc.datasource");

			InvocationInfoProxy.getInstance().setGroupId(pk_group);
			InvocationInfoProxy.getInstance().setUserDataSource(datasource);
			IPsndocService psndocService = NCLocator.getInstance().lookup(IPsndocService.class);
			IPsndocQueryService psndocQueryService = NCLocator.getInstance().lookup(IPsndocQueryService.class);

			IUAPQueryBS qry = NCLocator.getInstance().lookup(IUAPQueryBS.class);
			List<OrgVO> orgVOs = (List<OrgVO>) qry.retrieveByClause(OrgVO.class, " 11 = 11 and enablestate = 2 and isbusinessunit = 'Y' and pk_group = '" + pk_group
					+ "' and code='" + orgCode + "'  ");

			if (orgVOs == null || orgVOs.size() < 1) {
				result.put("result", "1");
				result.put("msg", "NCC不存在编码为" + orgCode + "的业务单元");
				Logger.error("NCC不存在编码为" + orgCode + "的业务单元");
				out.println(new JSONObject(result).toString());
				out.flush();
				out.close();
			}
			PsndocVO psndocVO = null;
			OrgVO orgVO = orgVOs.get(0);
			psncode = itfPsndocVO.getCode();
			if (StringUtils.isEmpty(psncode) || "null".equals(psncode)) {
				result.put("result", "1");
				result.put("msg", "参数code不能为空");
				Logger.error("修改人员基本信息同步NCC code不能为空");
				out.println(new JSONObject(result).toString());
				out.flush();
				out.close();
			}
			// String oaid = getString(json, "userid");
			// PsndocVO[] existsPsnDocs = null;
			// existsPsnDocs =
			// psndocQueryService.queryPsndocVOsByCondition(" (nickname='" +
			// oaid + "' )  and nvl(dr,0)=0 ");

			IUAPQueryBS queryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());

			// if (existsPsnDocs != null && existsPsnDocs.length > 0) {
			// psndocVO = existsPsnDocs[0];
			// }
			if (psndocVO == null) {
				PsndocVO[] psndocVOs = psndocQueryService.queryPsndocVOsByCondition("  pk_group = '" + pk_group + "' and code='" + psncode + "' and nvl(dr,0)=0 ");
				if (psndocVOs == null || psndocVOs.length < 1) {
					result.put("result", "1");
					result.put("msg", "业务单元" + orgVO.getName() + "不存在编码为" + psncode + "的人员");
					Logger.error("业务单元" + orgVO.getName() + "不存在编码为" + psncode + "的人员");
					out.println(new JSONObject(result).toString());
					out.flush();
					out.close();
				} else {
					psndocVO = psndocVOs[0];
				}
			}

			String name = itfPsndocVO.getName();
			if (StringUtils.isEmpty(name) || "null".equals(name)) {
				Logger.error("参数name不能为空");
			}
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

			PsndocVO retpsndocVO = psndocService.updatePsndoc(psndocVO, true);
			if (retpsndocVO != null) {
				result.put("result", "0");
				result.put("msg", "修改编码为" + psncode + "人员基本信息成功");
				result.put("code", psncode);
				Logger.error("修改编码为" + psncode + "人员基本信息成功");
				out.println(new JSONObject(result).toString());
				out.flush();
				out.close();
			} else {
				result.put("result", "1");
				result.put("msg", "修改编码为" + psncode + "人员基本信息失败");
				result.put("code", psncode);
				Logger.error("修改编码为" + psncode + "人员基本信息失败");
				out.println(new JSONObject(result).toString());
				out.flush();
				out.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
			result.put("result", "1");
			result.put("msg", e.getMessage());
			result.put("code", psncode);
			Logger.error("同步NCC修改人员基本信息失败，返回信息：" + e.getMessage());

			out.println(new JSONObject(result).toString());
			out.flush();
			out.close();
		}

	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.search.ui.servlet.BaseServlet#doInit()
	 */
	@Override
	public void doInit() {

	}

	@Override
	public void invoke(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doAction(req, resp);
	}

}
