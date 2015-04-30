'use strict';

angular.module('CallForPaper')
	.controller('AdminCtrl', function($scope, $translate) {
		$scope.language = $translate.use();

		$scope.changeLanguage = function (key) {
			$translate.use(key);
  		};
	});
