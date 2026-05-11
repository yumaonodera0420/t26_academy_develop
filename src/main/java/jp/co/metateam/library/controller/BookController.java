package jp.co.metateam.library.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import jp.co.metateam.library.model.BookMst;
import jp.co.metateam.library.model.BookMstDto;
import jp.co.metateam.library.service.BookMstService;
import lombok.extern.log4j.Log4j2;

/**
 * 書籍関連クラス
 */
@Log4j2
@Controller
public class BookController {
    private final BookMstService bookMstService;

    @Autowired
    public BookController(BookMstService bookMstService) {
        this.bookMstService = bookMstService;
    }

    /**
     * 一覧画面
     */
    @GetMapping("/book/index")
    public String index(Model model) {
        // 書籍を全件取得
        List<BookMstDto> bookMstList = this.bookMstService.findAvailableWithStockCount();

        model.addAttribute("bookMstList", bookMstList);
        return "book/index";
    }

    /**
     * 登録画面表示
     */
    @GetMapping("/book/add")
    public String add(Model model) {
        if (!model.containsAttribute("bookMstDto")) {
            model.addAttribute("bookMstDto", new BookMstDto());
        }
        return "book/add";
    }

    /**
     * 登録処理
     */
    @PostMapping("/book/add")
    public String store(
            @Valid @ModelAttribute BookMstDto bookMstDto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {

        // 入力チェック

        // バリデーションエラー判定
        if (bindingResult.hasErrors()) {

            log.warn("入力チェックエラー");

            model.addAttribute("bookMstDto", bookMstDto);

            return "book/add";
        }

        // ISBN重複チェック

        boolean existsIsbn = this.bookMstService.existsByIsbn(bookMstDto.getIsbn());

        // ISBNがDBに存在するか
        if (existsIsbn) {

            log.warn("ISBN重複エラー");

            bindingResult.rejectValue(
                    "isbn",
                    "duplicate",
                    "ISBNが重複しています");

            return "book/add";
        }

        // DB登録

        this.bookMstService.insert(bookMstDto);

        log.info("書籍登録完了");

        // 完了メッセージ

        redirectAttributes.addFlashAttribute(
                "message",
                "書籍を登録しました");

        // 一覧画面へ推移

        return "redirect:/book/index";
    }
}