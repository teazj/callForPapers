'use strict';

angular.module('CallForPaper')
	.controller('FormCtrl', ['$scope', '$filter', '$translate', 'RestrictedSession', '$state', function($scope, $filter, $translate, RestrictedSession, Application, $state) {
		// we will store all of our form data in this object
		$scope.formData = {};
		$scope.formData.steps = {};
		$scope.formData.steps.currentStep = 1;
		$scope.formData.sending = false;
		$scope.formData.steps.isValid = [false, false, false];
		$scope.language = $translate.use();

		$scope.formData.speaker = {};
		$scope.formData.speaker.phone = "";
		$scope.formData.session = {};
 		$scope.formData.help = {};
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

			RestrictedSession.save(model, function(success) {
				$scope.formData.sending = false;
				$state.go('form.result');
			}, function(error) {
				$scope.formData.sending = false;
				$scope.sendError = true;
			});
		};

		/**
		 * Update difficulty label when over
		 */
		$scope.hoverDifficulty = function(value) {
			$scope.formData.session.difficultyLabel = ([$filter('translate')('step2.beginner'), $filter('translate')('step2.confirmed'), $filter('translate')('step2.expert')])[value - 1];
		};
	}]);