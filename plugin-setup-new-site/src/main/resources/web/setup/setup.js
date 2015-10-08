'use strict';

angular.module('register', []);

var registerConsole = angular.module('setup', [
  'setup.setup',
  'ui.bootstrap',
  'ui.router',
  'pascalprecht.translate',
  'ngMessages'
]).
directive('autofocus', ['$timeout', function($timeout) {
  return {
    restrict: 'A',
    link : function($scope, $element) {
      $timeout(function() {
        $element[0].focus();
      });
    }
  }
}]).
config(function ($translateProvider) {
  $translateProvider.translations('en_US', {
	TITLE: 'Setup your website!',
	LANGUAGE: 'Choose your language?',
	NAME_YOUR_WEBSITE: 'What is the name of your website?',
	HELP_WEBSITENAME: 'Give your website a name. This is also the name which will be shown on the home page of your website.',
	EMAIL: 'Your email address?',
	HELP_EMAIL: 'What is your email-address? With this email-address you are allowed to login on the admin console. This email-address is also used when you forgot your password.',
	PASSWORD: 'Your password',
	HELP_PASSWORD: 'To protect the admin console, you have to enter a password.',
	PASSWORD_AGAIN: 'Enter your password again',
	HELP_PASSWORD_AGAIN: 'Please, repeat the password again to make sure it is correct.',
	NEXT: 'Next'
  });
  $translateProvider.translations('nl_NL', {
    TITLE: 'Installeer je website!',
	LANGUAGE: 'Kies uw taal?',
	NAME_YOUR_WEBSITE: 'Hoe heet je website?',
	HELP_WEBSITENAME: 'Geef je website een naam. Dit is de naam die terug ziet op de welkomst pagina van je website.',
    EMAIL: 'Wat is je email-adres?',
    HELP_EMAIL: 'Met dit email-adres kun je inloggen op het adminitratie-gedeelte. Dit email-adres wordt ook gebruikt wanneer u het wachtwoord vergeten bent of uw wachtwoord wilt veranderen.',
	PASSWORD: 'Je wachtwoord?',
	HELP_PASSWORD: 'Om het administratie-gedeelte te beveiligen, hebben we een wachtwoord nodig.',
	PASSWORD_AGAIN: 'Geef het wachtwoord nogmaals in?',
	HELP_PASSWORD_AGAIN: 'Herhaal het wachtwoord nogmaals.',
	NEXT: 'Volgende'
  });
  // Tell the module what language to use by default
  $translateProvider.preferredLanguage('nl_NL');

}).
run(['$http', '$rootScope',
         function($http, $rootScope) {


}]);