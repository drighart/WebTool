'use strict';

angular.module('dashboard.configuration.website', [])
.controller('ConfigurationWebsiteCtrl', ['$rootScope', '$scope', '$timeout', '$http', '$filter', function($rootScope, $scope, $timeout, $http, $filter) {
	$http.get('/rest/services/configurationitem/list').success(function(data) {
      $scope.configurations = data;
    });

    $scope.saveConfiguration = function(data, id) {
    	data.name = id;
    	return $http.post('/rest/services/configurationitem/save', data);
    };
	
}]);