package com.hospital.auth.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.hospital.auth.exception.BadRequestException;
import com.hospital.auth.exception.DuplicateResourceException;
import com.hospital.auth.exception.ResourceNotFoundException;

@ControllerAdvice(basePackages = "com.hospital.auth.web")
public class WebUiExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ModelAndView notFound(ResourceNotFoundException ex) {
		ModelAndView mv = new ModelAndView("oops");
		mv.addObject("message", ex.getMessage());
		mv.setStatus(HttpStatus.NOT_FOUND);
		return mv;
	}

	@ExceptionHandler({ DuplicateResourceException.class, BadRequestException.class })
	public ModelAndView clientError(RuntimeException ex) {
		ModelAndView mv = new ModelAndView("oops");
		mv.addObject("message", ex.getMessage());
		mv.setStatus(HttpStatus.BAD_REQUEST);
		return mv;
	}
}
