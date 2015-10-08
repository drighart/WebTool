'use strict';

angular.module('dashboard.menuitems', [])
.controller('MenuItemsCtrl', ['$rootScope', '$scope', '$timeout', '$http', '$filter',  
      function($rootScope, $scope, $timeout, $http, $filter) {

	$scope.reload = function() {
		$http.get('/rest/services/menuitems/list').success(function(data) {
		      $scope.menuitems = data;
		});
	};
	$scope.reload();
	
	$scope.add = function() {
		window.location = '#newMenuitem';
		return null;
	};

	$scope.edit = function(id) {
		window.location = '#menuitem/' + id;
		return null;
	};
	
	$scope.up = function(position) {
		$http.put('/rest/services/menuitems/up/' + position).
		success(function(data) {
			$scope.reload();
		}).
		error(function(data, status, headers, config) {
			console.error(status, data);
		});

		return null;
	};

	$scope.down = function(position) {
		$http.put('/rest/services/menuitems/down/' + position).
		success(function(data) {
			$scope.reload();
		}).
		error(function(data, status, headers, config) {
			console.error(status, data);
		});

		return null;
	};

	$scope.remove = function(name) {
		$http.delete('/rest/services/menuitems/remove/' + name).
		success(function(data) {
			$scope.reload();
		}).
		error(function(data, status, headers, config) {
			console.error(status, data);
		});

		return null;
	};

}]);