'use strict';


angular.module('dashboard.users', [])
.filter('activated', function () { 
    return function (input) {
        return input ? '<i class="glyphicon glyphicon-ok"></i>' : '';
    }
})
.controller('UsersCtrl', ['$rootScope', '$scope', '$timeout', '$http', '$filter', function($rootScope, $scope, $timeout, $http, $filter) {
	
	$scope.reload = function() {
		$http.get('/rest/services/users/list').success(function(data) {
		      $scope.users = data;
	    });
	};
	$scope.reload();
	
	$scope.edit = function(emailAddress) {
		window.location = '#user/' + emailAddress;
		return null;
	};

	$scope.remove = function(emailAddress) {
		$http.delete('/rest/services/user/remove/' + emailAddress).
		success(function(data) {
			$scope.reload();
		}).
		error(function(data, status, headers, config) {
			console.error(status, data);
		});

		return null;
	};

	$scope.newUser = function() {
		window.location = '#newUser';
		return null;
	};

}]);