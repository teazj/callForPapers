'use strict';

angular.module('CallForPaper')
	.controller('AutorizeCtrl', ['$scope', '$auth', function($scope, $auth) {
		$scope.configure = function(provider) {
			try {
				$auth.link(provider)
			} catch (e) {}
		};
	}]);