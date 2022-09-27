package com.example.inventory.business.domain;

import static com.example.common.constant.RuleConstant.*;

import java.io.Serializable;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import lombok.Data;

/**
 * 入出荷管理クラス
 */
@Data
public class ReceivingShipmentOrder implements Serializable {

	//private Integer no;
	//private String orderType;

	/** 商品コード */
	@NotNull
	@Min(MIN_GOODS_CODE)
	@Max(MAX_GOODS_CODE)
	private Integer goodsCode;

	/**入荷数
	 *
	 */
	@NotNull
	@Range(min = MIN_QUANTITY, max = MAX_QUANTITY)
	private Integer quantity;

	/**デフォルトコンストラクタ
	 *
	 */
	public ReceivingShipmentOrder() {

	}

	/**コンストラクタ
	 * @param no 入・出荷番号
	 * @param orderType 入荷・出荷
	 * @param goodsCode 商品コード
	 * @param quantity 入荷数
	 */
	public ReceivingShipmentOrder(Integer goodsCode, Integer quantity) {
		this.goodsCode = goodsCode;
		this.quantity = quantity;
	}

}
