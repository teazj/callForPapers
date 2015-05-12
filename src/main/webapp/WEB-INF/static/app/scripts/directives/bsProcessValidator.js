angular.module('bs-has', [])
  .factory('bsProcessValidator', ['$timeout', function($timeout) {
    return function(scope, element, ngClass, bsClass) {
      $timeout(function() {
        var input = element.find('input');
        if(!input.length) { input = element.find('select'); }
        if(!input.length) { input = element.find('textarea'); }
        if (input.length) {
            scope.$watch(function() {
                return input.hasClass(ngClass) && input.hasClass('ng-dirty');
            }, function(isValid) {
                element.toggleClass(bsClass, isValid);
            });
        }
      });
    };
  }])
  .directive('bsHasSuccess', ['bsProcessValidator', function(bsProcessValidator) {
    return {
      restrict: 'A',
      link: function(scope, element) {
        bsProcessValidator(scope, element, 'ng-valid', 'has-success');
      }
    };
  }])
  .directive('bsHasError', ['bsProcessValidator', function(bsProcessValidator) {
    return {
      restrict: 'A',
      link: function(scope, element) {
        bsProcessValidator(scope, element, 'ng-invalid', 'has-error');
      }
    };
  }])
  .directive('bsHas', ['bsProcessValidator', function(bsProcessValidator) {
    return {
      restrict: 'A',
      link: function(scope, element) {
        bsProcessValidator(scope, element, 'ng-valid', 'has-success');
        bsProcessValidator(scope, element, 'ng-invalid', 'has-error');
      }
    };
  }]);