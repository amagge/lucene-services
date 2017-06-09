package edu.asu.lucene.service.rest.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Handles request errors
 * @author devdemetri, amagge
 */
@Controller
public class LuceneServicesErrorController implements ErrorController{

    /**
     * Error Attributes in the Application
     */
    private ErrorAttributes errorAttributes;

    private final static String ERROR_PATH = "/error";

    private final static Logger log = Logger.getLogger("LuceneServicesErrorController");

    /**
     * Controller for the Error Controller
     * @param errorAttributes
     */
    public LuceneServicesErrorController(ErrorAttributes errorAttributes) {
        this.errorAttributes = errorAttributes;
    }

    /**
     * Supports other formats like JSON, XML
     * @param request
     * @return HTTP ResponseEntity containing the HTTP error code and error message
     */
    @RequestMapping(value = ERROR_PATH)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
        Map<String, Object> attributes = getErrorAttributes(request, getTraceParameter(request));
        log.warning("Handling request error from: "+ request.getRemoteAddr() + " : " + attributes.get("exception") + " : " + attributes.get("message"));
        HttpStatus status = getStatus(request);
        Map<String, Object> body = new HashMap<String, Object>();
        body.put("status", attributes.get("status"));
        if (status.equals(HttpStatus.NOT_FOUND) && attributes.get("message").equals("No message available")) {
        	body.put("message", "Invalid Path");
        }
        else {
        	body.put("message", attributes.get("message"));
        }
        body.put("path", attributes.get("path"));
        return new ResponseEntity<Map<String, Object>>(body, status);
    }

    private boolean getTraceParameter(HttpServletRequest request) {
        String parameter = request.getParameter("trace");
        if (parameter == null) {
            return false;
        }
        return !"false".equals(parameter.toLowerCase());
    }

    private Map<String, Object> getErrorAttributes(HttpServletRequest request, boolean includeStackTrace) {
        RequestAttributes requestAttributes = new ServletRequestAttributes(request);
        return this.errorAttributes.getErrorAttributes(requestAttributes, includeStackTrace);
    }

    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode != null) {
            try {
                return HttpStatus.valueOf(statusCode);
            }
            catch (Exception e) {
            	log.warning("Could not convert status code!"+statusCode+": "+e.getMessage());
            }
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
    
    /**
     * Returns the path of the error page.
     * @return the error path
     */
    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }
    
}
