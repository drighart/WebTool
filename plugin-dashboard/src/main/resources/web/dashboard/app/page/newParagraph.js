'use strict';

angular.module('dashboard.newparagraph', [])
.controller('NewParagraphCtrl', ['$rootScope', '$scope', '$timeout', '$http', '$state', '$stateParams', 
    function($rootScope, $scope, $timeout, $http, $state, $stateParams) {
	
	$scope.template = "TEXT";
	$scope.pageId = $stateParams.pageId;
	
	$scope.cancel = function() {
		$state.go('paragraphs');
		return null;
	};

	$scope.save = function() {
		$http.post('/rest/services/paragraph/new/' + $scope.pageId + '/' + $scope.template).
		success(function(data) {
			$rootScope.nrParagraphs++;
			$state.go('paragraphs', {}, {reload: true});
		}).
		error(function(data, status, headers, config) {
			console.error(status, data);
		});
		return null;
	};

}]);