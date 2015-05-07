'use strict';

angular.module('CallForPaper')
	.controller('FormCtrl', function($scope, $filter, $translate, $rootScope, Session, Application, $state) {
		// we will store all of our form data in this object
		$scope.formData = {};
		$scope.formData.steps = {};
		$scope.formData.steps.currentStep = 1;
		$scope.formData.sending = false;
		$scope.formData.steps.isValid = [false, false, false];
		$scope.language = $translate.use();

		/**
		 * Send the form to the server
		 * @param  {Boolean}
		 * @return {void}
		 */
		$scope.processForm = function(isValid) {
			$scope.formData.sending = true;
			var model = {};
			angular.extend(model, $scope.formData.help);
			angular.extend(model, $scope.formData.speaker);
			angular.extend(model, $scope.formData.session);

			Session.save(model, function(success) {
				$scope.formData.sending = false;
				$state.go('form.result');
			}, function(error) {
				$scope.formData.sending = false;
				$scope.sendError = true;
			});
		};

		/**
		 * Get eventName
		 */
		Application.get(function(config) {
			$scope.title = config.eventName;
		})

		$scope.changeLanguage = function(key) {
			$translate.use(key);
		};

		$rootScope.$on('$translateChangeEnd', function(event, args) {
			$scope.language = args.language;
		});

		/**
		 * Update difficulty label when over
		 */
		$scope.hoverDifficulty = function(value) {
			$scope.formData.session.difficultyLabel = ([$filter('translate')('step2.beginner'), $filter('translate')('step2.confirmed'), $filter('translate')('step2.expert')])[value - 1];
		};
	});