'use strict';

angular.module('CallForPaper')
	.controller('Step1Ctrl', function($scope) {
	  $scope.$parent.formData.speaker = {};
	  $scope.$parent.formData.speaker.phone = "";
		$scope.$watch(function(){
			return $scope.form.email.$valid && $scope.form.name.$valid && $scope.form.firstname.$valid && ($scope.form.phone.$valid || $scope.$parent.formData.speaker.phone == "") && $scope.form.company.$valid && $scope.form.bio.$valid && $scope.form.social.$valid;
		},function(isValid){
			$scope.$parent.formData.steps.isValid[0]= isValid;
		})

		$scope.$watch(function(){
			if($scope.$parent.formData.speaker.socialArray !== undefined)
			return $scope.$parent.formData.speaker.socialArray.length;
		},function(){
			if($scope.$parent.formData.speaker.socialArray !== undefined)
			{
				$scope.$parent.formData.speaker.social = $scope.$parent.formData.speaker.socialArray.map(function(elem){return elem.text;}).join(", ");
			}
		})
	});
