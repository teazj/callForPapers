'use strict';

angular.module('CallForPaper')
	.controller('SessionCtrl', function($scope, $stateParams, Session) {
		$scope.session = null;
		Session.get({ id: $stateParams.id}, function(sessionTmp) {
			console.log(sessionTmp);
			$scope.session = sessionTmp;
		});
		$scope.difficulties = ["Débutant","Confirmé","Expert"];
	});