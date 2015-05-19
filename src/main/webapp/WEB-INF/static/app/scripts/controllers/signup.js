'use strict';

angular.module('CallForPaper')
  .controller('SignupCtrl', ['$scope', '$auth', function($scope, $auth) {
    $scope.alreadyExists = false;
    $scope.loading = false;
    $scope.signup = function() {
      $scope.loading = true;
      $auth.signup({
        email: $scope.email,
        password: $scope.password
      }).then(function() {
        $scope.loading = false;
      }).catch(function(response) {
        $scope.loading = false;
        if (response.status === 409) {
          $scope.alreadyExists = true;
        }
      });
    };
  }]);