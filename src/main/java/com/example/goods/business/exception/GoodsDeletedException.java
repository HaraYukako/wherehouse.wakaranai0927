package com.example.goods.business.exception;

/** 商品論理削除済例外クラス */
public class GoodsDeletedException extends Exception {
	private static final long serialVersionUID = 1L;

	/** デフォルトコンストラクタ */
	public GoodsDeletedException() {
		super();
	}

	/** コンストラクタ
	 * @param goodsCode 商品コード
	*/
	public GoodsDeletedException(int goodsCode) {
		super(new Integer(goodsCode).toString());
	}
}
