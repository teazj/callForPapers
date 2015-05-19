'use strict';

angular.module('CallForPaper')
  .controller('SignupCtrl',['$scope', '$auth', function($scope, $auth) {
    $scope.alreadyExists = false;
    $scope.signup = function() {
      $auth.signup({
        email: $scope.email,
        password: $scope.password
      }).catch(function(response) {
        if(response.status === 409)
        {
            $scope.alreadyExists = true;
        }
      });
    };
  }]);