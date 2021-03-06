(function() {
    'use strict';

    angular
        .module('wordsearchApp')
//        .filter('highlight', HightLightFilter)
        .controller('WordController', WordController);

//    HightLightFilter.$inject = ['$scope'];
//
//    function HightLightFilter ($scope) {
//        return function(text, phrase) {
//            if (phrase) {
//                text = text.replace(new RegExp('('+phrase+')', 'gi'), '<span class="highlighted">$1</span>')
//            }
//
//            return $scope.trustAsHtml(text)
//        }
//    }

    WordController.$inject = ['$scope', '$state', 'Word', 'WordSearch', 'ParseLinks', 'AlertService'];

    function WordController ($scope, $state, Word, WordSearch, ParseLinks, AlertService) {
        var vm = this;

        vm.words = [];
        vm.loadPage = loadPage;
        vm.page = 0;
        vm.links = {
            last: 0
        };
        vm.predicate = 'id';
        vm.reset = reset;
        vm.reverse = true;
        vm.clear = clear;
        vm.loadAll = loadAll;
        vm.search = search;

        loadAll();

        function loadAll () {
            if (vm.currentSearch) {
                WordSearch.query({
                    query: vm.currentSearch,
                    page: vm.page,
                    size: 20,
                    sort: sort()
                }, onSuccess, onError);
            } else {
                Word.query({
                    page: vm.page,
                    size: 20,
                    sort: sort()
                }, onSuccess, onError);
            }
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }

            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                for (var i = 0; i < data.length; i++) {
                    vm.words.push(data[i]);
                }
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function reset () {
            vm.page = 0;
            vm.words = [];
            loadAll();
        }

        function loadPage(page) {
            vm.page = page;
            loadAll();
        }

        function clear () {
            vm.words = [];
            vm.links = {
                last: 0
            };
            vm.page = 0;
            vm.predicate = 'id';
            vm.reverse = true;
            vm.searchQuery = null;
            vm.currentSearch = null;
            vm.loadAll();
        }

        function search (searchQuery) {
            if (!searchQuery){
                return vm.clear();
            }
            vm.words = [];
            vm.links = {
                last: 0
            };
            vm.page = 0;
            vm.predicate = '_score';
            vm.reverse = false;
            vm.currentSearch = searchQuery;
            vm.loadAll();
        }

//        function highlight(text) {
//            if (!vm.currentSearch || vm.currentSearch === '') {
//                return $scope.trustAsHtml(text);
//            }
//            return $scope.trustAsHtml(text.replace(new RegExp(search, 'gi'), '<span class="highlighted">$&</span>'));
//        };
    }
})();
