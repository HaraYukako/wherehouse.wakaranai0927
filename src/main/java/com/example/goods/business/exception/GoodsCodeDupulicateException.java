package com.example.goods.business.exception;

/** 商品コード重複例外クラス */
public class GoodsCodeDupulicateException extends Exception {
	private static final long serialVersionUID = 1L;

	/** デフォルトコンストラクタ */
	public GoodsCodeDupulicateException() {
		super();
	}

	/** コンストラクタ
	 * @param goodsCode 商品コード
	*/
	public GoodsCodeDupulicateException(int goodsCode) {
		super(new Integer(goodsCode).toString());
	}
}
