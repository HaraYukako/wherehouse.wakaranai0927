package com.example.inventory.web.controller;

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

import com.example.inventory.business.domain.ReceivingShipmentOrder;
import com.example.inventory.business.domain.Stock;
import com.example.inventory.business.exception.NoStockException;
import com.example.inventory.business.exception.StockDeletedException;
import com.example.inventory.business.exception.StockOverException;
import com.example.inventory.business.service.InventoryService;

import lombok.extern.log4j.Log4j2;

@Controller
@RequestMapping("/inventory/receiving_order")
@SessionAttributes({ "goodsCode", "stock", "receivingShipmentOrder" })
@Log4j2

public class ReceivingOrderController {

	/**インスタンスを生成しなくてもメソッドを利用できるようにする。
	 *
	 */
	@Autowired
	InventoryService inventoryService;

	/**セッション処理完了の確認
	 * @param status セッション処理
	 * @return "redirect:input" リダイレクト遷移先はGetMappingのinputメソッド
	 */
	@PostMapping("/input")
	public String input(SessionStatus status) {
		status.setComplete();
		return "redirect:input";
	}

	/**商品コードを取得する処理
	 * @param goodsCode 商品コード
	 * @return 入荷画面（入力）に遷移
	 */
	@GetMapping("/input")
	public String input(ReceivingShipmentOrder receivingShipmentOrder) {
		return "inventory/receiving_order_input";
	}

	/**入力された商品コードがサーバー側が想定しているものか確認する処理
	 * @param goodsCode 商品コード
	 * @param errors エラーメッセージを格納
	 * @param model リダイレクト先に渡したいパラメータを指定
	 * @return 商品コードがサーバー側が想定しているものでない場合、入荷画面(入力)に再遷移
	 *          商品コードがエラーでない場合、入荷画面(確認)に遷移
	 * @throws NoStockException 在庫テーブルに在庫が存在しない例外
	 * @throws StockDeletedException 在庫商品が既に削除されている例外
	 * @throws StockOverException 在庫商品数が100を超えた例外
	 */
	@PostMapping("/confirm")
	public String confirm(@Validated ReceivingShipmentOrder receivingShipmentOrder, Errors errors, Model model)
			throws NoStockException, StockDeletedException, StockOverException {
		//商品コードがサーバー側が想定しているものでない場合、入荷画面(入力)に再遷移
		if (errors.hasErrors()) {
			log.warn(errors);
			return "inventory/receiving_order_input";
		}
		//商品コードがエラーでない場合、入荷画面(確認)に遷移
		ReceivingShipmentOrder receivingShipmentOrderInfo = new ReceivingShipmentOrder(
				receivingShipmentOrder.getGoodsCode(), 0);
		Stock stock = inventoryService.findStock(receivingShipmentOrderInfo.getGoodsCode());
		model.addAttribute("stock", stock);
		log.info(stock);
		return "inventory/receiving_order_confirm";
	}

	/**在庫入荷商品の入荷処理
	 * @param goodsCode 商品コード
	 * @return リダイレクト遷移先はGetMappingのcompleteメソッド
	 *  @throws NoStockException 在庫テーブルに在庫が存在しない例外
	 * @throws StockDeletedException 在庫商品が既に削除されている例外
	 * @throws StockOverException
	 */
	@PostMapping("/complete")
	public String complete(@Validated ReceivingShipmentOrder receivingShipmentOrder, Model model)
			throws NoStockException, StockDeletedException, StockOverException {
		inventoryService.receiveStock(receivingShipmentOrder);
		Stock stock = inventoryService.findStock(receivingShipmentOrder.getGoodsCode());
		model.addAttribute("stock", stock);
		log.info(receivingShipmentOrder);
		return "redirect:complete";
	}

	@GetMapping("/complete")
	public String complete() {
		return "inventory/receiving_order_complete";
	}

	/**存在しない在庫商品例外のエラー文字表示
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

	/**商品の在庫が100より多い例外のエラー文字表示処理
	 * @param model リダイレクト先に渡したいパラメータを指定
	 * @param e エラー処理
	 * @return リダイレクト遷移先はGetMappingのinputメソッド
	 */
	@ExceptionHandler(StockOverException.class)
	public String handleStockOverException(
			RedirectAttributes model, StockOverException e) {
		model.addFlashAttribute("errorCode",
				"error.stock.overQuantity");
		log.warn(model, e);
		return "redirect:input";
	}
}
