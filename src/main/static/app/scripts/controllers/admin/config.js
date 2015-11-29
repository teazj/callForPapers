'use strict';

angular.module('CallForPaper')
    .controller('AdminConfigCtrl', ['$scope','$filter', '$http', 'Application',
        function($scope,$filter, $http, Application) {

            $scope.submission = false;
            Application.get(function(config) {
                $scope.submission = config.open;
                $scope.config = config;
                $scope.config.start = $scope.toDate(config.date);
                $scope.config.release = $scope.toDate(config.releaseDate);
                $scope.config.decision = $scope.toDate(config.decisionDate);
            });

            $scope.toDate = function(stringDate){
                 var datePartials = stringDate.split("/");
                 return new Date(datePartials[2], datePartials[1] - 1, datePartials[0]);
            }

            /**
             * Enable or disable submission of new talks
             * @param  {boolean} enable or disable submissions
             * @return {void}
             */
            $scope.toggleSubmit = function(value) {
                $http.post('/api/admin/config/enableSubmissions', {
                    key: value
                }).then(function() {
                }, function() {
                    $scope.submission = !value;
                });
            };


            $scope.saveConfig = function(){
                $scope.config.date = $filter('date')($scope.config.start,'dd/MM/yyyy');
                $scope.config.releaseDate = $filter('date')($scope.config.release,'dd/MM/yyyy');
                $scope.config.decisionDate = $filter('date')($scope.config.decision,'dd/MM/yyyy');
                $http.post('/api/application', $scope.config).then(function() {
                }, function() {
                });

            };
        }
    ]);
