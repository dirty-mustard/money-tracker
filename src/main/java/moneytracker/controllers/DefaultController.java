package moneytracker.controllers;

import moneytracker.security.SecurityContext;
import moneytracker.facades.ImportFacade;
import moneytracker.model.Bank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.Part;
import java.io.IOException;

@Controller
public class DefaultController {

    @Autowired
    private SecurityContext securityContext;

    @Autowired
    private ImportFacade importFacade;

    @RequestMapping(value = "/import", method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("user", securityContext.getAuthenticatedUser());
        return "index";
    }

    @RequestMapping(value = "/import", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void handleImport(@ModelAttribute("bank") Bank bank, @RequestParam("file") Part file) throws IOException {
        importFacade.importCsv(securityContext.getAuthenticatedUser(), bank, file.getInputStream());
    }

}
