'use strict';

angular.module('CallForPaper')
	.controller('SessionCtrl', function($scope, $stateParams, Session, Comment) {
		$scope.session = null;
		Session.get({ id: $stateParams.id}, function(sessionTmp) {
			$scope.session = sessionTmp;
		});
		$scope.difficulties = ["Débutant","Confirmé","Expert"];

		Comment.getByRowId({rowId : $stateParams.id},function(commentsTmp){
			$scope.comments = commentsTmp;
		})

		$scope.commentButtonDisabled = false;
		$scope.postComment = function(){
			$scope.commentButtonDisabled = true;
			Comment.save({"comment": $scope.commentMsg , "userId" : 6544293208522752, "rowId" : $stateParams.id}, function(c){
				$scope.comments.push(c);
				$scope.commentMsg = "";
				$scope.commentButtonDisabled = false;
			});
		}
	});