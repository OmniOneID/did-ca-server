package org.omnione.did.cas.v1.admin.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SpaController implements ErrorController {

    @GetMapping(value = "/error")
    public String error() {
        return "index.html";
    }
    public String getErrorPath() {
        return "error";
    }
}
