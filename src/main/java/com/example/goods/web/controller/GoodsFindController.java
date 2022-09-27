package com.example.goods.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.goods.business.domain.Goods;
import com.example.goods.business.exception.NoGoodsException;
import com.example.goods.business.service.GoodsService;
import com.example.goods.web.dto.GoodsCode;

import lombok.extern.log4j.Log4j2;

@Controller
@RequestMapping("/goods/find")
@Log4j2
public class GoodsFindController {

	@Autowired
	GoodsService goodsService;

	@GetMapping("/input")
	public String input(GoodsCode goodsCode) {
		return "goods/goods_find_input";
	}

	@PostMapping("/complete")
	public String complete(
			@Validated GoodsCode goodsCode, Errors errors) {
		if (errors.hasErrors()) {
			log.warn(errors);
			return "goods/goods_find_input";
		}
		log.info(goodsCode);
		return "redirect:" + goodsCode.getCode();
	}

	@GetMapping("/{goodsCode}")
	public String show(@PathVariable("goodsCode") int goodsCode, SessionStatus status, Model model)
			throws NoGoodsException {
		Goods goods = goodsService.findGoods(goodsCode);
		model.addAttribute("goods", goods);
		return "goods/goods_find_complete";
	}

	@ExceptionHandler(NoGoodsException.class)
	public String handleNoGoodsException(
			RedirectAttributes model, NoGoodsException e) {
		model.addFlashAttribute("errorCode",
				"error.goods.no.data");
		log.warn(model, e);
		return "redirect:input";
	}
}
