package com.quotepro.mattresscompany;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/mattcompany")
@CrossOrigin
public interface CompanyService {

	@GetMapping("/grid")
	ResponseEntity<Object> Grid(); // Grid

	@PostMapping("/save")
	<T> Object save(@RequestBody MattressCompanyDTO dto); // Save Quality Detail

	@GetMapping("/getdata")
	ResponseEntity<Object> getData(@RequestParam("id") String mattcompanyid); // Edit Get Data

	@GetMapping("/delete")
	<T> Object deleteData(@RequestParam("id") String mattcompanyid); // Delete Data
}
