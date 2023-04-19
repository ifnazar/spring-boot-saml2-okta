package com.poc.saml;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/foo")
public class FooController {
	
    @RequestMapping("/bar")
    public String bar() {
        return "bar";
    }
    
    @RequestMapping("/coffee")
    public String coffee() {
        return "coffee";
    } 
    
    @RequestMapping("/xxx")
    public String xxx() {
        return "xxx";
    }

}