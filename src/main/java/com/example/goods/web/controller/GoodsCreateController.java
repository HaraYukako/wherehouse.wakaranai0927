package com.example.goods.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.goods.business.domain.Goods;
import com.example.goods.business.exception.GoodsCodeDupulicateException;
import com.example.goods.business.exception.GoodsDeletedException;
import com.example.goods.business.service.GoodsService;

import lombok.extern.log4j.Log4j2;

@Controller
@RequestMapping("/goods/create")
@SessionAttributes("goods")
@Log4j2
public class GoodsCreateController {

	@Autowired
	GoodsService goodsService;

	@PostMapping("/input")
	public String input(SessionStatus status) {
		return "redirect:input";
	}

	@GetMapping("/input")
	public String input(Goods goods) {
		return "goods/goods_create_input";
	}

	@PostMapping("/confirm")
	public String confirm(@Validated Goods goods, Errors errors)
			throws GoodsCodeDupulicateException, GoodsDeletedException {
		if (errors.hasErrors()) {
			log.warn(errors);
			return "goods/goods_create_input";
		}
		goodsService.canCreateGoods(goods.getCode());
		log.info(goods);
		return "goods/goods_create_confirm";
	}

	@PostMapping("/complete")
	public String complete(@Validated Goods goods)
			throws GoodsCodeDupulicateException, GoodsDeletedException {
		goodsService.createGoods(goods);
		log.info(goods);
		return "redirect:complete";
	}

	@GetMapping("/complete")
	public String complete( ) {
		return "goods/goods_create_complete";
	}

	@ExceptionHandler(GoodsCodeDupulicateException.class)
	public String handleGoodsCodeDupulicateException(
			RedirectAttributes model, GoodsCodeDupulicateException e) {
		model.addFlashAttribute("errorCode",
				"error.goods.code.dupulicate");
		log.warn(model, e);
		return "redirect:input";
	}

	@ExceptionHandler(GoodsDeletedException.class)
	public String handleGoodsDeletedException(
			RedirectAttributes model, GoodsDeletedException e) {
		model.addFlashAttribute("errorCode",
				"error.goods.code.delete");
		log.warn(model, e);
		return "redirect:input";
	}
}
