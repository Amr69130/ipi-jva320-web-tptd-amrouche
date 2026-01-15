package com.ipi.jva320.controller;

import com.ipi.jva320.model.SalarieAideADomicile;
import com.ipi.jva320.service.SalarieAideADomicileService;
import com.ipi.jva320.exception.SalarieException;
import jakarta.persistence.EntityExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SalarieController {

    @Autowired
    private SalarieAideADomicileService salarieAideADomicileService;

    @GetMapping("/salaries/{id}")
    public String getDetailSalarie(@PathVariable("id") Long id, ModelMap model) {
        SalarieAideADomicile salarie = salarieAideADomicileService.getSalarie(id);
        if (salarie == null) {
            return "redirect:/salaries";
        }
        model.put("salarie", salarie);
        return "detail_Salarie";
    }

    @GetMapping("/salaries/aide/new")
    public String newSalarie(ModelMap model) {
        model.put("salarie", new SalarieAideADomicile());
        return "new_Salarie";
    }

    @PostMapping("/salaries/aide/save")
    public String addSalarie(@ModelAttribute SalarieAideADomicile salarie)
            throws SalarieException, EntityExistsException {

        salarieAideADomicileService.creerSalarieAideADomicile(salarie);
        return "redirect:/salaries/" + salarie.getId();
    }

    @GetMapping("/salaries")
    public String listSalaries(ModelMap model) {
        model.put("salaries", salarieAideADomicileService.getSalaries());
        return "list";
    }

//    Methode qui gère la suppresion d'un salarié , on RETURN une redirection sur la list des salariés pour constater l'absence du salarié fraichement suprimé,
//    on privilégie la redirection au simple "return list" pour eviter les problème de cache qui nous montrerai encore la présence du salarié fraichement supprimé
@GetMapping("/salaries/{id}/delete")
public String deleteSalarie(@PathVariable Long id)
        throws EntityExistsException, SalarieException {

    salarieAideADomicileService.deleteSalarieAideADomicile(id);
    return "redirect:/salaries";
}

//  Methode qui gère la modification d'un salarié, on RETURN sur le detail de ce salarié pour constater les modif
//  (pour tres bien s'en apercevoir il suffit de modifier un des champs via le formulaire et une fois appuyé sur enregistrer
//  alors le champ n'est plus en surbrillance mais en affichage pris en compte => à ce moment là nous ne sommes donc plus dans la "page modif" mais la "page détails d'un salarié"
    @PostMapping("/salaries/{id}")
    public String updateSalarie(@PathVariable(value = "id") Long id, final ModelMap model,
                                SalarieAideADomicile updatedSalarie) throws EntityExistsException, SalarieException {
        salarieAideADomicileService.updateSalarieAideADomicile(updatedSalarie);
        model.put("salaries", salarieAideADomicileService.getSalaries());
        return "redirect:/salaries/" + updatedSalarie.getId();
    }
}
