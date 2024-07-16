package com.crio.LearningNavigator.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crio.LearningNavigator.Service.EasterEggService;

@RestController
@RequestMapping("/hidden-feature")
public class EasterEggController {

    @Autowired
    EasterEggService easterEggService;

    @GetMapping("{number}")
    public ResponseEntity<String> generatefact(@PathVariable("number") long number){
        return new ResponseEntity<String>(easterEggService.getFact(number), HttpStatus.OK);
    } 
    
}
