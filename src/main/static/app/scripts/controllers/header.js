'use strict';

angular.module('CallForPaper').controller('HeaderCtrl', function($scope, $rootScope, $translate, $auth, config) {
    this.language = $translate.use();

    this.title = config.eventName;
    this.releaseDate = config.releaseDate;
    this.decisionDate = config.decisionDate;

    this.changeLanguage = function(key) {
        $translate.use(key);
    };

    this.navBarColorClass = 'navbar-black'; // TODO Pretty dirty…

    $rootScope.$on('$translateChangeEnd', function(event, args) {
        this.language = args.language;
    }.bind(this));

    this.isAuthenticated = $auth.isAuthenticated();
    $scope.$on('authenticate', function() {
        this.isAuthenticated = $auth.isAuthenticated();
    }.bind(this));

});
