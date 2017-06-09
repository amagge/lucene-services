package edu.asu.lucene.service.rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author devdemetri
 * Custom exception for general Lucene errors
 */
@ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR)
public class LuceneSearcherException extends Exception {

	private static final long serialVersionUID = 4941959372750095512L;

	public LuceneSearcherException(String msg) {
		super(msg);
	}
	
}
