'use strict';

angular.module('dashboard.paragraph', [])
.controller('ParagraphCtrl', ['$rootScope', '$scope', '$timeout', '$http', '$stateParams', 'FileUploader', 
       function($rootScope, $scope, $timeout, $http, $stateParams, FileUploader) {
	$scope.visibleButtonText = false;
	$scope.visibleImage = false;
	$scope.visibleBackground = false;
	
	$scope.imagePath = '';
	$scope.imageName = '';
	$scope.backgroundImagePath = '';
	$scope.backgroundImageName = '';
	$scope.uploadedFile = null;
	$scope.backgroundUploadedFile = null;
	$scope.imageAlignment = 'ALIGN_LEFT';
	
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
    
    // ==================================================================
    
    var uploaderBackground = $scope.uploaderBackground = new FileUploader({
        url: '/rest/services/images/upload'
    });

    // FILTERS
    uploaderBackground.filters.push({
        name: 'imageFilter',
        fn: function(item /*{File|FileLikeObject}*/, options) {
            var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
            return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
        }
    });
	
    uploaderBackground.onAfterAddingFile = function(fileItem) {
    	$scope.backgroundUploadedFile = "/rest/unsecured/images/get/preview/" + fileItem.file.name;
    	$scope.backgroundImageName = fileItem.file.name;
        uploaderBackground.uploadAll();
    };
    
    uploaderBackground.onCompleteAll = function() {
    	uploaderBackground.clearQueue();
        $scope.backgroundImagePath = $scope.backgroundUploadedFile;
    };

    // ==================================================================
    
	$http.get('/rest/services/paragraph/get/' + $stateParams.paragraphId).
	success(function(data) {
	      $scope.paragraph = data;
	      $scope.imagePath = "/rest/unsecured/images/get/preview/" + $scope.paragraph.imageName;
	      $scope.imageName = $scope.paragraph.imageName;
	      $scope.imageAlignment = ($scope.paragraph.imageAlignment != null && $scope.paragraph.imageAlignment != '' ? $scope.paragraph.imageAlignment : 'ALIGN_LEFT');
	      $scope.backgroundImagePath = "/rest/unsecured/images/get/preview/" + $scope.paragraph.backgroundImage;
	      $scope.backgroundImageName = $scope.paragraph.backgroundImage;
	      $scope.visibleButtonText = ($scope.paragraph.buttonLink != null && $scope.paragraph.buttonLink != "");
	      $scope.visibleImage = ($scope.paragraph.imageName != null && $scope.paragraph.imageName != "");
	      $scope.visibleBackground = ($scope.paragraph.backgroundImage != null && $scope.paragraph.backgroundImage != "" && $scope.paragraph.backgroundColor != null && $scope.paragraph.backgroundColor != "");
	}).
	error(function(data, status, headers, config) {
		console.error(status, data);
	});

	$scope.cancel = function() {
		window.location = '#paragraphs';
		return null;
	};

	$scope.save = function() {
		$scope.paragraph.imageName = $scope.imageName;
		$scope.paragraph.imageAlignment = $scope.imageAlignment;
		$scope.paragraph.backgroundImage = $scope.backgroundImageName;
		console.log($scope.paragraph.backgroundImage);
		$http.post('/rest/services/paragraph/save', $scope.paragraph).
		success(function(data) {
			window.location = '#paragraphs';
		}).
		error(function(data, status, headers, config) {
			console.error(status, data);
		});
		return null;
	};
	
	$scope.toggleButton = function () {
        $scope.visibleButtonText = !$scope.visibleButtonText;
    };

	$scope.toggleBackgroundButton = function () {
        $scope.visibleBackground = !$scope.visibleBackground;
    };

	$scope.toggleImage = function () {
        $scope.visibleImage = !$scope.visibleImage;
    };
    
	$scope.clearImage = function () {
		uploader.clearQueue();
		$scope.imagePath = '';
		$scope.imageName = '';
    };

	$scope.clearBackgroundImage = function () {
		uploaderBackground.clearQueue();
		$scope.backgroundImagePath = '';
		$scope.backgroundImageName = '';
    };
}]);