'use strict';

angular.module('dashboard').
controller('ParentController', ['$scope', '$rootScope', 'AuthenticationService', 'AUTH_EVENTS', 'USER_ROLES', '$state', '$location',
function($scope, $rootScope, AuthenticationService, AUTH_EVENTS, USER_ROLES, $state, $location) {
//	$rootScope.showMenu = false;

	// this is the parent controller for all controllers.
	// Manages auth login functions and each controller
	// inherits from this controller	
	var showLoginDialog = function() {
//		$rootScope.showMenu = false;
//		$state.go('login');
//		$location.path('/login');
	};
	
	var setCurrentUser = function(){
		$scope.currentUser = $rootScope.currentUser;
	}
	
	var showNotAuthorized = function(){
		alert("Not Authorized");
	}
	
	$scope.currentUser = null;
	$scope.userRoles = USER_ROLES;
	$scope.isAuthorized = AuthenticationService.isAuthorized;
	
//	$rootScope.$on(AUTH_EVENTS.notAuthorized, showNotAuthorized);
//	$rootScope.$on(AUTH_EVENTS.notAuthenticated, showLoginDialog);
//	$rootScope.$on(AUTH_EVENTS.sessionTimeout, showLoginDialog);
//	$rootScope.$on(AUTH_EVENTS.logoutSuccess, showLoginDialog);
//	$rootScope.$on(AUTH_EVENTS.loginSuccess, setCurrentUser);
//
//	$rootScope.$on('$stateChangeStart', function (event, next) {
//	    var authorizedRoles = next.data.authorizedRoles;
////	    if (next.url === '/login') {
////	    	$rootScope.showMenu = false;
////	    	console.log('Login screen.');
////	    } else {
//		    if (!AuthenticationService.isAuthorized(authorizedRoles)) {
//		      event.preventDefault();
//		      if (AuthenticationService.isAuthenticated()) {
//		        // user is not allowed
//		    	console.log('user is not allowed.');
//		        $rootScope.$broadcast(AUTH_EVENTS.notAuthorized);
//		      } else {
//		        // user is not logged in
//	    	    console.log('user is not logged in.');
//		        $rootScope.$broadcast(AUTH_EVENTS.notAuthenticated);
//		      }
//		    }
////	    }
//	  });
//	
//	/* To show current active state on menu */
//	$rootScope.getClass = function(path) {
//		if ($state.current.name == path) {
//			return "active";
//		} else {
//			return "";
//		}
//	}
//	
//	$rootScope.logout = function(){
//		AuthenticationService.logout();
//	};
} 
]);