'use strict';

angular.module('CallForPaper')
	.controller('RestrictedSessionCtrl', ['$scope', '$stateParams', '$filter', 'RestrictedSession', function($scope, $stateParams, $filter, RestrictedSession) {
		$scope.session = null;
		RestrictedSession.get({
			id: $stateParams.id
		}).$promise.then(function(sessionTmp) {
			$scope.session = sessionTmp;
			$scope.session.socialLinks = [];
			if(sessionTmp.social !== null) {
				var links = sessionTmp.social.split(',').map(function(value){
					return $filter('createLinks')(value);
				})
				$scope.session.socialLinks = links;
			}
			$scope.session.keyDifficulty = (['beginner', 'confirmed', 'expert'])[sessionTmp.difficulty - 1];
		});
	}]);