package jp.co.metateam.library.model;

import java.sql.Timestamp;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * 書籍マスタDTO
 */
@Getter
@Setter
public class BookMstDto {
    
    private Long id; 

    /**
     * ISBN
     */
    @NotBlank(message="ISBNを入力してください")
    @Pattern(regexp = "\\d+", message = "ISBNは数字のみに入力してください")
    @Size(min = 13, max = 13, message = "ISBNは13桁で入力してください")
    private String isbn;

    /**
     * 書籍名
     */
    @NotBlank(message = "書籍名を入力してください")
    @Size(max = 255, message = "書籍名は255文字以下で入力してください")

    private String title;
    
    private Timestamp deletedAt;

    private BookMst bookMst;
}
