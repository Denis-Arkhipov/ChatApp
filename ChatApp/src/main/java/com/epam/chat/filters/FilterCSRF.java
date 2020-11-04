package com.epam.chat.filters;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CSRF prevention filter. Generates tokens for page validation.
 * 
 * @author Arkhipov Denis
 * @version 1.0 2020
 */
public class FilterCSRF implements Filter {
    private static final String NAME_TITLE_CSRF = "CSRF-TOKEN";
    private static final String LOGIN_URI = "/ChatApp/Controller/user/login";
    private static final String LOGOUT_URI = "/ChatApp/Controller/user/logout";
    private static final String REGISTRATION_URI = "/ChatApp/Controller/user"
    	+ "/registration";
    private static final Logger logger = LoggerFactory.getLogger(
	    FilterCSRF.class);
    private Set<String> token = new HashSet<>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
	// implements initialization
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
	    FilterChain chain)
	    throws IOException, ServletException {
	HttpServletRequest httpReq = (HttpServletRequest) request;
	HttpServletResponse httpResp = (HttpServletResponse) response;
	String tokenOfReq = httpReq.getHeader(NAME_TITLE_CSRF);
	logger.debug("Received token from client: {}", tokenOfReq);
	String uri = httpReq.getRequestURI();

	switch (uri) {
	case LOGIN_URI:	    
	    setNewToken(httpResp, tokenOfReq);
	    chain.doFilter(request, response);	    
	    break;
	case LOGOUT_URI:	    
	    this.token.remove(tokenOfReq);
	    chain.doFilter(request, response);
	    break;
	case REGISTRATION_URI:
	    chain.doFilter(request, response);
	    break;
	default:
	    if (this.token.contains(tokenOfReq)) {
		setNewToken(httpResp, tokenOfReq);
		chain.doFilter(request, response);
	    } else {
		httpResp.sendError(HttpServletResponse.SC_UNAUTHORIZED,
			"Invalid CSRF Token.");
	    }
	    break;
	}

    }

    @Override
    public void destroy() {
	// implements destroy
    }

    private void setNewToken(HttpServletResponse response, String oldToken) {
	SecureRandom random = new SecureRandom();
	String newToken = random.generateSeed(20).toString();
	logger.debug("New token: {}", newToken);

	this.token.remove(oldToken);
	this.token.add(newToken);

	response.setHeader(NAME_TITLE_CSRF, newToken);
    }
}
