package com.ipi.jva320.controller;

import com.ipi.jva320.model.SalarieAideADomicile;
import com.ipi.jva320.service.SalarieAideADomicileService;
import com.ipi.jva320.exception.SalarieException;
import jakarta.persistence.EntityExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

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
    public String searchSalarie(@RequestParam(value = "nom", required = false) String nom,
                                final ModelMap model) {

        // Si le paramètre "nom" est présent dans l'URL
        if (nom != null && !nom.isEmpty()) {

            // 1. On appelle le service pour chercher
            List<SalarieAideADomicile> salaries = salarieAideADomicileService.getSalaries(nom);

            // 2. Gestion de l'erreur 404 si la liste est vide
            if (salaries.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Aucun salarié trouvé avec le nom : " + nom);
            }

            // 3. Si on a trouvé, on prend le premier résultat (car la vue détail attend un seul objet 'salarie')
            model.put("salarie", salaries.get(0));

            // 4. On renvoie directement vers la vue de DÉTAIL
            return "detail_Salarie";
        }

        // Si aucun nom n'est fourni, on affiche la liste complète (comportement par défaut)
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
