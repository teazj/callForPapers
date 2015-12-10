'use strict';

angular.module('CallForPaper').provider('ProfileValidator', function() {

    function isValid(user) {
        return Boolean(user.firstname && user.lastname && user.bio && user.email);
    }

    return {
        '$get': function() {
            return {
                isValid: isValid
            };
        },
        isValid: function() {
            return /*@ngInject*/ function(user, $q) {
                return isValid(user) ? $q.when(true) : $q.reject('profile.incomplete');
            };
        }
    };
});
