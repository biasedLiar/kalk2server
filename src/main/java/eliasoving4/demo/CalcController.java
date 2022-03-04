package eliasoving4.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class CalcController {

    @Autowired
    private CalcService service;

    Logger logger = LoggerFactory.getLogger(CalcController.class);


    @GetMapping("/Hello")
    public String hello(){
        logger.debug("Hello");
        System.out.println(" pressed, ");
        return String.format("Hello !");
    }

    @GetMapping("/Calc/{button}")
    public double button(@PathVariable("button") String button){
        double resp = service.buttonPressed(button);
        logger.debug(button + " pressed, " + resp + " returned.");
        System.out.println(button + " pressed, " + resp + " returned.");
        return resp;
    }

}
