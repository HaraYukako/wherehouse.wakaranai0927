package com.example.goods.business.service;

import java.util.List;

import com.example.goods.business.domain.Goods;
import com.example.goods.business.exception.GoodsCodeDupulicateException;
import com.example.goods.business.exception.GoodsDeletedException;
import com.example.goods.business.exception.NoGoodsException;

/** 商品管理サービスインターフェース */
public interface GoodsService {

	/**
     * 商品ドメインで商品を追加。
     * <br>
     * @param goods 商品ドメイン
     * @throws GoodsCodeDupulicateException 商品コード重複
     * @throws GoodsDeletedException 商品論理削除済
     */
	void createGoods(Goods goods) throws GoodsCodeDupulicateException, GoodsDeletedException;

	/**
     * 商品コードで商品を追加可能か検証。
     * <br>
     * @param goodsCode 商品コード
     * @throws GoodsCodeDupulicateException 商品コード重複
     * @throws GoodsDeletedException 商品論理削除済
     */
	void canCreateGoods(int goodsCode) throws GoodsCodeDupulicateException, GoodsDeletedException;

    /**
     * 商品の全量を検索。
     * <br>
     * 0件の場合は、nullを返却する。
     * <br>
     * @return GoodsオブジェクトのList
     * @throws NoGoodsException 存在しない商品
     */
	List<Goods> findAllGoods() throws NoGoodsException;

    /**
     * 商品コードで商品を検索。
     * <br>
     * 0件の場合は、nullを返却する。
     * <br>
     * @param goodsCode 商品コード
     * @return 検索結果のレコード情報を保持するGoodsオブジェクト
     * @throws NoGoodsException 存在しない商品
     */
	Goods findGoods(int goodsCode) throws NoGoodsException;

	/**
     * 商品コードで商品を論理削除。
     * <br>
     * @param goodsCode 商品コード
     * @throws NoGoodsException 存在しない商品
     * @throws GoodsDeletedException 商品論理削除済
     */
	void deleteGoods(int goodsCode) throws NoGoodsException, GoodsDeletedException;

	/**
     * 商品コードで商品を論理削除可能か検証。
     * <br>
     * @param goodsCode 商品コード
     * @throws NoGoodsException 存在しない商品
     * @throws GoodsDeletedException 商品論理削除済
     */
	void canDeleteGoods(int goodsCode) throws NoGoodsException, GoodsDeletedException;
}