'use strict';

angular.module('CallForPaper').factory('Drafts', function(Restangular) {
    var Drafts = Restangular.service('restricted/drafts');

    Drafts.get = function get(id) {
        return Drafts.one(id).get();
    };

    Drafts.delete = function deleteDraft(id) {
        return Drafts.one(id).delete();
    };

    Drafts.save = function save(draft) {
        if (draft.id) {
            return draft.put();
        } else {
            return Drafts.post(draft);
        }
    };

    return Drafts;
});
