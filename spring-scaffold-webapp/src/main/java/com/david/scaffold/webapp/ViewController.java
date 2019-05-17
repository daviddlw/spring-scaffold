package com.david.scaffold.webapp;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("view")
public class ViewController {

    @RequestMapping("/helloWorld")
    public ModelAndView helloWorld() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("helloWorld");
        return mv;
    }
}
