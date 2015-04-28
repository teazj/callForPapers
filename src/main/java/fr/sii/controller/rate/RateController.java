package fr.sii.controller.rate;

import fr.sii.domain.rate.Rate;
import fr.sii.service.rate.RateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by tmaugin on 24/04/2015.
 */
@Controller
@RequestMapping(value="/rate")
public class RateController {

    @Autowired
    RateService rateService;

    @RequestMapping(method= RequestMethod.GET)
    @ResponseBody
    public List<Rate> getRates() {
        return rateService.findAll();
    }

    @RequestMapping(method= RequestMethod.DELETE)
    @ResponseBody
    public void deleteRates() {
        rateService.deleteAll();
    }

    @RequestMapping(method=RequestMethod.POST)
    @ResponseBody public Rate postRate(@Valid @RequestBody Rate rate){
        return rateService.save(rate);
    }

    @RequestMapping(value="/{id}", method=RequestMethod.PUT)
    @ResponseBody public Rate putRate(@PathVariable Long id, @Valid @RequestBody Rate rate){
        return rateService.put(id, rate);
    }

    @RequestMapping(value="/{id}", method= RequestMethod.GET)
    @ResponseBody
    public Rate getRate(@PathVariable Long id) {
        return rateService.findOne(id);
    }

    @RequestMapping(value="/user/{id}", method= RequestMethod.GET)
    @ResponseBody
    public List<Rate> getRateByUserId(@PathVariable Long id) {
        return rateService.findByUserId(id);
    }

    @RequestMapping(value="/{id}", method= RequestMethod.DELETE)
    @ResponseBody
    public void deleteRate(@PathVariable Long id) {
        rateService.delete(id);
    }
}
