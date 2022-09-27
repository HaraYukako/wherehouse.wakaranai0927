package com.example.goods.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.goods.business.domain.Goods;
import com.example.goods.business.exception.NoGoodsException;
import com.example.goods.business.service.GoodsService;

import lombok.extern.log4j.Log4j2;

@Controller
@RequestMapping("/goods")
@Log4j2
public class GoodsListController {

	@Autowired
	GoodsService goodsService;

	@GetMapping("/list")
	public String list(Model model) throws NoGoodsException {
		List<Goods> goodsList = goodsService.findAllGoods();
		model.addAttribute("goodsList", goodsList);
		log.info(goodsList);
		return "goods/goods_list";
	}

	@ExceptionHandler(NoGoodsException.class)
	public String handleNoGoodsException(Model model, NoGoodsException e) {
		model.addAttribute("errorCode",
				"error.goods.no.data");
		log.warn(model, e);
		return "goods/goods_list";
	}
}
