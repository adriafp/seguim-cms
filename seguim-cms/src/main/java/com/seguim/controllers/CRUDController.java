package com.seguim.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class CRUDController {

    @RequestMapping("/crud")
    public String index() {
        return "admin.crudListDomains";
    }

    @RequestMapping("/crud/{domain}")
    public String index(@PathVariable String domain, ModelMap model) {
        model.addAttribute("domainName",domain);
        return "admin.crudShowDomain";
    }

    @RequestMapping("/crud/{domain}/{item}")
    public String index(@PathVariable String domain,@PathVariable String item, ModelMap model) {
        model.addAttribute("domainName",domain);
        model.addAttribute("itemName",item);
        return "admin.crudShowItem";
    }


}