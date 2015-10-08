'use strict';

angular.module('dashboard.images', [])
.filter('preview', function () { 
    return function (input) {
        return input ? '<img src="/rest/unsecured/images/get/preview/' + input + '"/>' : '';
    }
})
.controller('ImagesCtrl', ['$rootScope', '$scope', '$timeout', '$http', '$filter', function($rootScope, $scope, $timeout, $http, $filter) {
	
	$scope.reload = function() {
		$http.get('/rest/services/images/list').success(function(data) {
		      $scope.images = data;
	    });
	};
	$scope.reload();
	
	$scope.edit = function(name) {
		window.location = '#image/' + name;
		return null;
	};

	$scope.remove = function(name) {
		$http.delete('/rest/services/images/remove/' + name).
		success(function(data) {
			$scope.reload();
		}).
		error(function(data, status, headers, config) {
			console.error(status, data);
		});

		return null;
	};

	$scope.uploadImages = function() {
		window.location = '#uploadImages';
		return null;
	};

	$scope.generateImages = function() {
		window.location = '#generateImages';
		return null;
	};

}]);