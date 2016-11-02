(function() {
    'use strict';

    angular
        .module('wordsearchApp')
        .controller('WordDetailController', WordDetailController);

    WordDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Word'];

    function WordDetailController($scope, $rootScope, $stateParams, previousState, entity, Word) {
        var vm = this;

        vm.word = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('wordsearchApp:wordUpdate', function(event, result) {
            vm.word = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
