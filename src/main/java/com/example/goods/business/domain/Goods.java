package com.example.goods.business.domain;

import static com.example.common.constant.RuleConstant.*;

import java.io.Serializable;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

/**
 * 商品ドメイン
 */
@Data
public class Goods implements Serializable {

	/** 商品コード */
	@NotNull
	@Min(MIN_GOODS_CODE)
	@Max(MAX_GOODS_CODE)
	private Integer code;

	/** 商品名 */
	@Length(min = MIN_STRING, max = MAX_STRING)
	private String name;

	/** 価格 */
	@NotNull
	@Min(MIN_PRICE)
	@Max(MAX_PRICE)
	private Integer price;

	/** デフォルトコンストラクタ */
	public Goods() {
	}

	/** コンストラクタ
	 * @param code 商品コード
	 * @param name 商品名
	 * @param price 価格
	*/
	public Goods(Integer code, String name, Integer price) {
		this.code = code;
		this.name = name;
		this.price = price;
	}
}
