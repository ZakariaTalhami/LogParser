package com.example.demo.controller;

import javax.websocket.server.PathParam;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class viewTesting {
	
	@RequestMapping("/view2")
	public String getView2(@RequestParam(name="name" , required=false, defaultValue ="fan") String name  , Model model) {
		
		return "how";
	}
	
	
	@GetMapping("/greeting")
    public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "greeting2";
    }
}
