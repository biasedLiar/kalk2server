package eliasoving4.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

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
    public double button(@PathVariable("button") String button, @RequestParam(name = "userId") int userId){
        double resp = service.buttonPressed(button, userId);
        logger.debug(button + " pressed, " + resp + " returned.");
        System.out.println(button + " pressed, " + resp + " returned.");
        return resp;
    }

    @GetMapping("/history")
    public ArrayList<Question> button(@RequestParam(name = "userId") int userId){
        logger.debug("History.");
        System.out.println("History.");
        return service.getEquationsOfUser(userId);
    }

}
