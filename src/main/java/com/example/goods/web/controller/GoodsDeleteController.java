package com.example.goods.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
import com.example.goods.business.exception.GoodsDeletedException;
import com.example.goods.business.exception.NoGoodsException;
import com.example.goods.business.service.GoodsService;
import com.example.goods.web.dto.GoodsCode;

import lombok.extern.log4j.Log4j2;

@Controller
@RequestMapping("/goods/delete")
@SessionAttributes({"goodsCode", "goods"})
@Log4j2
public class GoodsDeleteController {

	@Autowired
	GoodsService goodsService;

	@PostMapping("/input")
	public String input(SessionStatus status) {
		status.setComplete();
		return "redirect:input";
	}

	@GetMapping("/input")
	public String input(GoodsCode goodsCode) {
		return "goods/goods_delete_input";
	}

	@PostMapping("/confirm")
	public String confirm(@Validated GoodsCode goodsCode, Errors errors,
			Model model)
			throws NoGoodsException, GoodsDeletedException {
		if (errors.hasErrors()) {
			log.warn(errors);
			return "goods/goods_delete_input";
		}
		goodsService.canDeleteGoods(goodsCode.getCode());
		Goods goods = goodsService.findGoods(goodsCode.getCode());
		model.addAttribute("goods", goods);
		log.info(goods);
		return "goods/goods_delete_confirm";
	}

	@PostMapping("/complete")
	public String complete(	@Validated GoodsCode goodsCode)
			throws NoGoodsException, GoodsDeletedException {
		goodsService.deleteGoods(goodsCode.getCode());
		log.info(goodsCode);
		return "redirect:complete";
	}

	@GetMapping("/complete")
	public String complete(	) {
		return "goods/goods_delete_complete";
	}

	@ExceptionHandler(NoGoodsException.class)
	public String handleNoGoodsException(
			RedirectAttributes model, NoGoodsException e) {
		model.addFlashAttribute("errorCode",
				"error.goods.no.data");
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
