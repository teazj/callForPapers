package fr.sii.controller;

import java.util.List;

import fr.sii.domain.TestClass;
import fr.sii.repository.TestRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

@Controller
public class TestController {

	@Autowired
	TestRespository repository;

	@RequestMapping(value="/test", method= RequestMethod.POST)
	@ResponseBody
	public TestClass postGoogleSpreadsheet(@RequestBody @Valid TestClass c) {
		return repository.save(c);
	}
	
	@RequestMapping("/spring")
	String index(){
		return "index";
	}
	
	@RequestMapping("list")
	String list(Model model){
		List<TestClass> userList = repository.findAll();
		System.out.println(userList);
		model.addAttribute("userlist", userList);
		return "list";
	}
	
	@RequestMapping("add")
	String add(TestClass member){
		repository.save(member);
		return "redirect:/list";
	}
	
	String update(){
		return "redirect:/list";
	}

}
