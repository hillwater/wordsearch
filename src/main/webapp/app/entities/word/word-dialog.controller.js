(function() {
    'use strict';

    angular
        .module('wordsearchApp')
        .controller('WordDialogController', WordDialogController);

    WordDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Word'];

    function WordDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Word) {
        var vm = this;

        vm.word = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.word.id !== null) {
                Word.update(vm.word, onSaveSuccess, onSaveError);
            } else {
                Word.save(vm.word, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('wordsearchApp:wordUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
