package com.quotepro.quotation;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/quation")
@CrossOrigin
public interface QuationSerive {

	@GetMapping("/grid")
	ResponseEntity<Object> Grid(); // Grid

	@PostMapping("/save")
	<T> Object save(@RequestBody QuationDTO dto); // Save Quality Detail

	@GetMapping("/getdata")
	ResponseEntity<Object> getQualityData(@RequestParam("customer_id") String customer_id); // Edit Get Data
	
	@GetMapping("/delete")
	<T> Object deleteData(@RequestParam("customer_id") String customer_id); // Delete Data
}
