'use strict';

angular.module('dashboard.newmenuitem', [])
.controller('NewMenuItemCtrl', ['$rootScope', '$scope', '$timeout', '$http', function($rootScope, $scope, $timeout, $http) {
	
	$scope.cancel = function() {
		window.location = '#menuitems';
		return null;
	};

	$scope.save = function() {
		$http.post('/rest/services/menuitems/new', $scope.menuitem).
		success(function(data) {
			window.location = '#menuitems';
		}).
		error(function(data, status, headers, config) {
			console.error(status, data);
		});
		return null;
	};

}]);