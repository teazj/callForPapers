'use strict';

angular.module('CallForPaper')
	.controller('SessionCtrl', function($scope, $stateParams, Session, Comment, Rate) {
		$scope.session = null;
		Session.get({
			id: $stateParams.id
		}, function(sessionTmp) {
			$scope.session = sessionTmp;
		});
		$scope.difficulties = ["Débutant", "Confirmé", "Expert"];

		var updateComments = function() {
			Comment.getByRowId({
				rowId: $stateParams.id
			}, function(commentsTmp) {
				$scope.comments = commentsTmp;
			})
		}
		updateComments();

		$scope.commentButtonDisabled = false;
		$scope.postComment = function() {
			$scope.commentButtonDisabled = true;
			Comment.save({
				'comment': $scope.commentMsg,
				'userId': 5629499534213120,
				'rowId': $stateParams.id
			}, function(c) {
				$scope.commentMsg = "";
				$scope.commentButtonDisabled = false;
				updateComments();
			});
		}

		var updateRates = function() {
			Rate.getByRowId({
				'rowId': $stateParams.id
			}, function(ratesTmp) {
				$scope.rates = ratesTmp;
				$scope.mean = $scope.rates.map(function(rateTmp) {
					return rateTmp.rate;
				}).reduce(function(x, y) {
					return x + y;
				}, 0) / ($scope.rates.length == 0 ? 1 : $scope.rates.length);
			})
		}
		updateRates();

		$scope.yourRate = {
			rate: 0,
			id: undefined
		};
		Rate.getByRowIdAndUserId({
			'rowId': $stateParams.id,
			'userId': 4573968371548160
		}, function(rateTmp) {
			if (rateTmp.id !== undefined) {
				$scope.yourRate = rateTmp;
			}
		})

		$scope.ratetButtonDisabled = false;
		$scope.postRate = function() {
			$scope.ratetButtonDisabled = true;
			if ($scope.yourRate.id === undefined) {
				Rate.save({
					'rate': $scope.yourRate.rate,
					'userId': 4573968371548160,
					'rowId': $stateParams.id
				}, function(c) {
					$scope.yourRate.id = c.id;
					$scope.commentButtonDisabled = false;
					updateRates();
				});
			} else {
				Rate.update({
					'id': $scope.yourRate.id
				}, {
					'rate': $scope.yourRate.rate,
					'userId': 4573968371548160,
					'rowId': $stateParams.id
				}, function(c) {
					$scope.commentButtonDisabled = false;
					updateRates();
				});
			}
		}
	});