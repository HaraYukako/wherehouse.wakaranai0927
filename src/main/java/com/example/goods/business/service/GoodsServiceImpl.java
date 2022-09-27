package com.example.goods.business.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.goods.business.domain.Goods;
import com.example.goods.business.exception.GoodsCodeDupulicateException;
import com.example.goods.business.exception.GoodsDeletedException;
import com.example.goods.business.exception.NoGoodsException;
import com.example.goods.business.repository.GoodsRepository;

/** 商品管理サービスクラス */
@Service
@Transactional(rollbackFor = Exception.class)
public class GoodsServiceImpl implements GoodsService {

	@Autowired
	private GoodsRepository goodsRepository;

	@Override
	public void createGoods(Goods goods) throws GoodsCodeDupulicateException, GoodsDeletedException {
		canCreateGoods(goods.getCode());
		goodsRepository.createGoods(goods);
	}

	@Transactional(readOnly = true)
	@Override
	public void canCreateGoods(int goodsCode) throws GoodsCodeDupulicateException, GoodsDeletedException {
		if (goodsRepository.isGoodsDeactive(goodsCode)) {
			throw new GoodsDeletedException(goodsCode);
		}
		if (goodsRepository.findGoods(goodsCode) != null) {
			throw new GoodsCodeDupulicateException(goodsCode);
		}
	}

	@Transactional(readOnly = true)
	@Override
	public List<Goods> findAllGoods() throws NoGoodsException {
		List<Goods> goodsList = goodsRepository.findAllGoods();
		if (goodsList.isEmpty()) {
			throw new NoGoodsException();
		}
		return goodsList;
	}

	@Transactional(readOnly = true)
	@Override
	public Goods findGoods(int goodsCode) throws NoGoodsException {

		Goods goods = goodsRepository.findGoods(goodsCode);
		if (goods == null) {
			throw new NoGoodsException(goodsCode);
		}
		return goods;
	}

	@Override
	public void deleteGoods(int goodsCode) throws NoGoodsException, GoodsDeletedException {
		canDeleteGoods(goodsCode);
		int count = goodsRepository.deleteGoods(goodsCode);
		if (count == 0) {
			throw new NoGoodsException(goodsCode);
		}
	}

	@Transactional(readOnly = true)
	@Override
	public void canDeleteGoods(int goodsCode) throws NoGoodsException, GoodsDeletedException {
		if (goodsRepository.isGoodsDeactive(goodsCode)) {
			throw new GoodsDeletedException(goodsCode);
		}
		if (goodsRepository.findGoods(goodsCode) == null) {
			throw new NoGoodsException(goodsCode);
		}
	}
}
