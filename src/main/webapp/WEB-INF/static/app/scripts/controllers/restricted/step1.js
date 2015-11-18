'use strict';

angular.module('CallForPaper')
    .controller('Step1Ctrl', ['$scope', '$state', function($scope, $state) {
        $scope.$watch(function() {
            return $scope.form.email.$valid && $scope.form.name.$valid && $scope.form.firstname.$valid && ($scope.form.phone.$valid || $scope.$parent.formData.speaker.phone == "") && $scope.form.company.$valid && $scope.form.bio.$valid && $scope.form.social.$valid && $scope.form.twitter.$valid && $scope.form.googlePlus.$valid && $scope.form.github.$valid;
        }, function(isValid) {
            $scope.$parent.formData.steps.isValid[0] = isValid;
        })

        $scope.$watch(function() {
            if ($scope.$parent.formData.speaker.socialArray !== undefined)
                return $scope.$parent.formData.speaker.socialArray.length;
        }, function() {
            if ($scope.$parent.formData.speaker.socialArray !== undefined) {
                $scope.$parent.formData.speaker.social = $scope.$parent.formData.speaker.socialArray.map(function(elem) {
                    return elem.text;
                }).join(", ");
            }
        })

        $scope.verify = false;
        $scope.doVerify = function() {
            $scope.verify = true;
            if ($scope.$parent.formData.steps.isValid[0]) {
                $state.go('app.form.step2');
            }
        }

        /**
         * remove selected img, then current img, then social pimg
         * @return {[type]} [description]
         */
        $scope.removeImage = function() {
            if ($scope.$parent.formData.speaker.files && $scope.$parent.formData.speaker.files.length) {
                $scope.$parent.formData.speaker.files = [];
            } else if ($scope.$parent.formData.speaker.imageProfilKey) {
                $scope.$parent.formData.speaker.imageProfilKey = null;
            } else if ($scope.$parent.formData.speaker.socialProfilImageUrl) {
                $scope.$parent.formData.speaker.socialProfilImageUrl = null;
            }
        }

    }]);
