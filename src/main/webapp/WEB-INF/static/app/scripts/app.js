'use strict';


var app = angular.module('CallForPaper', [
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
    'k8LanguagePicker'
  ])
  .config(function($stateProvider, $urlRouterProvider) {
    //delete $httpProvider.defaults.headers.common['X-Requested-With'];
    $urlRouterProvider
    .when('/event/:eventName/form', '/event/:eventName/form/step1')
    .when('/event/:eventName/form/', '/event/:eventName/form/step1')
    .when('/event/:eventName', '/event/:eventName/form/step1')
    .when('/event/:eventName/', '/event/:eventName/form/step1');
    $urlRouterProvider.otherwise('/404');
    $stateProvider
      // .state('index', {
      //   url: '/',
      //   templateUrl: 'views/main.html',
      //   controller: 'MainCtrl'
      // })
      .state('form', {
        url: '/event/:eventName/form',
        abstract: true,
        views : {
          '' : {
            templateUrl: 'views/form.html',
            controller: 'FormCtrl'
          },
          '@form' : {
            templateUrl: 'views/step1.html'
          }
        },
      })
      // nested states 
      // each of these sections will have their own view
      .state('form.step1', {
        url: '/step1',
        templateUrl: 'views/step1.html',
        controller: 'Step1Ctrl'
      })
      .state('form.step2', {
        url: '/step2',
        templateUrl: 'views/step2.html',
        controller: 'Step2Ctrl'
      })
      .state('form.step3', {
        url: '/step3',
        templateUrl: 'views/step3.html',
        controller: 'Step3Ctrl'
      })
      .state('form.result', {
        url: '/result',
        templateUrl: 'views/result.html',
        controller: 'ResultCtrl'
      })
      .state('close', {
        url: '/close',
        templateUrl: 'views/close.html'
      })
      .state('404', {
        url: '/404',
        templateUrl: '404.html'
      });
  })
.config(function(tagsInputConfigProvider) {
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
});