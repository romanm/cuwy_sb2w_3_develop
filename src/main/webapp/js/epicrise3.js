
var historyFile = "/hol/history_id_" + parameters.hid;
var epicriseTemplate = {
		head1s : [ 
		{ id : "dz", name : "Діагнози"
		}, { id : "op", name : "Орерації"
		}, { id : "treatLab", name : "Лікування, обстеження, аналізи, рекомендації ..."
			,onDemand : true
		}, { id : "ans", name : "Перебіг хвороби"
		}, { id : "treat", name : "Лікування"
		}, { id : "zak", name : "ЗАК"
		}, { id : "zas", name : "ЗАС"
		}, { id : "biochim", name : "Біохімічний аналіз"
		}, { id : "rek", name : "Рекомендовано/смерть/перевід"
		}, { id : "nepraz", name : "Листки непрацездатності"
		} ],
		"Біохімічний аналіз" : [
			{name : "Загальний білок"}
			,{name : "Альбумін"}
			,{name : "Глобулін"}
			,{name : "Крефіцієнт"}
			,{name : "Холестерин загальний"}
			,{name : "Тригліцериди"}
			,{name : "Холестерин високої щільності	"}
			,{name : "β-ліпопротеїди"}
			,{name : "Білірубін загальний"}
			,{name : "Білірубін прямий"}
			,{name : "Білірубін непрямий"}
			,{name : "Калій"}
			,{name : "Натрій"}
			,{name : "Кальцій"}
			,{name : "Магній"}
			,{name : "Залізо"}
			,{name : "Фосфор"}
			,{name : "Хлор"}
			,{name : "АлАт"}
			,{name : "АсАт"}
			,{name : "Фосфатаза лужна (ФЛ)"}
			,{name : "α-амілаза"}
			,{name : "Сечовина"}
			,{name : "Креатинін"}
			,{name : "Сечова кислота"}
			,{name : "γ ГТП"}
			,{name : "Тимолова"}
		],
		"ЗАК" : [
		{name : "Гемоглобін"}
		,{name : "Еритроцити"}
		,{name : "Кольоровий показник"}
		,{name : "Лейкоцити"}
		,{name : "ШОЕ"}
		,{name : "Тромбоцити"}
		,{name : "Глюкоза натщесерце"}
		]
}

cuwyApp.controller('EpicriseCtrl', [ '$scope', '$http', '$filter', '$sce', function ($scope, $http, $filter, $sce) {

	console.log("EpicriseCtrl");
	$scope.epicriseTemplate = epicriseTemplate;
	$scope.epicrise = {};
	console.log($scope.epicriseTemplate);

	$http({ method : 'GET', url : historyFile
	}).success(function(data, status, headers, config) {
		$scope.epicrise.patientHistory = data;
		initEpicrise();
	}).error(function(data, status, headers, config) {
	});

	initEpicrise = function(){
		$scope.epicrise.patientHistory.historyTreatmentAnalysises.forEach(function(o) {
			console.log(o);
			$scope.epicrise[o.historyTreatmentAnalysisName] = {hol1:o};
			initFromHol1($scope.epicrise[o.historyTreatmentAnalysisName]);
		});
		console.log($scope.epicrise);
	}

	initFromHol1 = function(epicriseElement){
		var textHol1 = epicriseElement.hol1.historyTreatmentAnalysisText;
		var element = angular.element(textHol1);
		var trs = element.find("td.name");
		if(trs.length > 0){
			var hol1LaborValues = {};
			for(i = 0; i < trs.length; i++){
				var nameTd = angular.element(trs[i]);
				var valueTd = nameTd.next();
				var unitTd = valueTd.next();
				if(valueTd.text().trim().length > 0){
					hol1LaborValues[nameTd.text()] = {value:valueTd.text(), unit:unitTd.text()};
				}
			}
			epicriseElement.hol1LaborValues = hol1LaborValues;
		}
	}

	$scope.openToEdit = function(head1s, h1Index){
		if(!$scope.epicrise[head1s[h1Index].name])
		{
			$scope.epicrise[head1s[h1Index].name] = {text:""};
		}

		var element = angular.element($scope.epicrise[head1s[h1Index].name].textHol1);
		var trs = element.find("td.name");
		var oldOpen = head1s[h1Index].open;

		head1s[h1Index].open = !oldOpen;
	}

} ]);
