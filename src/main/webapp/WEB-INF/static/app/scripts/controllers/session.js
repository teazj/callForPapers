'use strict';

angular.module('CallForPaper')
	.controller('SessionCtrl', ['$scope', '$stateParams', '$filter', '$translate', 'Session', 'Comment', 'Rate', function($scope, $stateParams, $filter, $translate,Session, Comment, Rate) {
		$scope.session = null;
		Session.get({
			id: $stateParams.id
		}, function(sessionTmp) {
			$scope.session = sessionTmp;
		});
		$scope.difficulties = [$filter('translate')('step2.beginner'), $filter('translate')('step2.confirmed'), $filter('translate')('step2.expert')];

		/**
		 * get comments of the session
		 * @return {[Comment]}
		 */
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
				'rowId': $stateParams.id
			}, function(c) {
				$scope.commentMsg = "";
				$scope.commentButtonDisabled = false;
				updateComments();
			});
		}

		/**
		 * get rates of the session
		 * @return {[Rate]}
		 */
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
		/**
		 * Obtain current user rate
		 * @param  {long : rowId}
		 * @return {Rate}
		 */
		Rate.getByRowIdAndUserId({
			'rowId': $stateParams.id,
		}, function(rateTmp) {
			if (rateTmp.id !== undefined) {
				$scope.yourRate = rateTmp;
			}
		})

		$scope.rateButtonDisabled = false;
		/*
		 *	Post new rate
		 */
		$scope.postRate = function() {
			$scope.rateButtonDisabled = true;
			if ($scope.yourRate.id === undefined) {
				Rate.save({
					'rate': $scope.yourRate.rate,
					'rowId': $stateParams.id
				}, function(c) {
					$scope.yourRate.id = c.id;
					$scope.rateButtonDisabled = false;
					updateRates();
				});
			} else {
				Rate.update({
					'id': $scope.yourRate.id
				}, {
					'rate': $scope.yourRate.rate,
					'rowId': $stateParams.id
				}, function(c) {
					$scope.rateButtonDisabled = false;
					updateRates();
				});
			}
		}
	}]);