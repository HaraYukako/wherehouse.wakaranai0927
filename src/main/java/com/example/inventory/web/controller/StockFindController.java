package com.example.inventory.web.controller;

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

import com.example.goods.web.dto.GoodsCode;
import com.example.inventory.business.domain.Stock;
import com.example.inventory.business.exception.NoStockException;
import com.example.inventory.business.service.InventoryService;

import lombok.extern.log4j.Log4j2;

@Controller
@RequestMapping("/inventory/stock_find")
@Log4j2
public class StockFindController {

	/**
	 * インスタンスを生成せずにメソッドを呼び出せるようにする
	 */
	@Autowired
	InventoryService inventoryService;

	/**在庫商品検索画面（入力）に遷移
	 * @param goodsCode 商品コード
	 * @return 在庫商品検索画面（入力）に遷移
	 */
	@GetMapping("/input")
	public String input(GoodsCode goodsCode) {
		return "inventory/stock_find_input";
	}

	/**入力された商品コードがサーバー側が想定しているものか確認する処理
	 * @param goods 商品コード
	 * @param errors エラー処理
	 * @return エラーが発生した場合在庫商品検索画面（入力）に遷移
	 *          エラーが発生しなかった場合商品コードが渡される
	 */
	@PostMapping("/complete")
	public String complete(@Validated GoodsCode goods, Errors errors) {
		if (errors.hasErrors()) {
			log.warn(errors);
			return "inventory/stock_find_input";
		}
		log.info(goods);
		return "redirect:" + goods.getCode();
	}

	/**該当する在庫商品情報を獲得、渡す処理
	 * @param goodsCode 商品コード
	 * @param status セッション処理が完了していない場合false、完了している場合true
	 * @param model リダイレクト先に渡したいパラメータを指定
	 * @return 在庫商品検索画面（完了）に遷移
	 * @throws NoStockException 在庫商品が存在しない例外
	 */
	@GetMapping("/{goodsCode}")
	public String show(@PathVariable("goodsCode") int goodsCode, SessionStatus status, Model model)
			throws NoStockException {
		Stock stock = inventoryService.findStock(goodsCode);
		model.addAttribute("stock", stock);
		return "inventory/stock_find_complete";
	}

	/**商品コードの在庫商品が存在しない場合エラーメッセージ表示処理
	 * @param model リダイレクト先に渡したいパラメータを指定
	 * @param e エラー処理
	 * @return リダイレクト遷移先はGetMappingのinputメソッド
	 */
	@ExceptionHandler(NoStockException.class)
	public String handleNoStockException(
			RedirectAttributes model, NoStockException e) {
		model.addFlashAttribute("errorCode",
				"error.stock.no.data");
		log.warn(model, e);
		return "redirect:input";
	}
}
