'use strict';

angular.module('CallForPaper').factory('RestrictedContact', ['$resource', function($resource) {
    return $resource('api/restricted/sessions/:id/contacts', {},
        {
            getByRowId: {
                url: 'api/restricted/sessions/contacts/row/:rowId',
                method: 'GET',
                isArray: true
            },
            update: {method: 'PUT'}
        });
}]);
