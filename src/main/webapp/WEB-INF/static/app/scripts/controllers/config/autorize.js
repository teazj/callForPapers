'use strict';

angular.module('CallForPaper')
	.controller('AutorizeCtrl', ['$scope', '$auth', function($scope, $auth) {
		$scope.error = false;
		$scope.success = false;
		$scope.configure = function(provider) {
			$auth.autorize(provider).then(function(response)
			{
				if(response === false)
				{
					$scope.error = true;
					$scope.success = false;
				}
				else
				{
					$scope.error = false;
					$scope.success = true;
				}
			},function(){
				$scope.error = true;
				$scope.success = false;
			})
		};
	}]);