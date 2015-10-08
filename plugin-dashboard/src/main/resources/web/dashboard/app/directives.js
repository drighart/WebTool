angular.module('dashboard')
/**
 * Directive to solve the form autofill data
 * as desribed in this blog post: 
 * https://medium.com/opinionated-angularjs/techniques-for-authentication-in-angularjs-applications-7bbf0346acec
 */
.directive('autofocus', ['$timeout', function($timeout) {
  return {
    restrict: 'A',
    link : function($scope, $element) {
      $timeout(function() {
        $element[0].focus();
      });
    }
  }
}])
.directive('imageloaded', [ function () {
	return {
		restrict: 'A',
		link: function(scope, element, attrs) {   
			var cssClass = attrs.loadedclass;

			element.bind('load', function (e) {
				angular.element(element).addClass(cssClass);
			});
		}
	}
}]);


