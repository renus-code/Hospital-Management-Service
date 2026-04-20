package com.hospital.hms.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.hospital.hms.exception.BadRequestException;
import com.hospital.hms.exception.DuplicateResourceException;
import com.hospital.hms.exception.ResourceNotFoundException;
import com.hospital.hms.exception.ServiceUnavailableException;

@ControllerAdvice(basePackages = "com.hospital.hms.web")
public class WebUiExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ModelAndView notFound(ResourceNotFoundException ex) {
		ModelAndView mv = new ModelAndView("oops");
		mv.addObject("message", ex.getMessage());
		mv.setStatus(HttpStatus.NOT_FOUND);
		return mv;
	}

	@ExceptionHandler({ BadRequestException.class, DuplicateResourceException.class, ServiceUnavailableException.class })
	public ModelAndView clientError(RuntimeException ex) {
		ModelAndView mv = new ModelAndView("oops");
		mv.addObject("message", ex.getMessage());
		mv.setStatus(HttpStatus.BAD_REQUEST);
		return mv;
	}
}
