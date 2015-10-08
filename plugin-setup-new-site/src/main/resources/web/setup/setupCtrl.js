'use strict';

angular.module('setup.setup', [])
.controller('SetupCtrl', ['$rootScope', '$scope', '$timeout', '$http', '$translate', function($rootScope, $scope, $timeout, $http, $translate) {
	$scope.language = 'nl_NL';
	
	$scope.save = function(name, emailAddress, password, passwordAgain) {
		var website = {
			"name": name,
			"emailAddress": emailAddress,
			"password": password,
			"passwordAgain": passwordAgain,
			"language": $scope.language
		};
		
		console.log(website);
		
		$http.post('/rest/unsecured/setup', website).
		success(function(data) {
			window.location = '/dashboard';
		}).
		error(function(data, status, headers, config) {
			console.error(status, data);
		});
		return null;
	};

	$scope.changeLanguage = function (key) {
	    $translate.use(key);
	};
	
}]);