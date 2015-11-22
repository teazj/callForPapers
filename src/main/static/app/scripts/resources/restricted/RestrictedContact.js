'use strict';

angular.module('CallForPaper').factory('RestrictedContact', ['$resource', function($resource) {
    return $resource('api/restricted/sessions/:rowId/contacts', {},
        {
            getByRowId: {
                url: 'api/restricted/sessions/:rowId/contacts',
                method: 'GET',
                isArray: true
            },
            update: {method: 'PUT',
             url: 'api/restricted/sessions/:rowId/contacts/:id'
             }
        });
}]);
