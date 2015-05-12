'use strict';

angular.module('CallForPaper')
	.controller('Step3Ctrl', ['$scope', function($scope) {
	  $scope.$parent.formData.help = {};
		$scope.$watch(function(){
 			var financial = $scope.$parent.formData.help.financial;
 			var hotel = $scope.$parent.formData.help.hotel;
 			var hotelDate = $scope.form.hotelDate.$valid;
 			var travel = $scope.$parent.formData.help.travel;
 			var travelFrom = $scope.form.travelFrom.$valid;
 			if(financial === "true")
 			{
 				if(!hotel && !travel)
 				{
 					return false;
 				}
 				else
 				{
 					return !(hotel && !hotelDate) && !(travel && !travelFrom);
 				}
 			}
 			else if(financial === "false")
 			{
 				return true;
 			}
 			else
 			{
 				return false;
 			}
		},function(isValid){
			$scope.$parent.formData.steps.isValid[2]= isValid;
		})
	}]);
