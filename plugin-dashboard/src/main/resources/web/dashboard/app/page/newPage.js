'use strict';

angular.module('dashboard.newpage', [])
.controller('NewPageCtrl', ['$rootScope', '$scope', '$timeout', '$http', '$state', function($rootScope, $scope, $timeout, $http, $state) {
	
	$scope.cancel = function() {
		$state.go('dashboard');
//		window.location = '#dashboard';
		return null;
	};

	$scope.save = function() {
		$http.post('/rest/services/page/new', $scope.page).
		success(function(data) {
			$rootScope.showPagesMenu = true;
			$rootScope.nrPages++;
			$rootScope.showTipMenuPages = ($rootScope.nrPages == 1);

			$state.go('dashboard', {}, {reload: true});
		}).
		error(function(data, status, headers, config) {
			console.error(status, data);
		});
		return null;
	};

}]);