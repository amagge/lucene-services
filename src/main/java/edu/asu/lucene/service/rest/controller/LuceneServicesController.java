package edu.asu.lucene.service.rest.controller;

import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import edu.asu.lucene.service.rest.exception.InvalidLuceneQueryException;
import edu.asu.lucene.service.rest.exception.LuceneSearcherException;
import edu.asu.lucene.service.rest.search.LuceneSearcher;
import edu.asu.lucene.service.rest.search.Result;

/**
 * Responsible for mapping Lucene service requests
 * @author devdemetri, amagge
 */
@RestController
public class LuceneServicesController {
	
	@Autowired
	private LuceneSearcher indexSearcher;
	
	@Value("${lucene.query.default.records}")
	private Integer QUERY_DEFAULT_RECORDS;

	@Value("${lucene.query.max.records}")
	private Integer QUERY_MAX_RECORDS;
	
	private final static Logger logger = Logger.getLogger("LuceneServicesController");
	
	/**
	 * Simple check that REST services are running
	 * @return message that services are running
	 */
	@RequestMapping(value="/", method=RequestMethod.GET)
    @ResponseStatus(value=HttpStatus.OK)
	public String checkService() {
		return "Lucene services are up and running.";
	}

    /**
     * Retrieve resultss for Lucene query
     * @param query - Valid Lucene querystring
     * @param count - Number of records requested (Optional)
     * @return Result results of given query.
     * @throws LuceneSearcherException 
     * @throws InvalidLuceneQueryException 
     * @throws ParameterException 
     */
    @RequestMapping(value="/search", method=RequestMethod.GET)
    @ResponseStatus(value=HttpStatus.OK)
    public Result queryLucene(@RequestParam(value="query") String query,
    		@RequestParam(value="count", required = false) String countStr) 
    				throws LuceneSearcherException, InvalidLuceneQueryException {
		if (!query.trim().isEmpty()) {
			int count = QUERY_DEFAULT_RECORDS;
			boolean showAvailable = false;
			if(countStr != null){
				try{
					count = Integer.parseInt(countStr);
					count = Math.min(QUERY_MAX_RECORDS, Math.abs(count));
					logger.info("Requested count: " + count);
				} catch (NumberFormatException e){
					if(!countStr.equalsIgnoreCase("all")){
						logger.warning("Didn't recognize count '" + countStr + "'. Assigning default "+ QUERY_DEFAULT_RECORDS);
					} else {
						logger.info("Requested all available records");
						showAvailable = true;
					}
				}
			} else {
				logger.warning("Requesting default count "+ QUERY_DEFAULT_RECORDS);
			}
    		Result results = indexSearcher.searchIndex(query, count, showAvailable);
    		logger.info("Search for '" + query +"' found " + results.getAvailable() +
    				" and retrieved " + results.getRetrieved() + " records");
    		return results;
    	} else {
    		throw new InvalidLuceneQueryException(query);
    	}
    }

}
