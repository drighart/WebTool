package org.drdevelopment.webtool.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.drdevelopment.webtool.repository.UsersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FirstTimeFilter implements Filter {
	private static final Logger LOGGER = LoggerFactory.getLogger(FirstTimeFilter.class);

	private Boolean hasUsersInSystem = null;
	
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    	hasUsersInSystem = UsersRepository.getUsers().size() > 0;
    	LOGGER.info("Register filter.");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, 
                         ServletResponse servletResponse, 
                         FilterChain filterChain) throws IOException, ServletException {
    	LOGGER.info("FILTER HAS USERS {}", hasUsers());
        filterChain.doFilter(servletRequest, servletResponse);
        return;

    }

    private boolean hasUsers() {
    	if (hasUsersInSystem == null || !hasUsersInSystem) {
    		hasUsersInSystem = UsersRepository.getUsers().size() > 0;
    	}
    	return hasUsersInSystem;
    }
    
    @Override
    public void destroy() {
    	
    }

}