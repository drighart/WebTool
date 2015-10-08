'use strict';

//declare modules
angular.module('Authentication', []);
angular.module('myApp.home', []);

// Declare app level module which depends on views, and components
var app = angular.module('myApp', [
  'Authentication',
  'ngRoute',
  'ngCookies',
  'myApp.home',
  'myApp.events',
  'myApp.logging',
  'myApp.connections',
  'myApp.configuration',
  'myApp.planboard',
  'myApp.ptu',
  'ui.bootstrap'
]).
config(['$routeProvider', function($routeProvider) {
    $routeProvider.
    when('/login', {
        templateUrl: 'authentication/login.html',
        controller: 'LoginCtrl',
        hideMenus: true
    }).
    when('/home', {
        templateUrl: 'home/home.html',
        controller: 'HomeCtrl'
    }).
    when('/', {
        templateUrl: 'home/home.html',
        controller: 'HomeCtrl'
    }).
    when('/events', {
        templateUrl: 'events/events.html',
        controller: 'EventsCtrl'
    }).
    when('/logging', {
        templateUrl: 'logging/logging.html',
        controller: 'LoggingCtrl'
    }).
    when('/connections', {
        templateUrl: 'connections/connections.html',
        controller: 'ConnectionsCtrl'
    }).
    when('/planboard', {
        templateUrl: 'planboard/planboard.html',
        controller: 'PlanboardCtrl'
    }).
    when('/ptu', {
        templateUrl: 'ptu/ptu.html',
        controller: 'PtuCtrl'
    }).
    when('/configuration', {
        templateUrl: 'configuration/configuration.html',
        controller: 'ConfigurationCtrl'
    }).
    otherwise({
        redirectTo: '/login'
    });
}]).
run(['$http', '$rootScope', '$interval', '$location', '$cookieStore', function($http, $rootScope, $interval, $location, $cookieStore) {
    $rootScope.homeUrl = '';
    
    // keep user logged in after page refresh
    $rootScope.globals = $cookieStore.get('globals_gui_agr') || {};
    if ($rootScope.globals.currentUser) {
        $http.defaults.headers.common['Authorization'] = 'Basic ' + $rootScope.globals.currentUser.authdata; // jshint ignore:line
    }

    $rootScope.$on('$locationChangeStart', function (event, next, current) {
        console.log('redirect to login.')
        // redirect to login page if not logged in
        if ($location.path() !== '/login' && !$rootScope.globals.currentUser) {
            $location.path('/login');
        }
    });
    
    $rootScope.getConnectionStatus = function() {
      if ($rootScope.globals.currentUser) {
          $http.get($rootScope.homeUrl + '/agr/rest/system/status').
          success(function(data, status, headers, config) {
              $rootScope.currentTime = new Date(data.currenttime);
              $rootScope.intradayCycle = data.intradayCycle;
              $rootScope.currentPtu = data.currentPtu;
              $rootScope.intradayCycleOffset = data.intradayCycleOffset;
              $rootScope.intradayCyclePtus = data.intradayCyclePtus;
          }).
          error(function(data, status, headers, config) {
              console.log(status, data);
          });
      } else {
          console.log('Not logged in to get the system status.');
      }
    }

    $rootScope.refreshPbcEvents = function() {
        $http.get($rootScope.homeUrl + '/agr/rest/pbcevent').
        success(function(data, status, headers, config) {
            $rootScope.pbcEvents = data.events;
        }).
        error(function(data, status, headers, config) {
            console.log(status, data);
        });
    }

    
    $interval(function() {
        $rootScope.getConnectionStatus();
        $rootScope.refreshPbcEvents();
    }, 10000);

    $rootScope.getConnectionStatus();
    $rootScope.refreshPbcEvents();
}]);
