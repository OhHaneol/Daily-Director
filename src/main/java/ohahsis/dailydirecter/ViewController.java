package ohahsis.dailydirecter;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller  // @RestController가 아닌 @Controller 사용
public class ViewController {

    @GetMapping("/")
    public String home() {
        return "redirect:/html/note/home.html";  // .html 확장자 제외
    }
}