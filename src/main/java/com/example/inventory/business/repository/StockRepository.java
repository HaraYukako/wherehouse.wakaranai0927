package com.example.inventory.business.repository;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.example.inventory.business.domain.Stock;

@Mapper
public interface StockRepository {

	// @Insert()
	/**商品ドメインで商品を追加。
	 * @param stock 商品ドメイン
	 */
	@Insert("insert into STOCK(GOODS_CODE, QUANTITY, STATUS) values(#{goodsCode}, #{quantity}, 'ACTIVE')")
	void createStock(Stock stock);

	// @Select()
	/**
	 * @param goodsCode 商品コード
	 * @return 対象商品コードが論理削除済みである場合、（true）/削除済みでない、または存在しない場合（false）
	 */
	@Select("select count(*) = 1 from STOCK where GOODS_CODE = #{goodsCode} and STATUS = 'DEACTIVE'")
	boolean isStockDeactive(@Param("goodsCode") int goodsCode);

	// @Select()
	/**
	 * 商品コードで商品を検索
	 *
	 * 0件の場合は、nullを返却する。
	 *
	 * @param goodsCode 商品コード
	 * @return 検索結果のレコード情報を保持するGoodsオブジェクト
	 */

	@Select("select GOODS_CODE, QUANTITY from STOCK where GOODS_CODE = #{goodsCode} and STATUS = 'ACTIVE'")
	//該当する商品コードがない場合、nullを返却する
	Stock findStock(@Param("goodsCode") int goodsCode);

	/**
	 * 在庫商品の全量を検索。
	 * <br>
	 * 0件の場合は、nullを返却する。
	 * <br>
	 * @return GoodsオブジェクトのList
	 */
	@Select("select GOODS_CODE, QUANTITY from STOCK where STATUS = 'ACTIVE'")
	List<Stock> findAllStock();

	/**商品を論理削除。
	 * @param goodsCode 商品コード
	 * @return 更新できた場合(1)/更新できない場合(0)
	 */
	@Update("update STOCK set STATUS = 'DEACTIVE' where GOODS_CODE = #{goodsCode} and STATUS = 'ACTIVE'")
	int deleteStock(@Param("goodsCode") int goodsCode);

	// @Update()
	/**商品の更新。
	 * @param stock 在庫情報。
	 * @return 更新できた場合(1)/更新できない場合(0)
	 */
	@Update("update STOCK set QUANTITY = #{quantity} where GOODS_CODE = #{goodsCode} and STATUS = 'ACTIVE'")
	int updateStock(Stock stock);
}
