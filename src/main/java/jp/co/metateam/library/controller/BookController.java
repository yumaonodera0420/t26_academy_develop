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

    @GetMapping("/book/index")
    public String index(Model model) {
        // 書籍を全件取得
        List<BookMstDto> bookMstList = this.bookMstService.findAvailableWithStockCount();

        model.addAttribute("bookMstList", bookMstList);

        return "book/index";
    }

    @GetMapping("/book/add") // 登録画面表示
    public String add(Model model) {
        if (!model.containsAttribute("bookMstDto")) {
            model.addAttribute("bookMstDto", new BookMstDto());
        }

        return "book/add";
    }

    @PostMapping("/book/add")
    public String addbook(
            @Valid @ModelAttribute BookMstDto bookMstDto,
            BindingResult result,
            Model model) {

        // バリデーションエラー
        if (result.hasErrors()) {

            if (result.hasFieldErrors("title")) {
                model.addAttribute(
                        "errTitle",
                        result.getFieldError("title").getDefaultMessage());
            }

            if (result.hasFieldErrors("isbn")) {
                model.addAttribute(
                        "errISBN",
                        result.getFieldError("isbn").getDefaultMessage());
            }

            return "book/add";
        }

        // ISBN重複チェック
        if (bookMstService.findByIsbn(bookMstDto.getIsbn()).isPresent()) {

            model.addAttribute(
                    "errISBN",
                    "登録済みのISBNです");

            return "book/add";
        }

        // 保存
        bookMstService.save(bookMstDto);

        return "redirect:/book/index";
    }
}