package com.example.inventory.business.domain;

import java.io.Serializable;

import lombok.Data;

/**
 * 在庫商品管理クラス
 */
@Data
public class Stock implements Serializable {

	/**goodsCode 商品コード
	 */
	private Integer goodsCode;

	/**quantity 量
	 */
	private Integer quantity;

	/**デフォルトコンストラクタ
	 */
	public Stock() {
	}

	/** コンストラクタ
	 * @param goodscode 商品コード
	 * @param quantity 量
	 */
	public Stock(Integer goodscode, Integer quantity) {
		// 引数の値をフィールドに設定
		this.goodsCode = goodscode;
		this.quantity = quantity;
	}

}
