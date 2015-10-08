'use strict';

angular.module('dashboard.dashboard', [])
.controller('DashboardCtrl', ['$rootScope', '$scope', '$timeout', '$http', '$window', '$location', function($rootScope, $scope, $timeout, $http, $window, $location) {	
	
	if ($rootScope.nrPages == 0) {
		$rootScope.reloadPageStats();
	}
	
	$scope.newPage = function() {
		window.location = '#newPage';
		return null;
	};

	$scope.openWebsite = function() {
		var url = $location.protocol() + "://" + $location.host() + ":" + $location.port() + "/app";
		console.log('Opening website on url: ' + url);
		$window.open(url, '_blank');
	};
	
}]);