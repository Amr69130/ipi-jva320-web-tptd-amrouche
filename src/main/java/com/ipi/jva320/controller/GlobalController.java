package com.ipi.jva320.controller;

import com.ipi.jva320.service.SalarieAideADomicileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * LE GlobalController va injecter les attributs communs à toutes les pages Thymeleaf

 */
@ControllerAdvice
public class GlobalController {

    @Autowired
    private SalarieAideADomicileService salarieAideADomicileService;

    /**
     * Ainsi on fournit le nombre total de salariés pour TOUTES les templates Thymeleaf
     * Accessible via ${nbSalaries} dans navbar, h1 ou n'importe où
     */
    @ModelAttribute("nbSalaries")
    public long nbSalaries() {
        return salarieAideADomicileService.countSalaries();
    }
}
