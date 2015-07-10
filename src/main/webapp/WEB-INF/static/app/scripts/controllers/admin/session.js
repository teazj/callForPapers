'use strict';

angular.module('CallForPaper')
	.controller('AdminSessionCtrl', ['$scope', '$stateParams', '$filter', '$translate', 'AdminSession', 'AdminComment', 'AdminRate', '$modal', '$state', 'CommonProfilImage', function($scope, $stateParams, $filter, $translate, AdminSession, AdminComment, AdminRate, $modal, $state, CommonProfilImage) {
		$scope.session = null;
		AdminSession.get({
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

			CommonProfilImage.get({id : $scope.session.userId}).$promise.then(function(imgUriTmp) {
				$scope.session.profilImageUrl = imgUriTmp.uri;
			});
		});
		/**
		 * get comments of the session
		 * @return {[AdminComment]}
		 */
		var updateComments = function() {
			AdminComment.getByRowId({
				rowId: $stateParams.id
			}, function(commentsTmp) {
				$scope.comments = commentsTmp;
			})
		}
		updateComments();

		$scope.commentButtonDisabled = false;
		$scope.postComment = function() {
			$scope.commentButtonDisabled = true;
			AdminComment.save({
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
		 * @return {[AdminRate]}
		 */
		var updateRates = function() {
			AdminRate.getByRowId({
				'rowId': $stateParams.id
			}, function(ratesTmp) {
				$scope.rates = ratesTmp;
				var votedCount = $scope.rates.reduce(function(x, y){
					var i = y.rate !== 0 ? 1 : 0;
					return i + x;
				},0);
				$scope.mean = $scope.rates.reduce(function(x, y) {
					return y.rate + x;
				}, 0) / (votedCount == 0 ? 1 : votedCount);
			})
		}
		updateRates();

		$scope.yourRate = {
			rate: 0,
			id: undefined
		};

		$scope.vote = {
			noVote : false
		}
		/**
		 * Obtain current user rate
		 * @param  {long : rowId}
		 * @return {AdminRate}
		 */
		AdminRate.getByRowIdAndUserId({
			'rowId': $stateParams.id,
		}, function(rateTmp) {
			if (rateTmp.id !== undefined) {
				$scope.yourRate = rateTmp;
				if($scope.yourRate.rate === 0) {
					$scope.noVote = true;
				}
			}
		})

		$scope.rateButtonDisabled = false;
		/*
		 *	Post new rate
		 */
		$scope.postRate = function() {
			$scope.rateButtonDisabled = true;
			if ($scope.yourRate.id === undefined) {
				AdminRate.save({
					'rate': $scope.yourRate.rate,
					'rowId': $stateParams.id
				}, function(c) {
					$scope.yourRate.id = c.id;
					$scope.rateButtonDisabled = false;
					updateRates();
				});
			} else {
				AdminRate.update({
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

		/**
		 * Delete current session
		 * @return {void}
		 */
		$scope.deleteSession = function() {
			var modalInstance = $modal.open({
				animation: true,
				templateUrl: 'views/admin/modal.html',
				controller: 'ModalInstanceCtrl'
			});
			modalInstance.result.then(function() {
				AdminSession.delete({id : $stateParams.id}, function(){
					$state.go('admin.sessions');
				})
			}, function() {
				// cancel
			});
		}

		/**
		 * Reset vote on checkbox true
		 * @return {void}
		 */
		$scope.handleNoVote = function() {
			if($scope.noVote === true) {
				$scope.yourRate.rate = 0;
			}
		}

		/**
		 * Reset checkbox on vote
		 * @return {void}
		 */
		$scope.$watch(function(){
			return $scope.yourRate.rate;
		},function(rate){
			if($scope.yourRate.rate !== 0) {
				$scope.noVote = false;
			}
		})
	}])