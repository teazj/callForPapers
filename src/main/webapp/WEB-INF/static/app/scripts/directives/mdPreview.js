'use strict';

angular.module('mdPreview', [])
.directive('mdPreview',['$modal', function($modal) { 
  return {
    restrict: 'A',
    scope: {
      preview: '=mdPreview'
    },
    link: function(scope, element, attr) {
      element.on('click',function(){
        var modalInstance = $modal.open({
          animation: true,
          templateUrl: 'MdModalContent',
          controller: 'MdPreviewModalInstanceCtrl',
          resolve: {
            content: function () {
              return scope.preview
            }
          }
        });
      });
    }
  };
}])
.controller('MdPreviewModalInstanceCtrl', function ($scope, $modalInstance, content) {
  $scope.content = content;
  $scope.ok = function () {
    $modalInstance.close();
  };
}) 
.run(["$templateCache", function($templateCache) {
    $templateCache.put('MdModalContent', "<div class=\"modal-header\"><h3 class=\"modal-title\">{{\'previewModal.title\' | translate}}<\/h3><\/div><div class=\"modal-body\"><div marked=\"content\"><\/div><\/div><div class=\"modal-footer\"><button class=\"btn btn-primary\" ng-click=\"ok()\">{{\'previewModal.confirm\' | translate}}<\/button><\/div>");
}]); 