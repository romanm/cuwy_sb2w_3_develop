var historyFile = "/hol/history_no_"+parameters.hno;

console.log("historyFile = "+historyFile);

cuwyApp.controller('AdmissionPatientCtrl', [ '$scope', '$http', function ($scope, $http) {

	$scope.title = "Створити історію хвороби";
	$scope.addresses = configHol.countries;
	$scope.directs = configHol.directs;
	$scope.departmentsHol = configHol.departments;
	$scope.parameters = parameters;
	$scope.patient = {
		name: 'Patient name',
		history_no: parameters.hno,
		patientHistory: null
	};
	$scope.patientHistory = {};

	if(parameters.hno){
		$http({
			method : 'GET',
			url : historyFile
		}).success(function(data, status, headers, config) {
			$scope.patientHistory = data;
		}).error(function(data, status, headers, config) {
		});
	}
	
	$scope.isRegion2of4 = function($index, regions){
		return $index > regions.length/4 && $index < regions.length/4*2;
	}

	$scope.isRegion3of4 = function($index, regions){
		return $index > regions.length/4*2 && $index < regions.length/4*3;
	}

	$scope.isDistrict2of3 = function($index, districts){
		return $index < districts.length/3*2 && $index > districts.length/3
	}

	$scope.savePatientHistory = function(){
		console.log("savePatientHistory");
		var postNewPatien = $http({
			method : 'POST',
			data : $scope.patientHistory,
			url : '/savePatientHistory'
		}).success(function(data, status, headers, config){
			$scope.patientHistory = data;
		}).error(function(data, status, headers, config) {
		});

	}

	$scope.patientEditing = {name: '', last: '', patientPatronymic: '', patientGender: false,
		country: $scope.addresses[0].countryName,
		countryCollapsed: true,
		district: $scope.addresses[0].districtsHol[0].districtName,
		region: '',
		localityType: '',
		localityName: '',
		pilgy: '',
		urgentPlan: '',
		x: null
	};

	$scope.changeDiagnose = function(){
		console.log("changeDiagnose");
	}
	$scope.getCountryDistricts = function(){
		$($scope.addresses).each(function () {
			if(this.countryName == $scope.patientEditing.country){
				$scope.districts = this.districtsHol;
			}
		});
	}

	$scope.getCountryDistricts();

	$scope.getDistrictRegions = function(){
		$($scope.districts).each(function () {
			if(this.districtName == $scope.patientEditing.district){
				$scope.regions = this.regionsHol;
			}
		});
	}

	$scope.getDistrictRegions();

	$scope.writeDepartment = function(department){
		$scope.patientHistory.patientDepartmentMovements[0].departmentId = department.department_id;
		$scope.patientHistory.patientDepartmentMovements[0].departmentName = department.department_name;
		console.log($scope.patientHistory.patientDepartmentMovements[0]);
	}

	$scope.writeToModel = function(fieldName, value){
		$scope.patientEditing[fieldName] = value;
		if("country" == fieldName){
			$scope.patientEditing.countryCollapsed = true;
		}
	}

	$scope.writeDirect = function(direct){
		$scope.patientEditing.direct = direct.direct_name;
		$scope.supportDirectField();
	}

	$scope.collapseDirectListe = true;
	$scope.supportDirectField = function(){
		if($scope.patientEditing.direct){
			$scope.collapseDirectListe = !($scope.patientEditing.direct.length > 0);
			for(var i = 0 ; i < $scope.directs.length ; i++ ){
				if($scope.directs[i].direct_name == $scope.patientEditing.direct){
					$scope.collapseDirectListe = true;
					return ;
				}
			}
		}
	}
	$scope.supportDistrictField = function(){
		var collapseDistrictListe = true;
		if($scope.patientEditing.district){
			collapseDistrictListe = !($scope.patientEditing.district.length > 0);
			if(!$scope.districts){
				//seek all districts
			}else{
				$($scope.districts).each(function(){
					if(this.districtName == $scope.patientEditing.district){
						collapseDistrictListe = true;
					}
				});
			}
		}
		return collapseDistrictListe;
	}
	$scope.collapseRegionListe = true;
	$scope.supportRegionField = function(){
		$scope.collapseRegionListe = true;
		if($scope.patientEditing.region ){
			$scope.collapseRegionListe = !($scope.patientEditing.region.length > 0);
			if(!$scope.regions){
				//seek all regions
			}else{
				$($scope.regions).each(function(){
					if(this.regionName == $scope.patientEditing.region){
						$scope.collapseRegionListe = true;
					}
				});
			}
		}
	}

	$scope.openCountryFormGroup = function(){
		console.log($scope.patientEditing.country);
		$scope.patientEditing.countryCollapsed = !$scope.patientEditing.countryCollapsed;
		if(false && !$scope.patientEditing.countryCollapsed){
			$http({
				method : 'POST',
				data : patient,
				url : '/openShortPatienHistory'
			}).success(function(data, status, headers, config){
				patient.patientHistory = data;
			}).error(function(data, status, headers, config) {
			});
		}
	}

	$scope.isWarning = function(field, art){
		var isShowWarning = field.$error.maxlength || field.$error.minlength;
		checkShowEditField(field, isShowWarning, "warning", "warning-sign");
		return field.$error[art] && field.$dirty;
	}

	$scope.isNoCountryInDb = function(){
		var isNoCountryInDb = true;
		$($scope.addresses).each(function () {
			if(this.countryName == $scope.patientEditing.country)
				isNoCountryInDb = false;
		}
		);
		checkShowEditField($scope.newPatientForm.country, isNoCountryInDb, "warning", "warning-sign");
		return isNoCountryInDb;
	}

	$scope.isLastNameValid = function(field, art){
		var isRequired = field.$error.required && field.$dirty;
		if(isRequired){
			checkShowEditField(field, isRequired, "error", "remove");
			if(!art)
				return isRequired;
		}
		var isShowWarning = field.$error.maxlength || field.$error.minlength;
		checkShowEditField(field, isShowWarning, "warning", "warning-sign");
		return field.$error[art] && field.$dirty;
	}

	$scope.isRequired = function(field){
		var isRequired = field.$error.required && field.$dirty;
		checkShowEditField(field, isRequired, "error", "remove");
		return isRequired;
	}

	checkShowEditField = function(field, isOk, groupValidClass, iconValidClass){
		var fieldEl = $("#"+field.$name);
		var fieldIconEl = $(fieldEl[0].nextElementSibling);
		if(fieldIconEl.hasClass("glyphicon")){
			var formGroupEl = fieldIconEl.parent();
			formGroupEl.removeClass("has-success has-warning has-error");
			fieldIconEl.removeClass("glyphicon-remove glyphicon-ok glyphicon-warning-sign");
			if(isOk){
				formGroupEl.addClass("has-"+groupValidClass);
				fieldIconEl.addClass("glyphicon-"+iconValidClass);
			}else{
				formGroupEl.addClass("has-success");
				if(field.$dirty){
					fieldIconEl.addClass("glyphicon-ok");
				}
			}
		}
	}

} ] );

$(function() {
	$('#dob').datepicker({
		format : 'dd-mm-yyyy'
	});
});
