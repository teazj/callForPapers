'use strict';

angular.module('CallForPaper').factory('Sessions', function(Restangular) {
    var Sessions = Restangular.service('restricted/sessions');

    Sessions.get = function get(id) {
        return Sessions.one(id).get();
    };

    Sessions.save = function save(session) {
        if (session.id) {
            return Sessions.one(session.id).customPUT(session); // "restangularized" session capabilities are not used because session object may be a draft
        } else {
            return Sessions.post(session);
        }
    };

    return Sessions;
});
