/**
 * @Description:
 * @Author:zls
 * @Date:2020年10月14日下午4:57:57
 **/
package nc.itf.psndoc.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.server.ISecurityTokenCallback;
import nc.bs.logging.Logger;
import nc.itf.psndoc.util.ParamUtils;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.rbac.IRoleManage;
import nc.itf.uap.rbac.IRoleManageQuery;
import nc.itf.uap.rbac.IUserManage;
import nc.itf.uap.rbac.IUserManageQuery;
import nc.pubitf.util.RequestUtil;
import nc.pubitf.util.Result;
import nc.search.ui.servlet.BaseServlet;
import nc.vo.bd.psn.PsndocVO;
import nc.vo.itf.psndoc.ItfPsnStateVO;
import nc.vo.org.OrgVO;
import nc.vo.pub.BusinessException;
import nc.vo.sm.UserVO;
import nc.vo.uap.rbac.role.RoleVO;
import org.apache.commons.lang3.StringUtils;
import com.alibaba.fastjson.JSONObject;

/**
 * 
 * @author zls
 * 
 * 
 */
@SuppressWarnings({ "restriction" })
public class NCCServiceUpdatePsnEnablestate extends BaseServlet {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings({ "unchecked" })
	public void doAction(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		Logger.init("zhongzhou");
		resp.setCharacterEncoding("UTF-8");
		PrintWriter out = resp.getWriter();
		String psncode = null;
		String param = RequestUtil.readAsChars(req);
		try {

			Logger.error("人员离职锁定用户参数：" + param);
			System.out.println("人员离职锁定用户参数：" + param);
			ISecurityTokenCallback securityTokenCallback = NCLocator.getInstance().lookup(ISecurityTokenCallback.class);
			securityTokenCallback.token("NCSystem".getBytes(), "pfxx".getBytes());
			String pk_group = ParamUtils.getParameter("ncc.pk_group");
			String datasource = ParamUtils.getParameter("ncc.datasource");

			ItfPsnStateVO itfPsnStateVO = JSONObject.parseObject(param).toJavaObject(ItfPsnStateVO.class);
			String orgCode = itfPsnStateVO.getPk_org();
			if (StringUtils.isEmpty(orgCode) || "null".equals(orgCode)) {
				Logger.error("人员离职锁定用户参数：参数pk_org不能为空");

				RequestUtil.response(out, Result.error("参数pk_org不能为空", param));
				return;
			}
			InvocationInfoProxy.getInstance().setGroupId(pk_group);
			InvocationInfoProxy.getInstance().setUserDataSource(datasource);
			IUAPQueryBS qry = NCLocator.getInstance().lookup(IUAPQueryBS.class);
			List<OrgVO> orgVOs = (List<OrgVO>) qry.retrieveByClause(OrgVO.class, " 11 = 11 and enablestate = 2 and isbusinessunit = 'Y' and pk_group = '" + pk_group
					+ "' and code='" + orgCode + "'  ");

			if (orgVOs == null || orgVOs.size() < 1) {
				Logger.error("NCC不存在编码为" + orgCode + "的业务单元");

				RequestUtil.response(out, Result.error("NCC不存在编码为" + orgCode + "的业务单元", param));
				return;
			}

			OrgVO orgVO = orgVOs.get(0);
			psncode = itfPsnStateVO.getCode();
			if (StringUtils.isEmpty(psncode) || "null".equals(psncode)) {
				Logger.error("人员离职参数code不能为空");
				RequestUtil.response(out, Result.error("参数code不能为空", param));
				return;
			}
			List<PsndocVO> psndocVOs = (List<PsndocVO>) qry.retrieveByClause(PsndocVO.class, " enablestate = 2 and pk_org = '" + orgVO.getPk_org() + "' and pk_group = '"
					+ pk_group + "' and code='" + psncode + "'  ");
			if (psndocVOs == null || psndocVOs.size() < 1) {
				Logger.error("业务单元" + orgVO.getName() + "不存在编码为" + psncode + "的人员");

				RequestUtil.response(out, Result.error("业务单元" + orgVO.getName() + "不存在编码为" + psncode + "的人员", param));
				return;
			}

			if ("Y".equals(itfPsnStateVO.getLockUser())) {
				IUserManageQuery userManageQuery = NCLocator.getInstance().lookup(IUserManageQuery.class);
				UserVO userVO = userManageQuery.queryUserVOByPsnDocID(psndocVOs.get(0).getPk_psndoc());
				if (userVO != null) {
					IUserManage userManage = NCLocator.getInstance().lookup(IUserManage.class);
					userManage.disableUser(userVO);

					IRoleManageQuery roleManageQuery = NCLocator.getInstance().lookup(IRoleManageQuery.class);
					IRoleManage roleManage = NCLocator.getInstance().lookup(IRoleManage.class);
					RoleVO[] currUserRoleVOs = roleManageQuery.queryRoleByUserID(userVO.getPrimaryKey(), null);
					if (currUserRoleVOs != null) {
						for (RoleVO urVO : currUserRoleVOs) {
							roleManage.unassignUserFromRole(urVO.getPk_role(), new String[] { userVO.getPrimaryKey() });
						}
					}
					RequestUtil.response(out, Result.ok("人员停用成功"));
					return;
				}
			}
		} catch (BusinessException e) {
			e.printStackTrace();
			Logger.error("人员离职锁定用户失败，返回信息：" + e.getMessage());
			RequestUtil.response(out, Result.error("人员离职锁定用户失败，返回信息：" + e.getMessage(), param));
			return;
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

	public String readAsChars(HttpServletRequest request) {
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		try {
			br = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
			String str = null;
			while ((str = br.readLine()) != null) {
				sb.append(str);
			}
		} catch (IOException e) {
			e.printStackTrace();
			Logger.error(e.getMessage());
		} finally {
			try {
				if (null != br) {
					br.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Logger.error("readAsChars 读取后的参数：" + sb.toString());
		return sb.toString();
	}

	private String getString(JSONObject json, String key) {
		if (json.containsKey(key)) {
			return json.getString(key);
		} else {
			return "";
		}
	}
}
