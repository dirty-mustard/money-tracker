package moneytracker.controllers;

import moneytracker.model.Bank;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/banks")
public class BanksController {

    @RequestMapping(method = RequestMethod.GET)
    public List<Map<String, String>> banks() {

        List<Map<String, String>> banks = new LinkedList<>();
        for (Bank bank : Bank.values()) {
            banks.add(new HashMap<String, String>() {{
                put("name", bank.name());
                put("description", bank.getDescription());
            }});
        }

        return banks;

    }

}
