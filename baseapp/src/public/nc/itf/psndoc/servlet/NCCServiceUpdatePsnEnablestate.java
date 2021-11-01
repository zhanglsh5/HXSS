/**
 * @Description:
 * @Author:zls
 * @Date:2020��10��14������4:57:57
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

			Logger.error("��Ա��ְ�����û�������" + param);
			System.out.println("��Ա��ְ�����û�������" + param);
			ISecurityTokenCallback securityTokenCallback = NCLocator.getInstance().lookup(ISecurityTokenCallback.class);
			securityTokenCallback.token("NCSystem".getBytes(), "pfxx".getBytes());
			String pk_group = ParamUtils.getParameter("ncc.pk_group");
			String datasource = ParamUtils.getParameter("ncc.datasource");

			ItfPsnStateVO itfPsnStateVO = JSONObject.parseObject(param).toJavaObject(ItfPsnStateVO.class);
			String orgCode = itfPsnStateVO.getPk_org();
			if (StringUtils.isEmpty(orgCode) || "null".equals(orgCode)) {
				Logger.error("��Ա��ְ�����û�����������pk_org����Ϊ��");

				RequestUtil.response(out, Result.error("����pk_org����Ϊ��", param));
				return;
			}
			InvocationInfoProxy.getInstance().setGroupId(pk_group);
			InvocationInfoProxy.getInstance().setUserDataSource(datasource);
			IUAPQueryBS qry = NCLocator.getInstance().lookup(IUAPQueryBS.class);
			List<OrgVO> orgVOs = (List<OrgVO>) qry.retrieveByClause(OrgVO.class, " 11 = 11 and enablestate = 2 and isbusinessunit = 'Y' and pk_group = '" + pk_group
					+ "' and code='" + orgCode + "'  ");

			if (orgVOs == null || orgVOs.size() < 1) {
				Logger.error("NCC�����ڱ���Ϊ" + orgCode + "��ҵ��Ԫ");

				RequestUtil.response(out, Result.error("NCC�����ڱ���Ϊ" + orgCode + "��ҵ��Ԫ", param));
				return;
			}

			OrgVO orgVO = orgVOs.get(0);
			psncode = itfPsnStateVO.getCode();
			if (StringUtils.isEmpty(psncode) || "null".equals(psncode)) {
				Logger.error("��Ա��ְ����code����Ϊ��");
				RequestUtil.response(out, Result.error("����code����Ϊ��", param));
				return;
			}
			List<PsndocVO> psndocVOs = (List<PsndocVO>) qry.retrieveByClause(PsndocVO.class, " enablestate = 2 and pk_org = '" + orgVO.getPk_org() + "' and pk_group = '"
					+ pk_group + "' and code='" + psncode + "'  ");
			if (psndocVOs == null || psndocVOs.size() < 1) {
				Logger.error("ҵ��Ԫ" + orgVO.getName() + "�����ڱ���Ϊ" + psncode + "����Ա");

				RequestUtil.response(out, Result.error("ҵ��Ԫ" + orgVO.getName() + "�����ڱ���Ϊ" + psncode + "����Ա", param));
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
					RequestUtil.response(out, Result.ok("��Աͣ�óɹ�"));
					return;
				}
			}
		} catch (BusinessException e) {
			e.printStackTrace();
			Logger.error("��Ա��ְ�����û�ʧ�ܣ�������Ϣ��" + e.getMessage());
			RequestUtil.response(out, Result.error("��Ա��ְ�����û�ʧ�ܣ�������Ϣ��" + e.getMessage(), param));
			return;
		}

	}

	/*
	 * ���� Javadoc��
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
		Logger.error("readAsChars ��ȡ��Ĳ�����" + sb.toString());
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
