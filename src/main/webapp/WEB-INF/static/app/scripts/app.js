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
    'matchMedia',
    'satellizer',
    'angular-jwt'
  ])
  .config(['$httpProvider', function($httpProvider) {
    //Http Intercpetor to check auth failures for xhr requests
    $httpProvider.interceptors.push('authHttpResponseInterceptor');
    $httpProvider.interceptors.push('csrfInterceptor');
  }])
  .config(['$stateProvider', '$urlRouterProvider', 'AuthServiceProvider', function($stateProvider, $urlRouterProvider, AuthServiceProvider) {
    $urlRouterProvider
      .when('', '/dashboard')
      .when('/', '/dashboard')

    .when('/admin/sessions/', '/admin/sessions')
      .when('/admin/session/', '/admin/sessions')
      .when('/admin/session', '/admin/sessions')

    .when('/admin', '/admin/sessions')
      .when('/admin/', '/admin/sessions')

    .when('/form', '/form/')

    .otherwise('/login');
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
            },
            '@admin': {
              templateUrl: 'views/admin/sessions.html',
              controller: 'AdminSessionsCtrl'
            }
          }
        },
      })
      // Session
      .state('admin.sessions', {
        url: '/sessions',
        templateUrl: 'views/admin/sessions.html',
        controller: 'AdminSessionsCtrl'
      })
      .state('admin.session', {
        url: '/session/:id',
        templateUrl: 'views/admin/session.html',
        controller: 'AdminSessionCtrl'
      })
      // Login
      .state('loginAdmin', {
        url: '/admin/login',
        templateUrl: 'views/admin/login.html',
        controller: 'AdminLoginCtrl'
      })
      // Logout
      .state('logoutAdmin', {
        url: '/admin/logout',
        templateUrl: 'views/admin/login.html',
        controller: 'AdminLogoutCtrl'
      })

    // Restricted
    .state('app', {
        abstract: true,
        views: {
          '': {
            templateUrl: 'views/header.html',
            controller: 'HeaderCtrl'
          },
          '@app': {
            templateUrl: 'views/restricted/dashboard.html'
          }
        },
      })
      .state('app.dashboard', {
        url: '/dashboard',
        templateUrl: 'views/restricted/dashboard.html',
        controller: 'DashboardCtrl',
        resolve: {
          authenticated: ['$q', '$location', '$auth', 'jwtHelper', AuthServiceProvider.$get().authenticated]
        }
      })

    // Auth
    .state('app.login', {
        url: '/login',
        templateUrl: 'views/login.html',
        controller: 'LoginCtrl'
      })
      .state('app.verify', {
        url: '/verify?token&id',
        templateUrl: 'views/verify.html',
        controller: 'VerifyCtrl'
      })
      .state('app.logout', {
        url: '/logout',
        template: null,
        controller: 'LogoutCtrl'
      })
      .state('app.signup', {
        url: '/signup',
        templateUrl: 'views/signup.html',
        controller: 'SignupCtrl'
      })


    // Form
    .state('app.form', {
      url: '/form/:id',
      views: {
        '': {
          templateUrl: 'views/restricted/form/form.html',
          controller: 'FormCtrl',
          resolve: {
            verified: ['$q', '$location', '$auth', 'jwtHelper', AuthServiceProvider.$get().verified]
          }
        },
        '@app.form': {
          templateUrl: 'views/restricted/form/step1.html',
          controller: 'Step1Ctrl'
        }
      },
    })

    .state('app.form.step2', {
        templateUrl: 'views/restricted/form/step2.html',
        controller: 'Step2Ctrl'
      })
      .state('app.form.step3', {
        templateUrl: 'views/restricted/form/step3.html',
        controller: 'Step3Ctrl'
      })
      .state('app.form.result', {
        templateUrl: 'views/restricted/form/result.html',
        controller: 'ResultCtrl'
      })

    .state('app.session', {
      url: '/session/:id',
      templateUrl: 'views/restricted/session.html',
      controller: 'RestrictedSessionCtrl',
      resolve: {
        verified: ['$q', '$location', '$auth', 'jwtHelper', AuthServiceProvider.$get().verified]
      }
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
  .config(['$authProvider', function($authProvider) {
    $authProvider.google({
      clientId: '891245656445-7djmtfr3c4fo3giuc7t89sgee0co4vjh.apps.googleusercontent.com'
    });

    $authProvider.github({
      clientId: 'c21492ca67471ebdfdd7'
    });

  }])
  .run(['AuthService', '$http', function(AuthService, $http) {
    AuthService.init();
    $http.defaults.xsrfHeaderName = 'X-CSRF-TOKEN';
    $http.defaults.xsrfCookieName = 'CSRF-TOKEN';
  }]);