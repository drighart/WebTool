'use strict';

angular.module('dashboard.menuitem', [])
.controller('MenuItemCtrl', ['$rootScope', '$scope', '$timeout', '$http', '$stateParams', function($rootScope, $scope, $timeout, $http, $stateParams) {
	$http.get('/rest/services/menuitem/get/' + $stateParams.id).
	success(function(data) {
	      $scope.menuitem = data;
	}).
	error(function(data, status, headers, config) {
		console.error(status, data);
	});

	$http.get('/rest/services/pages/list').
	success(function(data) {
	      $scope.pages = data;
	}).
	error(function(data, status, headers, config) {
		console.error(status, data);
	});
	
	$scope.cancel = function() {
		window.location = '#menuitems';
		return null;
	};

	$scope.save = function() {
		$http.post('/rest/services/menuitem/save', $scope.menuitem).
		success(function(data) {
			window.location = '#menuitems';
		}).
		error(function(data, status, headers, config) {
			console.error(status, data);
		});
		return null;
	};

}]);