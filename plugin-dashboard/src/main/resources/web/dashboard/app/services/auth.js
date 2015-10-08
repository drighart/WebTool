'use strict';

angular.module('dashboard')
.factory('AuthenticationService', [ '$http', '$rootScope', '$window', 'Session', 'AUTH_EVENTS', 
function($http, $rootScope, $window, Session, AUTH_EVENTS) {
	var authService = {};
	
	//the login function
	authService.login = function(user, success, error) {
		$http.post('/rest/unsecured/login/check', user).
		success(function(data) {
			console.log('create session for user ' + data.displayName);

			delete data.created;
			delete data.modified;
			delete data.activated;
			delete data.credentials;

			$window.sessionStorage["userInfo"] = JSON.stringify(data);
			
			Session.create(data);
			$rootScope.currentUser = data;
			$rootScope.showMenu = true;

			$rootScope.$broadcast(AUTH_EVENTS.loginSuccess);
			success(data);
		}).
		error(function(data, status, headers, config) {
			console.error(status, data);

			$rootScope.$broadcast(AUTH_EVENTS.loginFailed);
			error();
		});
		
	};

	authService.isAuthenticated = function() {
		return !!Session.user;
	};
	
	authService.isAuthorized = function(authorizedRoles) {
		if (!angular.isArray(authorizedRoles)) {
	      authorizedRoles = [authorizedRoles];
	    }
	    return (authService.isAuthenticated() &&
	      authorizedRoles.indexOf(Session.userRole) !== -1);
	};
	
	authService.logout = function(){
		Session.destroy();
		$window.sessionStorage.removeItem("userInfo");
		$rootScope.$broadcast(AUTH_EVENTS.logoutSuccess);
	}

	return authService;
} ]);