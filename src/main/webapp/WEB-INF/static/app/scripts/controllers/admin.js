'use strict';

angular.module('CallForPaper')
	.controller('AdminCtrl', function($scope, $rootScope, $translate) {
		$scope.language = $translate.use();

		$scope.changeLanguage = function(key) {
			$translate.use(key);
		};

		$rootScope.$on('$translateChangeEnd', function(event, args) {
			$scope.language = args.language;
		});
	});