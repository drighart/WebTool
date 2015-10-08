'use strict';

angular.module('dashboard.newuser', [])
.controller('NewUserCtrl', ['$rootScope', '$scope', '$timeout', '$http', function($rootScope, $scope, $timeout, $http) {
	
	$scope.cancel = function() {
		window.location = '#users';
		return null;
	};

	$scope.save = function() {
		$http.post('/rest/services/user/new', $scope.user).
		success(function(data) {
			window.location = '#users';
		}).
		error(function(data, status, headers, config) {
			console.error(status, data);
		});
		return null;
	};

}]);