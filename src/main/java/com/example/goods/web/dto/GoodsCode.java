package com.example.goods.web.dto;

import static com.example.common.constant.RuleConstant.*;

import java.io.Serializable;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Data;

/** 商品コードDTO */
@Data
public class GoodsCode implements Serializable {

	/** 商品コード */
	@NotNull
	@Min(MIN_GOODS_CODE)
	@Max(MAX_GOODS_CODE)
	private Integer code;

	/**デフォルトコンストラクタ*/
	public GoodsCode() {
	}

	/**コンストラクタ
	 * @param code 商品コード
	 */
	public GoodsCode(Integer code) {
		this.code = code;
	}
}
