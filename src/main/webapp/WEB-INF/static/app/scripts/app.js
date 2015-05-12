'use strict';

angular.module('CallForPaper', [
    'ngCookies',
    'ngResource',
    'ngSanitize',
    'ui.router',
    'ngAnimate',
    'ui.bootstrap',
    'ngTagsInput',
    'internationalPhoneNumber',
    'bs-has',
    'pascalprecht.translate',
    'k8LanguagePicker',
    'ngTable',
    'ui-notification',
    'customFilters',
    'ui.gravatar',
    'relativeDate',
    'matchMedia'
  ])
  .config(['$stateProvider', '$urlRouterProvider', 'AuthServiceProvider', function($stateProvider, $urlRouterProvider, AuthServiceProvider) {
    $urlRouterProvider
      .when('/event/form', '/event/form/step1')
      .when('/event/form/', '/event/form/step1')
      .when('/event', '/event/form/step1')
      .when('/event/', '/event/form/step1')

    .when('/admin/sessions/', '/admin/sessions')
      .when('/admin/session/', '/admin/sessions')
      .when('/admin/session', '/admin/sessions')

    .when('/admin', '/admin/sessions')
      .when('/admin/', '/admin/sessions')

    .otherwise('/event/form/step1');
    $stateProvider
      .state('admin', {
        url: '/admin',
        abstract: true,
        views: {
          '': {
            templateUrl: 'views/admin/admin.html',
            controller: 'AdminCtrl',
            resolve: {
              isAutorizedAdmin: ['$q', '$window', '$state', AuthServiceProvider.$get().isAutorizedAdmin]
            }
          },
          '@form': {
            templateUrl: 'views/admin/sessions.html'
          }
        },
      })
      // Session
      .state('admin.sessions', {
        url: '/sessions',
        templateUrl: 'views/admin/sessions.html',
        controller: 'SessionsCtrl'
      })
      .state('admin.session', {
        url: '/session/:id',
        templateUrl: 'views/admin/session.html',
        controller: 'SessionCtrl'
      })

    // Login
    .state('login', {
      url: '/login',
      templateUrl: 'views/login.html',
      controller: 'LoginCtrl'
    })

    // Login
    .state('logout', {
      url: '/logout',
      templateUrl: 'views/login.html',
      controller: 'LogoutCtrl'
    })

    // Form
    .state('form', {
        url: '/event/form',
        abstract: true,
        views: {
          '': {
            templateUrl: 'views/form/form.html',
            controller: 'FormCtrl'
          },
          '@form': {
            templateUrl: 'views/form/step1.html'
          }
        },
      })
      // nested states
      // each of these sections will have their own view
      .state('form.step1', {
        url: '/step1',
        templateUrl: 'views/form/step1.html',
        controller: 'Step1Ctrl'
      })
      .state('form.step2', {
        url: '/step2',
        templateUrl: 'views/form/step2.html',
        controller: 'Step2Ctrl'
      })
      .state('form.step3', {
        url: '/step3',
        templateUrl: 'views/form/step3.html',
        controller: 'Step3Ctrl'
      })
      .state('form.result', {
        url: '/result',
        templateUrl: 'views/form/result.html',
        controller: 'ResultCtrl'
      })
      .state('close', {
        url: '/close',
        templateUrl: 'views/form/close.html'
      })
      .state('403', {
        url: '/403',
        templateUrl: '403.html'
      })
      .state('404', {
        url: '/404',
        templateUrl: '404.html'
      });
  }])
  .config(['tagsInputConfigProvider', function(tagsInputConfigProvider) {
    tagsInputConfigProvider
      .setDefaults('tagsInput', {
        placeholder: '',
        minLength: 1,
        addOnEnter: true
      })
      .setDefaults('autoComplete', {
        debounceDelay: 200,
        loadOnDownArrow: true,
        loadOnEmpty: true
      })
  }])
  .config(['$translateProvider', function($translateProvider) {
    $translateProvider.useCookieStorage();
  }])
  .run(['AuthService', function(AuthService) {
    AuthService.init();
  }]);