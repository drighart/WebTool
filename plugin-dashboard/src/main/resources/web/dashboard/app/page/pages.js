'use strict';

angular.module('dashboard.pages', [])
.controller('PagesCtrl', ['$rootScope', '$scope', '$timeout', '$http', '$filter', function($rootScope, $scope, $timeout, $http, $filter) {

	$scope.reload = function() {
		$http.get('/rest/services/pages/list').success(function(data) {
		      $scope.pages = data;
	    });
	};
	$scope.reload();
	
	$scope.editPage = function(page) {
		window.location = '#page/' + page.id;
		return null;
	};

	$scope.removePage = function(page) {
		$http.delete('/rest/services/page/remove/' + page.id).
		success(function(data) {
			$scope.reload();
		}).
		error(function(data, status, headers, config) {
			console.error(status, data);
		});

		return null;
	};

	$scope.newPage = function() {
		window.location = '#newPage';
		return null;
	};

	$scope.upPage = function(page) {
		$http.put('/rest/services/page/up/' + position).
		success(function(data) {
			$scope.reload();
		}).
		error(function(data, status, headers, config) {
			console.error(status, data);
		});

		return null;
	};

	$scope.downPage = function(position) {
		$http.put('/rest/services/page/down/' + position).
		success(function(data) {
			$scope.reload();
		}).
		error(function(data, status, headers, config) {
			console.error(status, data);
		});

		return null;
	};

	// ------------------------------------------------------------------
	
	$scope.editParagraph = function(page) {
		window.location = '#page/' + page.id;
		return null;
	};

	$scope.removeParagraph = function(page) {
		$http.delete('/rest/services/page/remove/' + page.id).
		success(function(data) {
			$scope.reload();
		}).
		error(function(data, status, headers, config) {
			console.error(status, data);
		});

		return null;
	};
	
	$scope.upParagraph = function(page) {
		$http.put('/rest/services/paragraph/up/' + page.position).
		success(function(data) {
			$scope.reload();
		}).
		error(function(data, status, headers, config) {
			console.error(status, data);
		});

		return null;
	};

	$scope.downParagraph = function(page) {
		$http.put('/rest/services/paragraph/down/' + page.position).
		success(function(data) {
			$scope.reload();
		}).
		error(function(data, status, headers, config) {
			console.error(status, data);
		});

		return null;
	};
	
}]);