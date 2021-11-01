/**
 * @Description:
 * @Author:zls
 * @Date:2020��10��14������4:57:57
 **/
package nc.itf.psndoc.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
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
import nc.itf.org.IJobQryService;
import nc.itf.org.IPostQueryService;
import nc.itf.psndoc.util.ParamUtils;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.pubitf.util.RequestUtil;
import nc.pubitf.util.Result;
import nc.search.ui.servlet.BaseServlet;
import nc.vo.bd.psn.PsndocVO;
import nc.vo.bd.psn.PsnjobVO;
import nc.vo.itf.psndoc.ItfPsnjobVO;
import nc.vo.org.DeptVO;
import nc.vo.org.JobVO;
import nc.vo.org.OrgVO;
import nc.vo.org.PostVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFLiteralDate;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;

/**
 * 
 * @author zls
 * 
 */
@SuppressWarnings({ "restriction" })
public class NCCServiceEditPsnjob extends BaseServlet {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings({ "unchecked" })
	public void doAction(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		Logger.init("zhongzhou");
		Map<String, Object> result = new HashMap<String, Object>();
		resp.setCharacterEncoding("UTF-8");
		PrintWriter out = resp.getWriter();
		String deptCode = null;
		String psncode = null;
		String param = RequestUtil.readAsChars(req);
		try {
			Logger.error("�޸���Ա��ְ��¼ͬ��NCC������" + param);
			System.out.println("�޸���Ա��ְ��¼ͬ��NCC������" + param);
			ISecurityTokenCallback securityTokenCallback = NCLocator.getInstance().lookup(ISecurityTokenCallback.class);
			securityTokenCallback.token("NCSystem".getBytes(), "pfxx".getBytes());
			String pk_group = ParamUtils.getParameter("ncc.pk_group");
			String datasource = ParamUtils.getParameter("ncc.datasource");

			ItfPsnjobVO itfPsnjobVO = JSONObject.parseObject(param).toJavaObject(ItfPsnjobVO.class);

			String orgCode = itfPsnjobVO.getPk_org();
			if (StringUtils.isEmpty(orgCode) || "null".equals(orgCode)) {
				Logger.error("�޸���Ա��ְ��¼ͬ��NCC����pk_org����Ϊ��");
				RequestUtil.response(out, Result.error("����pk_org����Ϊ��", param));
				return;
			}
			InvocationInfoProxy.getInstance().setGroupId(pk_group);
			InvocationInfoProxy.getInstance().setUserDataSource(datasource);
			IUAPQueryBS qry = NCLocator.getInstance().lookup(IUAPQueryBS.class);

			psncode = itfPsnjobVO.getCode();
			if (StringUtils.isEmpty(psncode) || "null".equals(psncode)) {
				Logger.error("�޸���Ա��ְ��¼ͬ��NCC����code����Ϊ��");

				RequestUtil.response(out, Result.error("����code����Ϊ��", param));
				return;
			}
			result.put("code", psncode);

			deptCode = itfPsnjobVO.getPk_dept();
			if (StringUtils.isEmpty(deptCode) || "null".equals(deptCode)) {
				Logger.error("�޸���Ա��ְ��¼ͬ��NCC����pk_dept����Ϊ��");
				RequestUtil.response(out, Result.error("����pk_dept����Ϊ��", param));
				return;
			}
			List<OrgVO> orgVOs = (List<OrgVO>) qry.retrieveByClause(OrgVO.class, " enablestate = 2 and isbusinessunit = 'Y' and pk_group = '" + pk_group + "' and code='" + orgCode
					+ "'  ");

			if (orgVOs == null || orgVOs.size() < 1) {
				Logger.error("NCC�����ڱ���Ϊ" + orgCode + "��ҵ��Ԫ");
				RequestUtil.response(out, Result.error("NCC�����ڱ���Ϊ" + orgCode + "��ҵ��Ԫ", param));
				return;
			}

			OrgVO orgVO = orgVOs.get(0);

			List<DeptVO> deptVOs = (List<DeptVO>) qry.retrieveByClause(DeptVO.class, " 11 = 11 and enablestate = 2 and pk_org = '" + orgVO.getPk_org() + "' and pk_group = '"
					+ pk_group + "' and code='" + deptCode + "'  ");
			if (deptVOs == null || deptVOs.size() < 1) {
				Logger.error("ҵ��Ԫ" + orgVO.getName() + "�����ڱ���Ϊ" + orgCode + "�Ĳ���");
				RequestUtil.response(out, Result.error("ҵ��Ԫ" + orgVO.getName() + "�����ڱ���Ϊ" + orgCode + "�Ĳ���", param));
				return;
			}

			IPsndocQueryService psndocQueryService = NCLocator.getInstance().lookup(IPsndocQueryService.class);
			PsndocVO[] existsPsnDocs = null;
			existsPsnDocs = psndocQueryService.queryPsndocVOsByCondition(" enablestate = 2 and pk_group = '" + pk_group + "' and code='" + psncode + "' and nvl(dr,0)=0 ");

			if (existsPsnDocs == null || existsPsnDocs.length < 1) {
				Logger.error("������Ա��ְ��¼ʧ�ܣ�ҵ��Ԫ" + orgVO.getName() + "�����ڱ���Ϊ" + psncode + "����Ա");
				RequestUtil.response(out, Result.error("������Ա��ְ��¼ʧ�ܣ�ҵ��Ԫ" + orgVO.getName() + "�����ڱ���Ϊ" + psncode + "����Ա", param));
				return;
			}

			DeptVO deptVO = deptVOs.get(0);
			if (deptVOs == null || deptVOs.size() < 1) {
				Logger.error("ҵ��Ԫ" + orgVO.getName() + "�����ڱ���Ϊ" + orgCode + "�Ĳ���");
				RequestUtil.response(out, Result.error("ҵ��Ԫ" + orgVO.getName() + "�����ڱ���Ϊ" + orgCode + "�Ĳ���", param));
				return;
			}
			String ismainjob = itfPsnjobVO.getIsmainjob();
			if (StringUtils.isEmpty(ismainjob) || "null".equals(ismainjob)) {
				Logger.error("������ְ��¼ͬ��NCC����ismainjob����Ϊ��");
				RequestUtil.response(out, Result.error("����ismainjob����Ϊ��", param));
				return;
			}

			String psnjobFlag = orgVO.getPk_org() + deptVO.getPk_dept();

			PsndocVO psndocVO = existsPsnDocs[0];
			PsnjobVO[] psnjobs = psndocVO.getPsnjobs();
			List<PsnjobVO> psnjobList = new ArrayList<PsnjobVO>();
			UFLiteralDate indutyDate = null;
			String indutydate = itfPsnjobVO.getIndutydate();
			if (StringUtils.isEmpty(indutydate) || "null".equals(indutydate)) {
				indutyDate = new UFLiteralDate();
			} else {
				indutyDate = new UFLiteralDate(indutydate);
			}

			IPsndocService psndocService = NCLocator.getInstance().lookup(IPsndocService.class);
			boolean exists = false;
			for (PsnjobVO item : psnjobs) {

				if (psnjobFlag.equals(item.getPk_org() + item.getPk_dept()) && (item.getEnddutydate() == null || item.getEnddutydate().compareTo(indutyDate) > 0)) {
					exists = true;
				}

			}

			String psnclCode = itfPsnjobVO.getPk_psncl();
			if (StringUtils.isEmpty(psnclCode) || "null".equals(psnclCode)) {
				Logger.error("����pk_psncl����Ϊ��");
				RequestUtil.response(out, Result.error("����pk_psncl����Ϊ��", param));
				return;
			}
			String pk_psncl = (String) qry.executeQuery("select pk_psncl from bd_psncl where pk_group='" + pk_group + "' and code ='" + psnclCode
					+ "' and nvl(dr,0)=0 and enablestate=2", new ColumnProcessor());
			if (StringUtils.isEmpty(pk_psncl)) {
				Logger.error("NCC�����ڱ���Ϊ" + psnclCode + "����Ա���");
				RequestUtil.response(out, Result.error("NCC�����ڱ���Ϊ" + psnclCode + "����Ա���", param));
				return;
			}
			PsnjobVO psnjobVO = new PsnjobVO();
			psnjobVO.setPk_group(psndocVO.getPk_group());
			psnjobVO.setPk_org(orgVO.getPk_org());
			psnjobVO.setPsncode(psncode);
			psnjobVO.setPk_dept(deptVO.getPk_dept());
			psnjobVO.setPk_psncl(pk_psncl);
			psnjobVO.setIndutydate(indutyDate);

			// ��λ
			if (StringUtils.isNotEmpty(itfPsnjobVO.getPk_post()) && !"null".equals(itfPsnjobVO.getPk_post())) {
				IPostQueryService postQueryService = NCLocator.getInstance().lookup(IPostQueryService.class);
				PostVO[] postVOs = postQueryService.queryByOrgIDAndByDeptAndClause(psnjobVO.getPk_org(), psnjobVO.getPk_dept(), " postcode ='" + itfPsnjobVO.getPk_post() + "'");
				if (postVOs != null && postVOs.length > 0) {
					psnjobVO.setPk_post(postVOs[0].getPk_post());
				}
			}
			// ְ��
			if (StringUtils.isNotEmpty(itfPsnjobVO.getPk_job()) && !"null".equals(itfPsnjobVO.getPk_job())) {
				IJobQryService jobQryService = NCLocator.getInstance().lookup(IJobQryService.class);
				JobVO[] jobVOs = jobQryService.queryJobVOSByClause(" jobcode ='" + itfPsnjobVO.getPk_job() + "'");
				if (jobVOs != null && jobVOs.length > 0) {
					psnjobVO.setPk_job(jobVOs[0].getPk_job());
					psnjobVO.setJobname(jobVOs[0].getJobname());
				}
			}

			PsndocVO newPsndocVO = null;
			if (!exists) {
				psnjobVO.setStatus(2);
				if ("Y".equals(ismainjob)) {
					psnjobVO.setIsmainjob(UFBoolean.TRUE);
					setMainJobEndduty(psndocVO, indutyDate);
					Collections.addAll(psnjobList, psnjobs);
					psnjobList.add(psnjobVO);
					psndocVO.setPsnjobs(psnjobList.toArray(new PsnjobVO[0]));
					psndocVO.setPk_org(orgVO.getPk_org());
					newPsndocVO = psndocService.transferPsndoc(psndocVO);
				} else {
					psnjobVO.setIsmainjob(UFBoolean.FALSE);
					Collections.addAll(psnjobList, psnjobs);
					psnjobList.add(psnjobVO);
					psndocVO.setPsnjobs(psnjobList.toArray(new PsnjobVO[0]));
					newPsndocVO = psndocService.updatePsndoc(psndocVO, true);
				}
			} else {
				setMainJobEndduty(psndocVO, indutyDate);
				psnjobVO.setStatus(1);
				psnjobVO.setIsmainjob(UFBoolean.TRUE);
				Collections.addAll(psnjobList, psnjobs);
				psnjobList.add(psnjobVO);
				psndocVO.setPsnjobs(psnjobList.toArray(new PsnjobVO[0]));
				psndocVO.setPk_org(orgVO.getPk_org());
				newPsndocVO = psndocService.transferPsndoc(psndocVO);
			}
			if (newPsndocVO != null) {
				Logger.error("�޸���Ա��ְ��¼�ɹ�:" + param);
				RequestUtil.response(out, Result.ok("�޸���Ա��ְ��¼�ɹ�"));
				return;
			} else {
				Logger.error("�޸���Ա��ְ��¼ʧ��:" + param);
				RequestUtil.response(out, Result.error("�޸���Ա��ְ��¼ʧ��", param));
				return;
			}

		} catch (BusinessException e) {
			e.printStackTrace();
			Logger.error("�޸���Ա��ְ��¼ͬ��NCCʧ�ܣ�" + e.getMessage());

			RequestUtil.response(out, Result.error("�޸���Ա��ְ��¼ͬ��NCCʧ�ܣ�" + e.getMessage(), param));
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

	private void setMainJobEndduty(PsndocVO psndocVO, UFLiteralDate indutyDate) {
		PsnjobVO[] psnjobs = psndocVO.getPsnjobs();
		for (PsnjobVO psnjobVO : psnjobs) {

			if (psnjobVO.getIsmainjob() != null && psnjobVO.getIsmainjob().booleanValue()
					&& (psnjobVO.getEnddutydate() == null || psnjobVO.getEnddutydate().compareTo(new UFLiteralDate()) > 0)) {
				psnjobVO.setEnddutydate(indutyDate.getDateBefore(1));
				psnjobVO.setStatus(1);
			}
			System.out.println("��ְ��" + psnjobVO.getIsmainjob() + " ��ְ��ʼ����:" + psnjobVO.getIndutydate() + "   �������ڣ�" + psnjobVO.getEnddutydate() + " VOStatus��"
					+ psnjobVO.getStatus());
		}
	}
}
