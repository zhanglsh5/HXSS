package nc.vo.ic.m45.vochange;

import nc.bs.ic.general.util.GenBsUtil;
import nc.bs.ic.pub.env.ICBSContext;
import nc.vo.ic.m45.deal.Bill45ForPOTransProcess;
import nc.vo.ic.m45.entity.PurchaseInBodyVO;
import nc.vo.ic.m45.entity.PurchaseInVO;
import nc.vo.ic.pub.pf.ICDefaultChangeVOAdjust;
import nc.vo.ic.pub.pf.billtype.BillTypeInfoQuery;
import nc.vo.ic.pub.util.NCBaseTypeUtils;
import nc.vo.ic.pub.util.StringUtil;
import nc.vo.pf.change.ChangeVOAdjustContext;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scmpub.res.billtype.POBillType;

/**
 * �ɹ��������ɹ�����VO����������
 * 
 * @since 6.0
 * @version 2011-6-13 ����05:24:07
 * @author wanghna
 */
public class POTO45ChangeVOAdjust extends ICDefaultChangeVOAdjust {
	// ��ʶ�Ƿ���ն��������˿�
	private boolean isReturn;

	public POTO45ChangeVOAdjust() {
		this.isReturn = false;
	}

	public AggregatedValueObject[] batchAdjustAfterChange(AggregatedValueObject[] srcVOs, AggregatedValueObject[] destVOs, ChangeVOAdjustContext adjustContext)
			throws BusinessException {
		processSourceType(destVOs);
		// ��ʼ��ǰ��ת��������
		Bill45ForPOTransProcess proc = new Bill45ForPOTransProcess();
		balancePurchaseAstNum(srcVOs, destVOs);
		GenBsUtil.initTransBillBaseProcess(proc);
		AggregatedValueObject[] retvos = proc.processBillVOs((PurchaseInVO[]) (PurchaseInVO[]) destVOs);

		return retvos;
	}

	private void balancePurchaseAstNum(AggregatedValueObject[] srcVOs, AggregatedValueObject[] destVOs) {
		balanceNshouldAstNum(srcVOs, destVOs, getSrcNumKey(destVOs), null);
	}

	protected boolean isFirstTransFullNumBill(CircularlyAccessibleValueObject srcbody, CircularlyAccessibleValueObject destbody) {
		return isFirstTransBill(srcbody, (PurchaseInBodyVO) destbody);
	}

	private boolean isFirstTransBill(CircularlyAccessibleValueObject srcbody, PurchaseInBodyVO destbody) {
		String csourcebilltype = new ICBSContext().getBillTypeInfo().getBilltypecodeByPk(destbody.getCsourcetype());

		if (StringUtil.isStringEqual(csourcebilltype, POBillType.Order.getCode())) {
			return isFirstFrom21(srcbody, destbody);
		}
		if (StringUtil.isStringEqual(csourcebilltype, POBillType.Arrive.getCode())) {
			return isFirstFrom23(srcbody, destbody);
		}

		return false;
	}

	private String getSrcNumKey(AggregatedValueObject[] destVOs) {
		String csourcebilltype = new ICBSContext().getBillTypeInfo().getBilltypecodeByPk((String) destVOs[0].getChildrenVO()[0].getAttributeValue("csourcetype"));

		if (StringUtil.isStringEqual(csourcebilltype, POBillType.Order.getCode())) {
			return "nastnum";
		}
		if (StringUtil.isStringEqual(csourcebilltype, POBillType.Arrive.getCode())) {
			return "nastnum";
		}

		return null;
	}

	private boolean isFirstFrom21(CircularlyAccessibleValueObject srcbody, PurchaseInBodyVO destbody) {
		UFDouble ordernum = (UFDouble) srcbody.getAttributeValue("nnum");
		UFDouble shouldnum = (UFDouble) destbody.getAttributeValue("nshouldnum");
		UFDouble caninnum = (UFDouble) srcbody.getAttributeValue("ncaninnum");
		if ((NCBaseTypeUtils.isEquals(ordernum, caninnum)) && (NCBaseTypeUtils.isEquals(shouldnum, ordernum))) {
			return true;
		}
		if (NCBaseTypeUtils.isNegative(shouldnum, ordernum)) {
			this.isReturn = true;
			return true;
		}
		return false;
	}

	private boolean isFirstFrom23(CircularlyAccessibleValueObject srcbody, PurchaseInBodyVO destbody) {
		UFDouble arriveNum = (UFDouble) srcbody.getAttributeValue("nnum");
		UFDouble ncaninNum = (UFDouble) srcbody.getAttributeValue("ncanstorenum");
		UFDouble nshouldnum = (UFDouble) destbody.getAttributeValue("nshouldnum");

		return ((NCBaseTypeUtils.isEquals(arriveNum, ncaninNum)) && (NCBaseTypeUtils.isEquals(nshouldnum, arriveNum)));
	}

	protected void processNshoulAstNum(CircularlyAccessibleValueObject srcbody, CircularlyAccessibleValueObject body, String srcAstNumKey) {
		if (!(this.isReturn)) {
			body.setAttributeValue("nshouldassistnum", srcbody.getAttributeValue(srcAstNumKey));
		} else
			body.setAttributeValue("nshouldassistnum", NCBaseTypeUtils.negUFDouble((UFDouble) srcbody.getAttributeValue(srcAstNumKey)));
	}

	/**
	 * ���Ψһ�����������ڲ��յ��������ʱ��ͬһ���ϰ���������ֳɶ��У�ÿ������Ϊ1������������Դ������������/����������ÿһ�����ϵ����Ψһ��
	 * 
	 * @param retvos
	 */
	private void splitUniqueMaterialNoRows(AggregatedValueObject[] retvos) {
		for (AggregatedValueObject retVO : retvos) {
			CircularlyAccessibleValueObject[] bodys = retVO.getChildrenVO();
			for (CircularlyAccessibleValueObject body : bodys) {

			}
		}
	}

	// �ж������Ƿ��������Ψһ�����
	private boolean uniqueMaterialNoEnabled(String pk_material) {

		return false;

	}
}