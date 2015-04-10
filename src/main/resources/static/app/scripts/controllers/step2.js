'use strict';

angular.module('CallForPaper')
	.controller('Step2Ctrl', function($scope) {
		$scope.$watch(function(){
			return $scope.form.sessionName.$valid && $scope.form.description.$valid && $scope.form.references.$valid && ($scope.$parent.formData.session.difficulty > 0) && $scope.form.track.$valid && $scope.form.coSpeaker.$valid;
		},function(isValid){
			$scope.$parent.formData.steps.isValid[1]= isValid;
		})

		$scope.$watch(function(){ 
			if($scope.$parent.formData.session.coSpeakerArray !== undefined)
			return $scope.$parent.formData.session.coSpeakerArray.length;
		},function(){
			if($scope.$parent.formData.session.coSpeakerArray !== undefined)
			{
				$scope.$parent.formData.session.coSpeaker = $scope.$parent.formData.session.coSpeakerArray.map(function(elem){return elem.text;}).join(", ");
			}
		})
	});