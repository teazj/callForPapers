'use strict';

angular.module('CallForPaper')
	.controller('FormCtrl', function($scope, $filter, $translate,$rootScope, $http, $state, $stateParams) {
		// we will store all of our form data in this object
		$scope.formData = {};
		$scope.formData.steps = {};
		$scope.formData.steps.currentStep = 1;
		$scope.formData.sending = false;
		$scope.formData.steps.isValid = [false, false, false];
		$scope.language = $translate.use();
		// function to process the form

		$scope.processForm = function(isValid) {
			$scope.formData.sending = true;
			var model = {};
			angular.extend(model,$scope.formData.help);
			angular.extend(model,$scope.formData.speaker);
			angular.extend(model,$scope.formData.session);
			var eventName = $stateParams.event;

			$http.post('/' + eventName + '/session', model).
			success(function(data, status, headers, config) {
				// this callback will be called asynchronously
				// when the response is available
				$scope.formData.sending = false;
				$state.go('form.result');
			}).
			error(function(data, status, headers, config) {
				// called asynchronously if an error occurs
				// or server returns response with an error status.
				$scope.formData.sending = false;
				$scope.sendError = true;
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
