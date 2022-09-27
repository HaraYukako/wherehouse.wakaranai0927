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

import com.example.goods.web.dto.GoodsCode;
import com.example.inventory.business.domain.Stock;
import com.example.inventory.business.exception.NoStockException;
import com.example.inventory.business.exception.StockDeletedException;
import com.example.inventory.business.exception.StockNotEmptyException;
import com.example.inventory.business.service.InventoryService;

import lombok.extern.log4j.Log4j2;

@Controller
@RequestMapping("/inventory/stock_delete")
@SessionAttributes({ "goodsCode", "stock" })
@Log4j2
public class StockDeleteController {

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
	 * @return 在庫商品削除画面（入力）に遷移
	 */
	@GetMapping("/input")
	public String input(GoodsCode goodsCode) {
		return "inventory/stock_delete_input";
	}

	/**入力された商品コードがサーバー側が想定しているものか確認する処理
	 * @param goodsCode 商品コード
	 * @param errors エラーメッセージを格納
	 * @param model リダイレクト先に渡したいパラメータを指定
	 * @return 商品コードがサーバー側が想定しているものでない場合、在庫商品削除画面(入力)に再遷移
	 *          商品コードがエラーでない場合、在庫商品削除画面(確認)に遷移
	 * @throws NoStockException 在庫テーブルに在庫が存在しない例外
	 * @throws StockDeletedException 在庫商品が既に削除されている例外
	 * @throws StockNotEmptyException 削除したい在庫商品の在庫が存在している例外
	 */
	@PostMapping("/confirm")
	public String confirm(@Validated GoodsCode goodsCode, Errors errors, Model model)
			throws NoStockException, StockDeletedException, StockNotEmptyException {
		//商品コードがサーバー側が想定しているものでない場合、在庫商品削除画面(入力)に再遷移
		if (errors.hasErrors()) {
			log.warn(errors);
			return "inventory/stock_delete_input";
		}
		//商品コードがエラーでない場合、在庫商品削除画面(確認)に遷移
		inventoryService.canDeleteStock(goodsCode.getCode());
		Stock stock = inventoryService.findStock(goodsCode.getCode());
		model.addAttribute("stock", stock);
		log.info(stock);
		return "inventory/stock_delete_confirm";
	}

	/**在庫商品の論理削除処理
	 * @param goodsCode 商品コード
	 * @return リダイレクト遷移先はGetMappingのcompleteメソッド
	 *  @throws NoStockException 在庫テーブルに在庫が存在しない例外
	 * @throws StockDeletedException 在庫商品が既に削除されている例外
	 * @throws StockNotEmptyException 削除したい在庫商品の在庫が存在している例外
	 */
	@PostMapping("/complete")
	public String complete(@Validated GoodsCode goodsCode)
			throws NoStockException, StockDeletedException, StockNotEmptyException {
		inventoryService.deleteStock(goodsCode.getCode());
		log.info(goodsCode);
		return "redirect:complete";
	}

	/**在庫商品削除画面(完了)に遷移
	 * @return  在庫商品削除画面(完了)に遷移
	 */
	@GetMapping("/complete")
	public String complete() {
		return "inventory/stock_delete_complete";
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

	/**削除済み(Stock)コード例外のエラー文字表示処理
	 * @param model リダイレクト先に渡したいパラメータを指定
	 * @param e エラー処理
	 * @return リダイレクト遷移先はGetMappingのinputメソッド
	 */
	@ExceptionHandler(StockDeletedException.class)
	public String handleStockDeletedException(
			RedirectAttributes model, StockDeletedException e) {
		model.addFlashAttribute("errorCode",
				"error.goods.code.stockDelete");
		log.warn(model, e);
		return "redirect:input";
	}

	/**商品の在庫が存在している例外のエラー文字表示処理
	 * @param model リダイレクト先に渡したいパラメータを指定
	 * @param e エラー処理
	 * @return リダイレクト遷移先はGetMappingのinputメソッド
	 */
	@ExceptionHandler(StockNotEmptyException.class)
	public String handleStockNotEmptyException(
			RedirectAttributes model, StockNotEmptyException e) {
		model.addFlashAttribute("errorCode",
				"error.stock.quantity");
		log.warn(model, e);
		return "redirect:input";
	}
}
