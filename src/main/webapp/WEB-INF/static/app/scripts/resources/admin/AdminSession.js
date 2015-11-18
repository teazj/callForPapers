angular.module('CallForPaper').factory('AdminSession', ['resourceRetries', function(resourceRetries) {
    return resourceRetries('api/admin/sessions/:id', null,
        {
            update: {
                method: 'PUT',
                url: 'api/admin/sessions/:id'
            },
            delete: {
                method: 'DELETE',
                url: 'api/admin/sessions/:id'
            },
            setViewed: {
                method: 'POST',
                url: 'api/admin/sessions/viewed/:id',
            },
            changeTrack: {
                method: 'PUT',
                url: 'api/admin/sessions/track/:id'
            }
        });
}]);
