package airbnb.controller;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

	public static final String DEFAULT_ERROR_VIEW = "error";
	
	// Specify name of a specific view that will be used to display the error:
	@ExceptionHandler(SQLException.class)
	public String databaseError(Model model, Exception e) {
		// Nothing to do.  Returns the logical view name of an error page, passed
		// to the view-resolver(s) in usual way.
		// Note that the exception is NOT available to this view (it is not added
		// to the model) but see using ModelAndView below.
		model.addAttribute("error", "Unable to retrieve or update your data in the database. "
				+ "This may be due to an issue in our server. "
				+ "Please try again and make sure you enter valid data.");
		return DEFAULT_ERROR_VIEW;
	}

	// Total control - setup a model and return the view name yourself
	@ExceptionHandler(value = Exception.class)
	public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
		
		// Otherwise setup and send the user to a default error-view.
		ModelAndView mav = new ModelAndView();
		
		mav.addObject("url", req.getRequestURL());
		mav.setViewName(DEFAULT_ERROR_VIEW);
		return mav;
	}
}
