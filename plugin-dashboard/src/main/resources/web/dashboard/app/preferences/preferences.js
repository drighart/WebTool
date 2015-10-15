'use strict';

angular.module('dashboard.preferences', [])
.controller('PreferencesCtrl', ['$rootScope', '$scope', '$timeout', '$http', '$stateParams', 'FileUploader', 
       function($rootScope, $scope, $timeout, $http, $stateParams, FileUploader) {
	$scope.imagePath = '';
	$scope.imageName = '';
	$scope.uploadedFile = null;
	
    var uploader = $scope.uploader = new FileUploader({
        url: '/rest/services/images/upload'
    });

    // FILTERS
    uploader.filters.push({
        name: 'imageFilter',
        fn: function(item /*{File|FileLikeObject}*/, options) {
            var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
            return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
        }
    });
	
    uploader.onAfterAddingFile = function(fileItem) {
    	$scope.uploadedFile = "/rest/unsecured/images/get/preview/" + fileItem.file.name;
    	$scope.imageName = fileItem.file.name;
        uploader.uploadAll();
    };
    
    uploader.onCompleteAll = function() {
        console.info('onCompleteAll');
        uploader.clearQueue();
        $scope.imagePath = $scope.uploadedFile;
    };
    
	$http.get('/rest/services/preferences/get').
	success(function(data) {
	      $scope.preferences = data;
	      $scope.imagePath = "/rest/unsecured/images/get/preview/" + $scope.preferences.websiteImageName;
	      $scope.imageName = $scope.preferences.websiteImageName;
	}).
	error(function(data, status, headers, config) {
		console.error(status, data);
	});

	$scope.cancel = function() {
		window.location = '#dashboard';
		return null;
	};

	$scope.save = function() {
		$scope.preferences.websiteImageName = $scope.imageName;
		$http.post('/rest/services/preferences/save', $scope.preferences).
		success(function(data) {
			window.location = '#dashboard';
		}).
		error(function(data, status, headers, config) {
			console.error(status, data);
		});
		return null;
	};
	    
	$scope.clearImage = function () {
		uploader.clearQueue();
		$scope.imagePath = '';
		$scope.imageName = '';
    };
}]);