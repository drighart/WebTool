'use strict';

angular.module('dashboard.page', [])
.controller('PageCtrl', ['$rootScope', '$scope', '$timeout', '$http', '$stateParams', function($rootScope, $scope, $timeout, $http, $stateParams) {
	$scope.visibleIntroduction = false;
	
	$http.get('/rest/services/page/get/' + $stateParams.id).
	success(function(data) {
	      $scope.page = data;
	      $scope.visibleIntroduction = ($scope.page.introduction != null && $scope.page.introduction != "");
	}).
	error(function(data, status, headers, config) {
		console.error(status, data);
	});

	$scope.cancel = function() {
		window.location = '#pages';
		return null;
	};

	$scope.save = function() {
		$http.post('/rest/services/page/save', $scope.page).
		success(function(data) {
			window.location = '#pages';
		}).
		error(function(data, status, headers, config) {
			console.error(status, data);
		});
		return null;
	};
	
	$scope.toggle = function () {
        $scope.visibleIntroduction = !$scope.visibleIntroduction;
    };

}]);