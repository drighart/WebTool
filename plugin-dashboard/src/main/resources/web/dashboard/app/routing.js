angular.module('dashboard')
.config(['$stateProvider', '$urlRouterProvider', 'USER_ROLES',
function($stateProvider, $urlRouterProvider, USER_ROLES) {

  // For any unmatched url, redirect to /
  $urlRouterProvider.otherwise("/dashboard");
  
  // Now set up the states
  $stateProvider
	.state('dashboard', {
      url: "/dashboard",
      templateUrl: "dashboard/dashboard.html",
      controller: 'DashboardCtrl',
      data: {
          authorizedRoles: [USER_ROLES.admin, USER_ROLES.editor, USER_ROLES.guest]
      }
    })
  	.state('preferences', {
      url: "/preferences",
      templateUrl: "preference/preference.html",
      controller: 'PreferenceCtrl',
      data: {
          authorizedRoles: [USER_ROLES.admin, USER_ROLES.editor, USER_ROLES.guest]
      }
    })
  	.state('pages', {
      url: "/pages",
      templateUrl: "page/pages.html",
      controller: 'PagesCtrl',
	  data: {
          authorizedRoles: [USER_ROLES.admin, USER_ROLES.editor]
      }      	  
    })
    .state('paragraphs', {
      url: "/paragraphs",
      templateUrl: "page/paragraphs.html",
      controller: 'ParagraphsCtrl',
	  data: {
          authorizedRoles: [USER_ROLES.admin, USER_ROLES.editor]
      }      	  
    })
    .state('newParagraph', {
      url: "/newParagraph/{pageId}",
      templateUrl: "page/newParagraph.html",
      controller: 'NewParagraphCtrl',
	  data: {
          authorizedRoles: [USER_ROLES.admin, USER_ROLES.editor]
      }      	  
    })
    .state('paragraph', {
      url: "/paragraph/{paragraphId}",
      templateUrl: "page/paragraph.html",
      controller: 'ParagraphCtrl',
	  data: {
          authorizedRoles: [USER_ROLES.admin, USER_ROLES.editor]
      }      	  
    })
    .state('page', {
      url: "/page/{id}",
      templateUrl: "page/page.html",
      controller: 'PageCtrl',
      data: {
          authorizedRoles: [USER_ROLES.admin, USER_ROLES.editor, USER_ROLES.guest]
      }
    })
  	.state('newPage', {
      url: "/newPage",
      templateUrl: "page/newPage.html",
      controller: 'NewPageCtrl',
      data: {
          authorizedRoles: [USER_ROLES.admin, USER_ROLES.editor, USER_ROLES.guest]
      }
    })

  	.state('image', {
      url: "/image/{id}",
      templateUrl: "image/image.html",
      controller: 'ImageCtrl',
      data: {
          authorizedRoles: [USER_ROLES.admin, USER_ROLES.editor, USER_ROLES.guest]
      }
    })
  	.state('images', {
      url: "/images",
      templateUrl: "image/images.html",
      controller: 'ImagesCtrl',
      data: {
          authorizedRoles: [USER_ROLES.admin, USER_ROLES.editor, USER_ROLES.guest]
      }
    })
  	.state('uploadImages', {
      url: "/uploadImages",
      templateUrl: "image/uploadImage.html",
      controller: 'UploadImageCtrl',
      data: {
          authorizedRoles: [USER_ROLES.admin, USER_ROLES.editor, USER_ROLES.guest]
      }
    })
  	.state('generateImages', {
      url: "/generateImages",
      templateUrl: "image/generateImage.html",
      controller: 'GenerateImageCtrl',
      data: {
          authorizedRoles: [USER_ROLES.admin, USER_ROLES.editor, USER_ROLES.guest]
      }
    })
  	.state('users', {
      url: "/users",
      templateUrl: "user/users.html",
      controller: 'UsersCtrl',
	  data: {
          authorizedRoles: [USER_ROLES.admin]
      }      	  
    })
  	.state('user', {
      url: "/user/{emailAddress}",
      templateUrl: "user/user.html",
      controller: 'UserCtrl',
      data: {
          authorizedRoles: [USER_ROLES.admin]
      }
    })
  	.state('newUser', {
      url: "/newUser",
      templateUrl: "user/newUser.html",
      controller: 'NewUserCtrl',
      data: {
          authorizedRoles: [USER_ROLES.admin, USER_ROLES.editor]
      }
    })
    ;
}]);