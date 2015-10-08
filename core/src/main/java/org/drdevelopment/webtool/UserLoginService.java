package org.drdevelopment.webtool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.drdevelopment.webtool.dao.UserDao;
import org.drdevelopment.webtool.model.User;
import org.eclipse.jetty.security.IdentityService;
import org.eclipse.jetty.security.MappedLoginService;
import org.eclipse.jetty.server.UserIdentity;
import org.eclipse.jetty.util.security.Credential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserLoginService extends MappedLoginService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserLoginService.class);

    protected int _cacheTime;
    protected long _lastHashPurge;

    public UserLoginService() {
    }
    
    public UserLoginService(String name) {
    	super();
        setName(name);
    }
    
    public UserLoginService(String name, IdentityService identityService) {
    	super();
        setName(name);
        setIdentityService(identityService);
    }

    /**
     * @see org.eclipse.jetty.security.MappedLoginService#doStart()
     */
    @Override
    protected void doStart() throws Exception {
        _cacheTime *= 1000;
        _lastHashPurge = 0;
        super.doStart();
    }

    @Override
    public UserIdentity login(String username, Object credentials, ServletRequest request) {
//    	HttpServletRequest httpRequest = (HttpServletRequest) request;
//    	if (httpRequest.getRequestURI() != null) {
//    		String path = httpRequest.getRequestURI();
//    		LOGGER.info("requested: " + path);
//    	}
        long now = System.currentTimeMillis();
        if (now - _lastHashPurge > _cacheTime || _cacheTime == 0) {
            _users.clear();
            _lastHashPurge = now;
        }
        
        return super.login(username, credentials, request);
    }

    @Override
    protected void loadUsers() {   
    }
    
    @Override
    protected UserIdentity loadUser(String username) {
    	LOGGER.debug("loadUser: {}", username);
    	UserDao dao = Services.getServices().getDbi().open(UserDao.class);
		User user = dao.findUser(username);
		dao.close();
    	if (user == null) {
            LOGGER.warn("UserRealm {} could not load user information from database for user {}.", getName(), username);
            return null;
    	} else {
//    		LOGGER.debug("Load user with name {} and roles {}.", username, user.getRoles());
    		
    		List<String> roles = new ArrayList<>();
        	if (user.getRoles() != null) {
        		String rolesStr = StringUtils.remove(user.getRoles(), ' ');
        		roles = Arrays.asList(rolesStr.toLowerCase().split(","));
        	}

    		return putUser(username, user.getCredentials(), roles.toArray(new String[roles.size()]));
    	}
    }
    
    protected UserIdentity putUser (String username, String credentials, String[] roles) {
        return putUser(username, Credential.getCredential(credentials), roles);
    }

}
