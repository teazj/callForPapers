'use strict';

angular.module('CallForPaper')
	.controller('FormCtrl', function($scope, $filter, $translate,$rootScope, $http) {
		// we will store all of our form data in this object
		$scope.formData = {};
		$scope.formData.steps = {};
		$scope.formData.steps.currentStep = 1;
		$scope.formData.steps.isValid = [false, false, false];
		$scope.language = $translate.use();
		// function to process the form

		$scope.processForm = function(isValid) {
			var model = {};
			angular.extend(model,$scope.formData.help);
			angular.extend(model,$scope.formData.speaker);
			angular.extend(model,$scope.formData.session);
			console.log(model);

			$http.post('devfest/session', model).
			success(function(data, status, headers, config) {
			// this callback will be called asynchronously
			// when the response is available
			console.log(data);
			}).
			error(function(data, status, headers, config) {
			// called asynchronously if an error occurs
			// or server returns response with an error status.
			console.log(data);
			});

		};

		$scope.changeLanguage = function (key) {
			$translate.use(key);
  		};

		$rootScope.$on('$translateChangeEnd', function (event, args) {
			$scope.language = args.language;
		});

		$scope.hoverDifficulty = function(value) {
			$scope.formData.session.difficultyLabel = ([$filter('translate')('step2.beginner'),$filter('translate')('step2.confirmed'),$filter('translate')('step2.expert')])[value - 1];
		};
	});
