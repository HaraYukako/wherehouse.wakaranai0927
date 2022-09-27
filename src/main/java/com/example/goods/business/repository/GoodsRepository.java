package com.example.goods.business.repository;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.example.goods.business.domain.Goods;

/**
 * GOODSテーブルアクセス用リポジトリ
 */
@Mapper
public interface GoodsRepository {

	/**
     * 商品ドメインで商品を追加。
     * <br>
     * @param goods 商品ドメイン
     */
	@Insert("insert into GOODS(CODE, NAME, PRICE, STATUS) values(#{code}, #{name}, #{price}, 'ACTIVE')")
	void createGoods(Goods goods);

	/**
     * 商品コードで商品を検索。
     * <br>
     * @param goodsCode 商品コード
     * @return 対象商品コードが論理削除済みである場合(true)/削除済みでない、又は存在しない場合(false)
     */
	@Select("select count(*) = 1 from GOODS where CODE = #{code} and STATUS = 'DEACTIVE'")
	boolean isGoodsDeactive(int goodsCode);

    /**
     * 商品の全量を検索。
     * <br>
     * 0件の場合は、nullを返却する。
     * <br>
     * @return GoodsオブジェクトのList
     */
	@Select("select CODE, NAME, PRICE from GOODS where STATUS = 'ACTIVE'")
	List<Goods> findAllGoods();

    /**
     * 商品コードで商品を検索。
     * <br>
     * 0件の場合は、nullを返却する。
     * <br>
     * @param goodsCode 商品コード
     * @return 検索結果のレコード情報を保持するGoodsオブジェクト
     */
	@Select("select CODE, NAME, PRICE from GOODS where CODE = #{code} and STATUS = 'ACTIVE'")
	Goods findGoods(@Param("code") int goodsCode);

	/**
     * 商品コードで商品を論理削除。
     * <br>
     * @param goodsCode 商品コード
     * @return 更新できた場合(1)/更新できない場合(0)
     */
	@Update("update GOODS set STATUS = 'DEACTIVE' where CODE = #{code} and STATUS = 'ACTIVE'")
	int deleteGoods(@Param("code") int goodsCode);
}
