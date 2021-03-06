
var historyFile = "/hol/history_id_" + parameters.hid;
var history2File = "/hol2/history_id_" + parameters.hid;

cuwyApp.controller('EpicriseCtrl', [ '$scope', '$http', '$filter', '$sce', function ($scope, $http, $filter, $sce) {

	console.log("EpicriseCtrl");
	$scope.epicriseTemplate = epicriseTemplate;
	$scope.seekTag = "";
	$scope.epicrise = {};
	$scope.dt = new Date();
//	console.log($scope.epicriseTemplate);

	initDeclareController($scope, $http, $sce, $filter);

	$http({ method : 'GET', url : history2File
	}).success(function(data, status, headers, config) {
		$scope.epicrise = data;
		$scope.patientHistory = $scope.epicrise.patientHistory;
		if(!$scope.patientHistory){
			$http({ method : 'GET', url : historyFile
			}).success(function(data, status, headers, config) {
				$scope.patientHistory = data;
			}).error(function(data, status, headers, config) {
			});
		}
	}).error(function(data, status, headers, config) {
		readHol1();
	});

	readHol1 = function(){
		$http({ method : 'GET', url : historyFile
		}).success(function(data, status, headers, config) {
			$scope.patientHistory = data;
			initEpicrise();
		}).error(function(data, status, headers, config) {
		});
	}
	initEpicrise = function(){
		$scope.epicrise.departmentHistoryOut = new Date();
		$scope.epicrise.epicriseGroups = [];
		var rsp = {name:"Рекомендовано/смерть/перевід"};
		$scope.patientHistory.historyTreatmentAnalysises.forEach(function(hol1Element) {
			var groupElement = createGroupElement(hol1Element.historyTreatmentAnalysisName);
			addTextHtmlValue (groupElement, hol1Element.historyTreatmentAnalysisText);
			if( rsp.name == groupElement.name){
				rsp = groupElement;
			}else{
				$scope.epicrise.epicriseGroups.push(groupElement);
			}
		});
		var op = $scope.epicriseTemplate.head1s[1];
		$scope.epicrise.epicriseGroups.splice(0,0,{name:op.name});
		var dz = $scope.epicriseTemplate.head1s[0];
		$scope.epicrise.epicriseGroups.splice(0,0,{name:dz.name});
		var addGroup = $scope.epicriseTemplate.head1s[2];
		$scope.epicrise.epicriseGroups.push(rsp);
		var addGroup = {name:"Лікування, обстеження, аналізи, рекомендації ...", type : 'isOnDemand'};
		$scope.epicrise.epicriseGroups.splice(2,0,addGroup);
	}

	addTextHtmlValue = function(groupElement, textHtmlValue){
		var value = {};
		value.historyTreatmentAnalysisDatetime = new Date();
		initFromHol1(value, textHtmlValue);
		groupElement.value = value;
	}

	createGroupElement = function(h1Name){
		var type="isTextHtml";
		if(epicriseTemplate.epicriseBlockConfig[h1Name]){
			if(epicriseTemplate.epicriseBlockConfig[h1Name].isLabor){
				var type="isLabor";
			}else
				if(epicriseTemplate.epicriseBlockConfig[h1Name].isOnDemand){
					var type="isOnDemand";
				}
		}
		return {name:h1Name, type:type};
	}

	initFromHol1 = function(epicriseElement, textHol1){
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
		}else{
			epicriseElement.textHtml = textHol1;
			if(textHol1 == "") {
				epicriseElement.textHtml = " &nbsp; ";
			}
		}
	}

	$scope.openLaborToEdit = function(h1, laborName){
		h1.laborOpenToEdit=laborName;
		if(!h1.value.laborValues[laborName])
		h1.value.laborValues[laborName] = {value:""};
		//not work
		$('#'+laborName).focus();
	}

	$scope.setSeekTag = function(tag){
		$scope.seekTag = tag;
		if("все" === tag) {
			$scope.seekTag = "";
		}
	}

	$scope.beetDays = function(){
		var outDay = $scope.epicrise.departmentHistoryOut;
		var outDate = new Date(outDay);
		if($scope.patientHistory){
			var inDay = $scope.patientHistory.patientDepartmentMovements[0].departmentHistoryIn;
			var beetDaysRaw = (outDate - inDay)/dayInMs;
//			var beetDaysRaw = (outDay - inDay)/dayInMs;
			return Math.round(beetDaysRaw);
		}
	}
	var dayInMs = 1000*60*60*24;
	$scope.changeBlockDate = function(dt, h1Index){
		var epicriseBlockElement = $scope.epicrise.epicriseGroups[h1Index];
		epicriseBlockElement.value.historyTreatmentAnalysisDatetime=dt;
	}

	$scope.addGroup = function(addGroup){
		var middlePosition = ($scope.epicrise.epicriseGroups.length + $scope.epicrise.epicriseGroups.length%2)/2;
		var groupElement = createGroupElement(addGroup.name);
		addTextHtmlValue(groupElement, "");
		$scope.epicrise.epicriseGroups.splice(middlePosition,0,groupElement);
		closeAllGroupEditors();
		$scope.epicrise.epicriseGroups[middlePosition].open = true;
	}

	$scope.saveWorkDoc = function(){
		console.log("-----------------");
		$scope.epicrise.hid = parameters.hid;
		$scope.epicrise.patientHistory = $scope.patientHistory;
		saveWorkDoc("/save/epicrise", $scope, $http);
		console.log("-----------------");
	}

	$scope.editOpenClose = function(h1Index){
		var oldOpen = $scope.epicrise.epicriseGroups[h1Index].open;
		if(!$scope.epicrise.epicriseGroups[h1Index].value){
			addTextHtmlValue($scope.epicrise.epicriseGroups[h1Index], "");
		}
		$scope.dt = $scope.epicrise.epicriseGroups[h1Index].value.historyTreatmentAnalysisDatetime;
		closeAllGroupEditors();
		$scope.epicrise.epicriseGroups[h1Index].open = !oldOpen;
		if(!$scope.epicrise.epicriseGroups[h1Index].open ){
			for(k in $scope.epicrise.epicriseGroups[h1Index].value.laborValues)
				if($scope.epicrise.epicriseGroups[h1Index].value.laborValues[k].value == "")
					delete $scope.epicrise.epicriseGroups[h1Index].value.laborValues[k];
		}
	}

	$scope.editOpenCloseOld = function(h1Index){
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
		closeAllGroupEditors();
		head1s[h1Index].open = !oldOpen;
		if(!head1s[h1Index].open )
		{
			for(k in $scope.epicrise[head1s[h1Index].name].laborValues)
				if($scope.epicrise[head1s[h1Index].name].laborValues[k].value == "")
					delete $scope.epicrise[head1s[h1Index].name].laborValues[k];
		}
	}

	closeAllGroupEditors = function(){
		$scope.epicrise.epicriseGroups.forEach(function(o) {
			o.open = false;
		});
	};

	//--------------sort array--------------------------------------------------

	moveTo = function(arrayToSort, indexFrom, indexTo){
		var el = arrayToSort.splice(indexFrom, 1);
		arrayToSort.splice(indexTo, 0, el[0]);
	}

	moveUp = function(arrayToSort, index){
		moveTo(arrayToSort, index, index-1);
	}

	movePlus = function(arrayToSort, index){
		if(index < arrayToSort.length){
			moveUp(arrayToSort, index);
		}else{
			moveTo(arrayToSort, index-1,0);
		}
	}

	moveMinus = function(arrayToSort, index){
		if(index > 0){
			moveUp(arrayToSort, index);
		}else{
			moveTo(arrayToSort, 0, arrayToSort.length-1);
		}
	}

	moveEpicriseGroupDown = function($itemScope){
		console.log($itemScope.$index);
		movePlus($scope.epicrise.epicriseGroups, $itemScope.$index);
		console.log($scope.epicrise.epicriseGroups);
	}
	moveEpicriseGroupUp = function($itemScope){
		console.log($itemScope.$index);
		moveMinus($scope.epicrise.epicriseGroups, $itemScope.$index);
		console.log($scope.epicrise.epicriseGroups);
	}
	//--------------sort array-----------------------------------------------END

	//-----------------context menu---------------------------------------------
	$scope.menuEpicriseGroup = [
		['<span class="glyphicon glyphicon-arrow-up"></span> Догори ', function ($itemScope) {
			moveEpicriseGroupUp($itemScope);
		}],
		['<span class="glyphicon glyphicon-arrow-down"></span> Донизу ', function ($itemScope) {
			moveEpicriseGroupDown($itemScope);
		}]
	];
	//-----------------context menu------------------------------------------END
} ]);
