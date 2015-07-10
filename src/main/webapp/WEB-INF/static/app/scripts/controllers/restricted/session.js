'use strict';

angular.module('CallForPaper')
	.controller('RestrictedSessionCtrl', ['$scope', '$stateParams', '$filter', 'RestrictedSession', 'CommonProfilImage', function($scope, $stateParams, $filter, RestrictedSession, CommonProfilImage) {
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
			if(sessionTmp.twitter !== null) $scope.session.twitter = $filter('createLinks')(sessionTmp.twitter);
			if(sessionTmp.googlePlus !== null) $scope.session.googlePlus = $filter('createLinks')(sessionTmp.googlePlus);
			if(sessionTmp.github !== null) $scope.session.github = $filter('createLinks')(sessionTmp.github);
			$scope.session.keyDifficulty = (['beginner', 'confirmed', 'expert'])[sessionTmp.difficulty - 1];

			CommonProfilImage.get({id : $scope.session.userId}).$promise.then(function(imgUriTmp) {
				$scope.session.profilImageUrl = imgUriTmp.uri;
			});
		});
	}]);