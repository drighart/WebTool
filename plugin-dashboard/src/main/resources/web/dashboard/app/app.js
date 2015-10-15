'use strict';

angular.module('dashboard', []);

var dashboard = angular.module('dashboard', [
  'dashboard.dashboard',
  'dashboard.preferences',
  'dashboard.pages',
  'dashboard.paragraphs',
  'dashboard.paragraph',
  'dashboard.newparagraph',
  'dashboard.page',
  'dashboard.newpage',
  'dashboard.images',
  'dashboard.image',
  'dashboard.uploadimage',
  'dashboard.generateimage',
  'dashboard.users',
  'dashboard.user',
  'dashboard.newuser',
  'ui.bootstrap',
  'ui.router',
  'xeditable',
  'textAngular',
  'angularFileUpload',
  'akoenig.deckgrid',
  'colorpicker.module',
  'ngCookies'
]).
constant('USER_ROLES', {
	all : '*',
	admin : 'admin',
	editor : 'editor',
	guest : 'guest'
}).
constant('AUTH_EVENTS', {
	loginSuccess : 'auth-login-success',
	loginFailed : 'auth-login-failed',
	logoutSuccess : 'auth-logout-success',
	sessionTimeout : 'auth-session-timeout',
	notAuthenticated : 'auth-not-authenticated',
	notAuthorized : 'auth-not-authorized'
}).
config(function ($httpProvider) {
	$httpProvider.interceptors.push('AuthInterceptor');
}).
run(['$http', '$rootScope', '$interval', 'editableOptions', 'taOptions', '$cookies', 
         function($http, $rootScope, $interval, editableOptions, taOptions, $cookies) {
	console.log('Starting application.');
	editableOptions.theme = 'bs3';
	
	taOptions.toolbar = [
	                     ['h1', 'h2', 'h3', 'h4', 'h5', 'h6', 'p', 'pre', 'quote'],
	                     ['bold', 'italics', 'underline', 'strikeThrough', 'ul', 'ol', 'redo', 'undo', 'clear'],
	                     ['justifyLeft', 'justifyCenter', 'justifyRight', 'indent', 'outdent'],
	                     ['html', 'insertLink']
	                 ];
	
	var cookies = $cookies.getAll();
	angular.forEach(cookies, function (v, k) {
	    $cookies.remove(k);
	});
	
	$rootScope.showImagesMenu = false;
	$rootScope.showPagesMenu = false;
	$rootScope.showPreferencesMenu = false;
	$rootScope.showUsersMenu = false;
	$rootScope.showTipPageSelector = true;
	$rootScope.nrPages = 0;
	$rootScope.nrParagraphs = 0;
	$rootScope.showTipMenuPages = false;
	
	$rootScope.reloadPageStats = function() {
		$http.get('/rest/services/system/pageStats').success(function(data) {
			$rootScope.nrPages = data.nrOfPages;
			$rootScope.nrParagraphs = data.nrOfParagraphs;
			
			console.log('nrPages: ' + $rootScope.nrPages);
			console.log('nrParagraphs: ' + $rootScope.nrParagraphs);
			
			$rootScope.showTipMenuPages = ($rootScope.nrPages == 1 && $rootScope.nrParagraphs == 0);
			$rootScope.showPagesMenu = ($rootScope.nrPages > 0);
			$rootScope.showPreferencesMenu = ($rootScope.nrPages > 0);
			$rootScope.showTipPageSelector = ($rootScope.nrPages == 2);
		});
	};

}]);