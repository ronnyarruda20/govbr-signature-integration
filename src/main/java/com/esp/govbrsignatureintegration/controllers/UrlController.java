package com.esp.govbrsignatureintegration.controllers;

import com.esp.govbrsignatureintegration.utils.Util;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UrlController {

    @Value("${govbr.servidorOauth}")
    private String servidorOauth;

    @Value("${govbr.redirectUri}")
    private String redirectUri;

    @Value("${govbr.clientId}")
    private String clientId;

    @Value("${govbr.secret}")
    private String secret;

    @GetMapping("/login")
    public String index(@RequestParam(name = "code", required = false) String code) {
        if (code == null) {
            return "<ul>\n" +
                    "<li><a href=\"" + Util.getUrlGovbr(servidorOauth, redirectUri, "sign", clientId) + "\">Atutenticar Gob.br</a></li>\n" +
                    "<li><a href=\"" + Util.getUrlGovbr(servidorOauth, redirectUri, "signature_session", clientId) + "\">Atutenticar Gob.br in Lote</a></li>\n" +
                    "</ul>";
        }

        return code;
    }
}
