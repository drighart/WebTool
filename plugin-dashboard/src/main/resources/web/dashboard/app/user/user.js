'use strict';

angular.module('dashboard.user', [])
.controller('UserCtrl', ['$rootScope', '$scope', '$timeout', '$http', '$stateParams', function($rootScope, $scope, $timeout, $http, $stateParams) {
	$http.get('/rest/services/user/get/' + $stateParams.emailAddress).
	success(function(data) {
	      $scope.user = data;
	}).
	error(function(data, status, headers, config) {
		console.error(status, data);
	});

	$scope.cancel = function() {
		window.location = '#users';
		return null;
	};

	$scope.save = function() {
		$http.post('/rest/services/user/save', $scope.user).
		success(function(data) {
			window.location = '#users';
		}).
		error(function(data, status, headers, config) {
			console.error(status, data);
		});
		return null;
	};

}]);