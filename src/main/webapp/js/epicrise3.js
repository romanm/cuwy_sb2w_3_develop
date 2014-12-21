
var historyFile = "/hol/history_id_" + parameters.hid;

cuwyApp.controller('EpicriseCtrl', [ '$scope', '$http', '$filter', '$sce', function ($scope, $http, $filter, $sce) {

	console.log("EpicriseCtrl");
	$scope.epicriseTemplate = epicriseTemplate;
	$scope.dt = new Date();
	$scope.epicrise = {};
	console.log($scope.epicriseTemplate);

	initDeclareController($scope, $http, $sce, $filter);

	$http({ method : 'GET', url : historyFile
	}).success(function(data, status, headers, config) {
		$scope.patientHistory = data;
		initEpicrise();
	}).error(function(data, status, headers, config) {
	});

	initEpicrise = function(){
		$scope.patientHistory.historyTreatmentAnalysises.forEach(function(hol1Element) {
			$scope.epicrise[hol1Element.historyTreatmentAnalysisName] = {};
			$scope.epicrise[hol1Element.historyTreatmentAnalysisName].historyTreatmentAnalysisDatetime = new Date();
			initFromHol1($scope.epicrise[hol1Element.historyTreatmentAnalysisName], hol1Element);
		});
		$scope.epicrise.patientHistory = $scope.patientHistory;
		console.log($scope.epicrise);
	}

	initFromHol1 = function(epicriseElement, hol1Element){
		var textHol1 = hol1Element.historyTreatmentAnalysisText;
		var element = angular.element(textHol1);
		var trs = element.find("td.name");
		if(trs.length > 0){
			var laborValues = {};
			for(i = 0; i < trs.length; i++){
				var nameTd = angular.element(trs[i]);
				var valueTd = nameTd.next();
				var unitTd = valueTd.next();
				if(valueTd.text().trim().length > 0){
					laborValues[nameTd.text()] = {value:valueTd.text(), unit:unitTd.text()};
				}
			}
			epicriseElement.laborValues = laborValues;
			hol1Element.historyTreatmentAnalysisText = null;
		}else{
			epicriseElement.textHtml = textHol1;
			if(textHol1 == "") {
				epicriseElement.textHtml = " &nbsp; ";
			}
		}
	}

	$scope.openLaborToEdit = function(laborBlock, laborName){
		laborBlock.laborOpenToEdit=laborName;
		if(!laborBlock.laborValues[laborName])
		laborBlock.laborValues[laborName] = {value:""};
		console.log($('#'+laborName));
		//not work
		$('#'+laborName).focus();
		console.log(laborBlock);
	}

	$scope.changeBlockDate = function(dt, h1Index){
		var epicriseBlockElement = $scope.epicrise[$scope.epicriseTemplate.head1s[h1Index].name];
		epicriseBlockElement.historyTreatmentAnalysisDatetime=dt;
	}

	$scope.saveWorkDoc = function(){
		console.log("-----------------");
		saveWorkDoc("/save/epicrise", $scope, $http);
		console.log("-----------------");
	}
	$scope.editOpenClose = function(h1Index){
		var head1s = $scope.epicriseTemplate.head1s;
		if(!$scope.epicrise[head1s[h1Index].name]) {
			$scope.epicrise[head1s[h1Index].name] = {textHtml : " &nbsp; "};
			var head = head1s[h1Index];
			if(head.isOnDemand){
				$scope.epicrise[head1s[h1Index].name].isOnDemand = true;
			}
		}
		var element = angular.element($scope.epicrise[head1s[h1Index].name].textHol1);
		var trs = element.find("td.name");
		var oldOpen = head1s[h1Index].open;
		epicriseTemplate.head1s.forEach(function(o) {
			o.open = false;
		});
		head1s[h1Index].open = !oldOpen;
		if(!head1s[h1Index].open )
		{
			for(k in $scope.epicrise[head1s[h1Index].name].laborValues)
				if($scope.epicrise[head1s[h1Index].name].laborValues[k].value == "")
					delete $scope.epicrise[head1s[h1Index].name].laborValues[k];
		}
	}

} ]);
