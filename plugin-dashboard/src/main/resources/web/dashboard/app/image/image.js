'use strict';

angular.module('dashboard.image', [])
.controller('ImageCtrl', ['$rootScope', '$scope', '$timeout', '$http', '$stateParams', function($rootScope, $scope, $timeout, $http, $stateParams) {
	$http.get('/rest/services/image/get/' + $stateParams.id).
	success(function(data) {
	      $scope.image = data;
	}).
	error(function(data, status, headers, config) {
		console.error(status, data);
	});
	
	$scope.cancel = function() {
		window.location = '#images';
		return null;
	};

	$scope.save = function() {
		$http.post('/rest/services/image/save', $scope.image).
		success(function(data) {
			window.location = '#images';
		}).
		error(function(data, status, headers, config) {
			console.error(status, data);
		});
		return null;
	};

}]);