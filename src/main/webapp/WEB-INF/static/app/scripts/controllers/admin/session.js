'use strict';

angular.module('CallForPaper')
	.controller('AdminSessionCtrl', ['$scope', '$stateParams', '$filter', '$translate', 'AdminSession', 'AdminComment', 'AdminRate', '$modal', '$state', 'CommonProfilImage', 'AuthService', function($scope, $stateParams, $filter, $translate, AdminSession, AdminComment, AdminRate, $modal, $state, CommonProfilImage, AuthService) {
		$scope.session = null;
		$scope.adminEmail = null;
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
			if(sessionTmp.twitter !== null) $scope.session.twitter = $filter('createLinks')(sessionTmp.twitter);
			if(sessionTmp.googlePlus !== null) $scope.session.googlePlus = $filter('createLinks')(sessionTmp.googlePlus);
			if(sessionTmp.github !== null) $scope.session.github = $filter('createLinks')(sessionTmp.github);
			$scope.session.keyDifficulty = (['beginner', 'confirmed', 'expert'])[sessionTmp.difficulty - 1];

			CommonProfilImage.get({id : $scope.session.userId}).$promise.then(function(imgUriTmp) {
				$scope.session.profilImageUrl = imgUriTmp.uri;
			});
		});

		AuthService.getCurrentUser().then(function(userInfo){
			$scope.adminEmail = userInfo.email;
		})

		AdminSession.getIds().$promise.then(function(idsTmp){
			var index = idsTmp.indexOf(parseInt($stateParams.id,10));
			if(index !== -1) {
				if(index > 0) $scope.previous = idsTmp[index - 1];
				if(index < idsTmp.length - 1) $scope.next = idsTmp[index + 1];
			}
		})
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
			}, function(c) {
				$scope.commentButtonDisabled = false;
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
				var votedCount = ratesTmp.reduce(function(x, y){
					var i = y.rate !== 0 ? 1 : 0;
					return i + x;
				},0);
				$scope.mean = ratesTmp.reduce(function(x, y) {
					return y.rate + x;
				}, 0) / (votedCount == 0 ? 1 : votedCount);

				AuthService.getCurrentUser().then(function(userInfo){
					$scope.rates = ratesTmp.filter(function(element){
						return element.user.email !== userInfo.email;
					})
				}, function(err){
					$scope.rates = ratesTmp;
				})
			})
		}
		updateRates();

		$scope.yourRate = {
			rate: 0,
			hate: false,
			love: false,
			id: undefined
		};
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
				if($scope.yourRate.hate || $scope.yourRate.love) $scope.changed = true;
				$scope.hate = $scope.yourRate.hate;
				$scope.love = $scope.yourRate.love;
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
					'hate': $scope.yourRate.hate,
					'love': $scope.yourRate.love,
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
					'hate': $scope.yourRate.hate,
					'love': $scope.yourRate.love,
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

		$scope.changed = false;
		/**
		 * Reset all other checkbox and vote
		 * @return {void}
		 */
		$scope.handleNoVote = function() {
			$scope.changed = true;
			if($scope.noVote === true) {
				$scope.yourRate.rate = 0;
				$scope.yourRate.hate = false;
				$scope.yourRate.love = false;
				$scope.hate = false;
				$scope.love = false;
			} else {
				$scope.yourRate.rate = 0;
				$scope.noVote = false
				$scope.hate = false;
				$scope.love = false;
			}
		}

		/**
		 * Reset all other checkbox and vote 1
		 * @return {void}
		 */
		$scope.handleHate = function() {
			$scope.changed = true;
			if($scope.hate === true) {
				$scope.yourRate.rate = 1;
				$scope.yourRate.hate = true;
				$scope.yourRate.love = false;
				$scope.love = false;
				$scope.noVote = false
			} else {
				$scope.yourRate.rate = 0;
				$scope.noVote = false
				$scope.hate = false;
				$scope.love = false;
			}
		}

		/**
		 * Reset all other checkbox and vote 4
		 * @return {void}
		 */
		$scope.handleLove = function() {
			$scope.changed = true;
			if($scope.love === true) {
				$scope.yourRate.rate = 5;
				$scope.yourRate.hate = false;
				$scope.yourRate.love = true;
				$scope.hate = false;
				$scope.noVote = false
			} else {
				$scope.yourRate.rate = 0;
				$scope.noVote = false
				$scope.hate = false;
				$scope.love = false;
			}
		}

		/**
		 * Reset checkbox on vote
		 * @return {void}
		 */
		$scope.$watch(function(){
			return $scope.yourRate.rate;
		},function(rate){
			if($scope.yourRate.rate !== 0 && !$scope.changed) {
				$scope.changed = false;
				$scope.noVote = false;
				$scope.yourRate.hate = false;
				$scope.hate = false;
				$scope.yourRate.love = false;
				$scope.love = false;
			}
			$scope.changed = false;
		})
	}])