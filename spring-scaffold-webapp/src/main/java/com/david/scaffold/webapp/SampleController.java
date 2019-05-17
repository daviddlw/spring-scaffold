package com.david.scaffold.webapp;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/sample")
public class SampleController {

    @RequestMapping("/pageOffice")
    public String pageOffice() {
        return "pageOffice";
    }
}
