'use strict';

angular.module('dashboard.generateimage', [])
.filter('listboxpreview', function () { 
    return function (input) {
        return input ? '<img src="/rest/unsecured/images/get/preview/' + input + '" style="height:100px; width:100px;"/>' : '';
    }
})
.controller('GenerateImageCtrl', ['$rootScope', '$scope', '$timeout', '$http', function($rootScope, $scope, $timeout, $http) {
	
	$scope.tags = '';
	$scope.selectedImages = {};
	$scope.nrOfSelectedImages = 0;
	$scope.filterTab = false;
	$scope.showProgressIndicator = false;
	$scope.filterModel = 'none';
	$scope.filterCount = 0;
	$scope.progressBarType = 'success';
		
	$scope.reload = function() {
		$http.get('/rest/services/images/list').success(function(data) {
		      $scope.images = data;
		      $scope.images.forEach(function(image) {
		    	  image.id = image.data;
		    	  image.src = '/rest/unsecured/images/get/preview/' + image.name;
		    	  image.title = image.name;
		      });
	    });
	};
	$scope.reload();

	$scope.cardSelected = function(card) {
		$scope.selectedImages[card.$index] = card;
		
		if (card.showIndex) {
			$scope.nrOfSelectedImages++;
		} else {
			$scope.nrOfSelectedImages--;
		}  	    
	}
	
	$scope.next = function() {
	  $scope.filterTab = !$scope.filterTab;
	};

	$scope.reselect = function() {
		$scope.filterTab = false;
	};

	$scope.apply = function() {
		if (!$scope.filterTab) {
			$scope.filterTab = !$scope.filterTab;
		} else {
			$scope.showProgressIndicator = true;
			if ($scope.applyFilterOnSelectedImages()) {
				window.location = '#images';
			}
		}
	};
	
	$scope.applyFilterOnSelectedImages = function() {
		var valid = true;
		for (var key in $scope.selectedImages) {
			var selectedImage = $scope.selectedImages[key];
			if (selectedImage.showIndex) {
				console.log('Apply filter on image: ' + selectedImage.title);
				if (!$scope.applyFilter(selectedImage.title)) {
					valid = false;
					$scope.progressBarType = 'danger';
				}
				$scope.filterCount++;
			}
		}
		return valid;
	}

	$scope.applyFilter = function(title, filter) {
		var applyFilter = {
				"name": title,
				"filter": $scope.filterModel,
				"postfix": $scope.postfix,
				"tags": $scope.tags
		};
		var valid = true;
		$http.post('/rest/services/image/applyfilter', applyFilter).
		success(function(data) {
		}).
		error(function(data, status, headers, config) {
			console.error("data: " + data + " status: " + status);
			valid = false;
		});
		return valid;
	}

	$scope.cancel = function() {
		window.location = '#images';
		return null;
	};

	$scope.done = function() {
		window.location = '#images';
		return null;
	};


}]);