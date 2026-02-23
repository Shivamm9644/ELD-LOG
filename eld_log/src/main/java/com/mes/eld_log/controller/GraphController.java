package com.mes.eld_log.controller;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;


@RestController
//@RequestMapping("/graph")
public class GraphController {
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
	   Map<String, String> errors = new HashMap<>();
	 
	   errors.put("status", "FAIL");
	   ex.getBindingResult().getFieldErrors().forEach(error ->
	           errors.put(error.getField(), error.getDefaultMessage()));
	   
	   return errors;
	}
	
	@GetMapping("/barChart")
	public String barChart(Model model)
	{
		Map<String, Integer> data = new LinkedHashMap<String, Integer>();
		data.put("Ashish", 30);
		data.put("Ankit", 50);
		data.put("Gurpreet", 70);
		data.put("Mohit", 90);
		data.put("Manish", 25);
		model.addAttribute("keySet", data.keySet());
		model.addAttribute("values", data.values());
		System.out.println("Call Graph...");
		return "barChart";
		
	}
	
	@GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("name", "John");
        return "home";  // The name of the Thymeleaf template without the ".html" extension
    }
	
	@PostMapping(value="/view_device")
	public String ViewDevice() {
		
		return "My Device";
	}
	
}
