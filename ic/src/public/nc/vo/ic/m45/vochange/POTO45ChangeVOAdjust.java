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
 * 采购订单，采购到货VO交换处理类
 * 
 * @since 6.0
 * @version 2011-6-13 下午05:24:07
 * @author wanghna
 */
public class POTO45ChangeVOAdjust extends ICDefaultChangeVOAdjust {
	// 标识是否参照订单做的退库
	private boolean isReturn;

	public POTO45ChangeVOAdjust() {
		this.isReturn = false;
	}

	public AggregatedValueObject[] batchAdjustAfterChange(AggregatedValueObject[] srcVOs, AggregatedValueObject[] destVOs, ChangeVOAdjustContext adjustContext)
			throws BusinessException {
		processSourceType(destVOs);
		// 初始化前置转单处理器
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
	 * 入库唯一码管理的物料在参照到货单入库时，同一物料按照数量拆分成多行，每行数量为1，重量等于来源到货单行重量/数量，生成每一行物料的入库唯一码
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

	// 判断物料是否启用入库唯一码管理
	private boolean uniqueMaterialNoEnabled(String pk_material) {

		return false;

	}
}