package com.example.goods.business.repository;

import java.util.ArrayList;
import java.util.List;

import com.example.goods.business.domain.Goods;

public class GoodsRepositoryMock implements GoodsRepository {

	@Override
	public void createGoods(Goods goods) {
	}

	@Override
	public List<Goods> findAllGoods()  {
		List<Goods> goodsList = new ArrayList<Goods>();
		goodsList.add(new Goods(0, "りんご", 100));
		goodsList.add(new Goods(1, "いちご", 350));
		goodsList.add(new Goods(2, "白菜", 90));
		goodsList.add(new Goods(6, "クレヨン", 1280));
		goodsList.add(new Goods(7, "サインペン", 50));
		return goodsList;
	}

	@Override
	public Goods findGoods(int goodsCode) {
		if(goodsCode == 0) return new Goods(0, "りんご", 100);
		if(goodsCode == 1) return new Goods(1, "いちご", 350);
		if(goodsCode == 777) return null;
		return null;
	}

	@Override
	public int deleteGoods(int goodsCode){
		if(goodsCode == 1) return 1;

		return 0;
	}

	@Override
	public boolean isGoodsDeactive(int goodsCode)  {
		if(goodsCode == 0) return false;
		if(goodsCode == 1) return false;
		if(goodsCode == 3) return true;
		if(goodsCode == 1001) return false;
		 return false;
	}

}
