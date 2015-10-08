'use strict';

angular.module('dashboard.paragraphs', [])
.filter('displayContent', function () { 
    return function (paragraph) {
        return paragraph.content ? paragraph.content : 'Edit the paragraph to fill this paragraph with text.';
    }
})
.controller('ParagraphsCtrl', ['$rootScope', '$scope', '$timeout', '$http', '$filter', '$cookies', function($rootScope, $scope, $timeout, $http, $filter, $cookies) {
	$rootScope.showTipMenuPages = false;
	$scope.showTipPageSelector = false;
	$scope.selectedPage = $cookies.get('selectedPage');
    if((typeof $scope.selectedPage === "undefined")) {
    	console.log('INIT');
		$scope.selectedPage = 0;
	}
	
	$scope.reloadParagraphs = function(page) {
		$scope.page = page;
		$scope.selectedPage = page.id;
		console.log('SELECTED: ' + page.id);
		$http.get('/rest/services/paragraph/list/' + page.id).success(function(data) {
		      $scope.paragraphs = data;
	    });
	};
	
	$scope.change = function(selectedPageId) {
		$cookies.putObject('selectedPage', selectedPageId);
		$scope.showTipPageSelector = false;
		
		$scope.pages.forEach(function(page) {
			if (page.id == selectedPageId) {
				console.log('SELECTED PAGE ID');
				$scope.reloadParagraphs(page);
			}
	    });
	}

	$scope.reload = function() {
		$http.get('/rest/services/pages/list').success(function(data) {
    	    $scope.pages = data;
    	    
    	    $scope.showTipPageSelector = ($scope.pages.length == 2);
    	    if ($scope.selectedPage != 0) {

    			$scope.pages.forEach(function(page) {
    				if (page.id == $scope.selectedPage) {
    					$scope.reloadParagraphs(page);
    				}
  		      	});
    			
    	    } else if ($scope.pages.length > 0) {
    	    	$scope.change($scope.pages[0].id);
			}
		});
	};
	$scope.reload();
	
	$scope.edit = function(paragraph) {
		window.location = '#paragraph/' + paragraph.id;
		return null;
	};

	$scope.remove = function(paragraph) {
		$http.delete('/rest/services/paragraph/remove/' + paragraph.id).
		success(function(data) {
			$rootScope.nrParagraphs--;
			$scope.reload();
		}).
		error(function(data, status, headers, config) {
			console.error(status, data);
		});

		return null;
	};

	$scope.newParagraph = function() {
		window.location = '#newParagraph/' + $scope.page.id;
		return null;
	};

	$scope.back = function() {
		$rootScope.reloadPageStats();
		window.location = '#dashboard';
		return null;
	};
	
	$scope.up = function(paragraph) {
		$http.put('/rest/services/paragraph/up/' + paragraph.pageId + '/' + paragraph.position).
		success(function(data) {
			$scope.reload();
		}).
		error(function(data, status, headers, config) {
			console.error(status, data);
		});

		return null;
	};

	$scope.down = function(paragraph) {
		$http.put('/rest/services/paragraph/down/' + paragraph.pageId + '/' + paragraph.position).
		success(function(data) {
			$scope.reload();
		}).
		error(function(data, status, headers, config) {
			console.error(status, data);
		});

		return null;
	};
	
}]);