package hello;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.cuwy1.hol.model.ConfigHol;
import org.cuwy1.hol.model.ConsumptionMaterialBooking;
import org.cuwy1.hol.model.CountryHol;
import org.cuwy1.hol.model.DepartmentHol;
import org.cuwy1.hol.model.DiagnosisOnAdmission;
import org.cuwy1.hol.model.DrugBooking;
import org.cuwy1.hol.model.HistoryTreatmentAnalysis;
import org.cuwy1.hol.model.PatientDepartmentMovement;
import org.cuwy1.hol.model.PatientDiagnosisHol;
import org.cuwy1.hol.model.PatientHistory;
import org.cuwy1.hol.model.ProcedureBooking;
import org.cuwy1.hol.model.RegionHol;
import org.cuwy1.hol.report.PatientsAdmission;
import org.cuwy1.holDb.model.HistoryHolDb;
import org.cuwy1.holDb.model.PatientHolDb;
import org.cuwy1.icd10.Icd10Class;
import org.cuwy1.icd10.Icd10UaClass;
import org.cuwy1.model1.Department;
import org.cuwy1.model1.PatientDiagnosis;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

@Controller
public class HelloController {

	private static final Logger logger = LoggerFactory.getLogger(HelloController.class);

	@Autowired
	private CuwyDbService1 cuwyDbService1;
	
	@RequestMapping(value="/i", method=RequestMethod.GET)
	public String index() {
		logger.info("\n Start index_t.");
		return "index_t";
	}

	@RequestMapping(value="/hol/admin", method=RequestMethod.GET)
	public String hol_admin() {
		logger.info("\n Start /hol/admin");
		return "hol/admin/admin";
	}
	@RequestMapping(value="/hol/admin/ato", method=RequestMethod.GET)
	public String hol_admin_ato() {
		logger.info("\n Start /hol/admin/ato");
		return "hol/admin/ato";
	}
	@RequestMapping(value="/hol/operation/order-archive", method=RequestMethod.GET)
	public String hol_operation_order_archive() {
		logger.info("\n Start /hol/operation/order-archive");
		return "hol/admin/order-archive";
	}
	@RequestMapping(value="/hol/operation-order-archive", method=RequestMethod.GET)
	public @ResponseBody List<Map<String, Object>> json_operation_order_archive() {
		logger.info("\n Start /hol/operation-order-archive");
		return cuwyDbService1.getArchiveOperationOrder();
	}
	@RequestMapping(value="/hol/operation/order-active", method=RequestMethod.GET)
	public String hol_operation_order_active() {
		logger.info("\n Start /hol/operation/order-active");
		return "hol/admin/order-active";
	}
	@RequestMapping(value="/hol/operation-order-active", method=RequestMethod.GET)
	public @ResponseBody List<Map<String, Object>> json_operation_order_active() {
		logger.info("\n Start /hol/operation-order-active");
		return cuwyDbService1.getActiveOperationOrder();
	}
	@RequestMapping(value="/hol/admin/personal", method=RequestMethod.GET)
	public String hol_admin_personal() {
		logger.info("\n Start /hol/admin/personal");
		return "hol/admin/personal";
	}
	@RequestMapping(value="/hol/personal-list", method=RequestMethod.GET)
	public @ResponseBody List<Map<String, Object>> hol_personal_list() {
		logger.info("\n Start /hol/personal-list");
		return cuwyDbService1.getPersonalListe();
	}

	@RequestMapping(value="/hol/department", method=RequestMethod.GET)
	public String department() {
		logger.info("\n Start /hol/department");
		return "hol/department";
	}

	@RequestMapping(value="/hol/epicrise", method=RequestMethod.GET)
	public String patient_epicrise() {
		logger.info("\n Start /hol/epicrise");
		return "hol/epicrise";
	}

	@RequestMapping(value="/hol/history_{historyId}", method=RequestMethod.GET)
	public String getHistoryById(@PathVariable Integer historyId) {
		logger.info("\n Start /hol/history_"+historyId);
		return "hol/history";
	}
	@RequestMapping(value="/hol/history", method=RequestMethod.GET)
	public String patient_history() {
		logger.info("\n Start /hol/history");
		return "hol/history";
	}

	@RequestMapping(value="/hol/admission/icd10Editor1", method=RequestMethod.GET)
	public String icd10Editor1() {
		logger.info("\n Start /hol/admission/icd10Editor1");
		return "hol/admission/icd10Editor1";
	}

	@RequestMapping(value="/hol/admission/patientOld1", method=RequestMethod.GET)
	public String hol_admission_patientOld1() {
		logger.info("\n Start /hol/admission/patientOld1");
		return "hol/admission/patientOld1";
	}
	@RequestMapping(value="/hol/admission/patient", method=RequestMethod.GET)
	public String hol_admission_patient() {
		logger.info("\n Start /hol/admission/patient");
		return "hol/admission/patient";
	}

	@RequestMapping(value="/hol/operation/add-op", method=RequestMethod.GET)
	public String hol_operation_add_op() {
		logger.info("\n Start /hol/operation/add-op ");
		return "hol/operation/add-op";
	}

	@RequestMapping(value="/hol/operation/liste-op", method=RequestMethod.GET)
	public String hol_operation_liste_op() {
		logger.info("\n Start /hol/operation/liste-op ");
		return "hol/operation/liste-op";
	}
	@RequestMapping(value="/hol/operation-liste", method=RequestMethod.GET)
	public @ResponseBody List<Map<String, Object>> json_operation_liste() {
		logger.info("\n Start /hol/operation-liste");
		return cuwyDbService1.getOperationListe();
	}
	@RequestMapping(value="/hol/operation/liste-complication", method=RequestMethod.GET)
	public String hol_operation_liste_complication() {
		logger.info("\n Start /hol/operation/liste-complication");
		return "hol/operation/liste-complication";
	}
	@RequestMapping(value="/hol/operation-complication", method=RequestMethod.GET)
	public @ResponseBody List<Map<String, Object>> json_complication_liste() {
		logger.info("\n Start /hol/operation-complication");
		return cuwyDbService1.getComplicationListe();
	}

	@RequestMapping(value="/hol/admission/statistic", method=RequestMethod.GET)
	public String hol_admission_statistic() {
		logger.info("\n Start /hol/admission/statistic");
		return "hol/admission/statistic";
	}

	@RequestMapping(value = "/jsonservlet", method = RequestMethod.POST)
	public @ResponseBody Article createEmployee(@RequestBody Article article) {
		logger.info("Start createEmployee.");
		logger.warn("\n article = "+article);
		return article;
	}

	@RequestMapping(value = "/seekIcd10Db/{query}", method = RequestMethod.GET)
	public @ResponseBody List<Map<String, Object>> seekIcd10Db(@PathVariable String query) {
		logger.warn("\n /seekIcd10Db/{query} query = "+query);
		List<Map<String, Object>> seekIcd10Db = cuwyDbService1.seekIcd10Db(query);
		logger.warn("\n /seekIcd10Db/"+query+" = "+seekIcd10Db.size());
		return seekIcd10Db;
	}

	@RequestMapping(value = "/icd10ua/allToFile", method = RequestMethod.GET)
	public @ResponseBody List<Map<String, Object>> icd10UaAllToFile() {
		List<Map<String, Object>> icd10UaAllToFile = cuwyDbService1.icd10UaAllToFile();
		writeToJsDbFile("var icd10uaAll = ", icd10UaAllToFile, icd10uaAllFileName);
		return icd10UaAllToFile;
	}
	@RequestMapping(value = "/icd10uaGroups/toFile", method = RequestMethod.GET)
	public @ResponseBody Icd10UaClass getIcd10UaGroupsToFile() {
		Icd10UaClass icd10UaGroups = cuwyDbService1.getIcd10UaGroups();
		writeToJsDbFile("var icd10uaGroups = ", icd10UaGroups, icd10uaGroupsFileName);
		return icd10UaGroups;
	}

	@RequestMapping(value = "/icd10ua/groups", method = RequestMethod.GET)
	public @ResponseBody Icd10UaClass getDummyIcd10Ua2() {
		Icd10UaClass icd10UaGroups = cuwyDbService1.getIcd10UaGroups();
		return icd10UaGroups;
	}

	@RequestMapping(value = "/readIcd10Childs", method = RequestMethod.POST)
	public @ResponseBody Icd10UaClass readIcd10Childs(@RequestBody Icd10UaClass icd10Class) {
		logger.warn("\n /readIcd10Childs= "+icd10Class);
		icd10Class = cuwyDbService1.getIcd10UaChilds(icd10Class);
		return icd10Class;
	}

	@RequestMapping(value = "/icd10ua/dummy", method = RequestMethod.GET)
	public @ResponseBody Icd10UaClass getDummyIcd10Ua() {
		logger.warn("\n /icd10ua/dummy ");
		Icd10UaClass icd10UaClass = new Icd10UaClass();
		List<Icd10UaClass> icd10UaChilds = cuwyDbService1.getIcd10UaChilds();
		logger.warn("\n "+icd10UaClass);
		icd10UaClass.setIcd10Childs(icd10UaChilds);
		return icd10UaClass;
	}

	@RequestMapping(value = "/icd10/dummy", method = RequestMethod.GET)
	public @ResponseBody Icd10Class getDummyIcd10() {
		Icd10Class icd10Class = new Icd10Class();
		SAXReader reader = new SAXReader();
		File url = new File(applicationFolderPfad + innerDbFolderPfad + icd10FileName);
		Document document = null;
		try {
			document = reader.read(url);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		logger.warn("document = "+document);
		if(null != document) {
			List<Element> classList = document.selectNodes( "/ClaML/Class[@kind = 'chapter']" );
			if(0 < classList.size()) {
				icd10Class.setIcd10Classes(new ArrayList<Icd10Class>());
				for (Element icdClassElement : classList) {
					Icd10Class icdClass = new Icd10Class();
					icdClass.setCode(icdClassElement.attributeValue("code"));
					icdClass.setKind(icdClassElement.attributeValue("kind"));
					List<Element> subClasses = icdClassElement.selectNodes("SubClass");
					String subClassCode = subClasses.get(0).attributeValue("code").substring(0, 3);
					subClassCode += subClasses.get(subClasses.size()-1).attributeValue("code").substring(3);
					icdClass.setSubCode(subClassCode);
					Element label = (Element) icdClassElement.selectSingleNode("Rubric/Label");
					icdClass.setText(label.getText());
					icd10Class.getIcd10Classes().add(icdClass);
				}
			}
		}
		return icd10Class;
	}

	private HistoryHolDb getShortPatientHistoryTemplate() {
		HistoryHolDb historyHolDb = new HistoryHolDb();
		historyHolDb.setHistoryUrgent(1);
		List<PatientDepartmentMovement> patientDepartmentMovements = new ArrayList<PatientDepartmentMovement>();
		patientDepartmentMovements.add(new PatientDepartmentMovement());
		historyHolDb.setPatientDepartmentMovements(patientDepartmentMovements);
		List<HistoryTreatmentAnalysis> historyTreatmentAnalysises = new ArrayList<HistoryTreatmentAnalysis>();
		historyHolDb.setHistoryTreatmentAnalysises(historyTreatmentAnalysises);
		DiagnosisOnAdmission diagnosisOnAdmission = new DiagnosisOnAdmission();
		diagnosisOnAdmission.setDiagnosId(2);
		historyHolDb.setDiagnosisOnAdmission(diagnosisOnAdmission);
		PatientHolDb patientHolDb = new PatientHolDb();
		historyHolDb.setPatientHolDb(patientHolDb);
		return historyHolDb;
	}

	private HistoryHolDb getShortPatientHistoryById(int historyId) {
		HistoryHolDb historyHolDb = cuwyDbService1.getHistoryHolDbById(historyId);
		addShortPatientHistory(historyHolDb);
		return historyHolDb;
	}

	private HistoryHolDb getShortPatientHistory(int historyNo) {
		HistoryHolDb historyHolDb = cuwyDbService1.getHistoryHolDbByNo(historyNo);
		addShortPatientHistory(historyHolDb);
		return historyHolDb;
	}

	private void addShortPatientHistory(HistoryHolDb historyHolDb) {
		System.out.println(1);
		List<PatientDepartmentMovement> patientDepartmentMovements
		= cuwyDbService1.getPatientDepartmentMovements(historyHolDb.getHistoryId());
		System.out.println(2);
		historyHolDb.setPatientDepartmentMovements(patientDepartmentMovements);
		List<HistoryTreatmentAnalysis> historyTreatmentAnalysises
		= cuwyDbService1.getHistoryTreatmentAnalysises(historyHolDb.getHistoryId());
		historyHolDb.setHistoryTreatmentAnalysises(historyTreatmentAnalysises);
		DiagnosisOnAdmission diagnosisOnAdmission
		= cuwyDbService1.getDiagnosisOnAdmission(historyHolDb.getHistoryId());
		historyHolDb.setDiagnosisOnAdmission(diagnosisOnAdmission);
		PatientHolDb patientHolDb = cuwyDbService1.getPatientHolDb(historyHolDb.getPatientId());
		historyHolDb.setPatientHolDb(patientHolDb);
	}

	private PatientHistory getShortPatientHistory_old(int history_no) {
		PatientHistory patientHistory = cuwyDbService1.getPatientHistoryByNo(history_no);
		List<PatientDepartmentMovement> patientDepartmentMovements
		= cuwyDbService1.getPatientDepartmentMovements(patientHistory.getHistory_id());
		patientHistory.setPatientDepartmentMovements(patientDepartmentMovements);
		List<HistoryTreatmentAnalysis> historyTreatmentAnalysises
		= cuwyDbService1.getHistoryTreatmentAnalysises(patientHistory.getHistory_id());
		patientHistory.setHistoryTreatmentAnalysises(historyTreatmentAnalysises);
		DiagnosisOnAdmission diagnosisOnAdmission
		= cuwyDbService1.getDiagnosisOnAdmission(patientHistory.getHistory_id());
		patientHistory.setDiagnosisOnAdmission(diagnosisOnAdmission);
		cuwyDbService1.setPatientName(patientHistory);
		int patientId = patientHistory.getPatientId();
		PatientHolDb patientHolDb = cuwyDbService1.getPatientHolDb(patientId);
		patientHistory.setPatientHolDb(patientHolDb);
		return patientHistory;
	}

	@RequestMapping(value = "/openShortPatienHistory", method = RequestMethod.POST)
	public @ResponseBody HistoryHolDb openShortPatienHistory(@RequestBody PatientDiagnosisHol patientDiagnosisHol) {
		logger.info("\n Start /openShortPatienHistory "+patientDiagnosisHol);
		int history_no = patientDiagnosisHol.getHistory_no();
		HistoryHolDb patientHistory = getShortPatientHistory(history_no);
//		PatientHistory patientHistory = getShortPatientHistory(history_no);
		return patientHistory;
	}

	@RequestMapping(value = "/savePatientHistory", method = RequestMethod.POST)
	public @ResponseBody HistoryHolDb savePatientHistory(@RequestBody HistoryHolDb historyHolDb) {
		logger.info("\n savePatientHistory patientHistory = "+historyHolDb);
		int historyId = historyHolDb.getHistoryId();
		logger.info("\n HistoryId = "+historyId
				+"\n PatientId = "+historyHolDb.getPatientId()
				+"\n HistoryNo = "+historyHolDb.getHistoryNo());
		if(0==historyId) {
			logger.info("\n HistoryId = "+historyId
					+"\n PatientId = "+historyHolDb.getPatientId()
					+"\n HistoryNo = "+historyHolDb.getHistoryNo());
			int nextHistoryNo = cuwyDbService1.nextHistoryNo(2014);
			int nextHistoryId = cuwyDbService1.getAutoIncrement("history");
			int nextPatientId = cuwyDbService1.getAutoIncrement("patient");
			System.out.println(nextHistoryNo+"/"+nextHistoryId+"/"+nextPatientId);
			cuwyDbService1.insertPatientHolDb(historyHolDb.getPatientHolDb());
			System.out.println(historyHolDb);
			cuwyDbService1.insertHistoryHolDb(historyHolDb);
			DiagnosisOnAdmission diagnosisOnAdmission = historyHolDb.getDiagnosisOnAdmission();
			diagnosisOnAdmission.setHistoryId(historyHolDb.getHistoryId());
			diagnosisOnAdmission.setPersonalDepartmentId(118);
			diagnosisOnAdmission.setDiagnosId(1);
			cuwyDbService1.insertDiagnosisOnAdmission(diagnosisOnAdmission);
			diagnosisOnAdmission.setDiagnosId(2);
			cuwyDbService1.insertDiagnosisOnAdmission(diagnosisOnAdmission);
		}else{
			cuwyDbService1.updatePatientHolDb(historyHolDb.getPatientHolDb());
		}
		String patientPhoneHome = historyHolDb.getPatientHolDb().getPatientPhoneHome();
		logger.info("\n patientPhoneHome = "+patientPhoneHome);
		String patientPhoneMobil = historyHolDb.getPatientHolDb().getPatientPhoneMobil();
		logger.info("\n patientPhoneMobil = "+patientPhoneMobil);
		return historyHolDb;
	}

	@RequestMapping(value = "/hol/history_old_no_{historyNo}", method = RequestMethod.GET)
	public @ResponseBody PatientHistory getHolPatientHistoryOld(@PathVariable Integer historyNo) throws IOException {
		logger.info("\n Start /hol/history_no_"+historyNo);
		return getShortPatientHistory_old(historyNo);
	}

	@RequestMapping(value = "/hol/history_id_undefined", method = RequestMethod.GET)
	public @ResponseBody HistoryHolDb getHolPatientHistoryById() throws IOException {
		logger.info("\n Start /hol/history_id_undefined");
		HistoryHolDb shortPatientHistory = getShortPatientHistoryTemplate();
		return shortPatientHistory;
	}

	@RequestMapping(value = "/hol/history_id_{historyId}", method = RequestMethod.GET)
	public @ResponseBody HistoryHolDb getHolPatientHistoryById(@PathVariable Integer historyId) throws IOException {
		logger.info("\n Start /hol/history_id_"+historyId);
		HistoryHolDb shortPatientHistory = getShortPatientHistoryById(historyId);
		return shortPatientHistory;
	}

	@RequestMapping(value = "/hol/history_no_{historyNo}", method = RequestMethod.GET)
	public @ResponseBody HistoryHolDb getHolPatientHistory(@PathVariable Integer historyNo) throws IOException {
		logger.info("\n Start /hol/history_no_"+historyNo);
//		PatientHistory patientHistory = getShortPatientHistory(historyNo);
		HistoryHolDb shortPatientHistory = getShortPatientHistory(historyNo);
		Date patientDob = shortPatientHistory.getPatientHolDb().getPatientDob();
		return shortPatientHistory;
	}

	@RequestMapping(value = "/hol/regions_{districtId}", method = RequestMethod.GET)
	public @ResponseBody List<RegionHol> getRegions(@PathVariable Integer districtId) throws IOException {
		List<RegionHol> regions = cuwyDbService1.getRegions(districtId);
		return regions;
	}
	@RequestMapping(value = "/hol/department_{departmentId}", method = RequestMethod.GET)
	public @ResponseBody DepartmentHol getHolDepartment(@PathVariable Integer departmentId) throws IOException {
		logger.info("\n Start /hol/department_"+departmentId);
		DepartmentHol departmentHol = cuwyDbService1.getDepartmentsHol(departmentId);
		List<PatientDiagnosisHol> departmentsHolPatientsDiagnose 
		= cuwyDbService1.getDepartmentsHolPatientsDiagnose(departmentId);
		departmentHol.setPatientesDiagnosisHol(departmentsHolPatientsDiagnose);
		return departmentHol;
	}

	@RequestMapping(value = "/hol/department-list", method = RequestMethod.GET)
	public @ResponseBody DepartmentHol getHolDepartment() {
		logger.info("\n Start /hol/department-list");
		DepartmentHol departmentHol = new DepartmentHol();
		List<DepartmentHol> departmentsHol = cuwyDbService1.getDepartmentsHol();
		departmentHol.setDepartmentsHol(departmentsHol);
		return departmentHol;
	}
	
	@RequestMapping(value = "/procedure-booking/dummy", method = RequestMethod.GET)
	public @ResponseBody ProcedureBooking getDummyProcedureBooking() {
		ProcedureBooking procedureBooking = new ProcedureBooking();
		procedureBooking.setName("urology anesthesia ETH EA");
		procedureBooking.setNameLong("ХОЛ::Анестезія урологія::ЕТН ЕА - Ендотрахеальний наркоз - епідуральна анестезія");
		ArrayList<DrugBooking> drugsBooking = new ArrayList<DrugBooking>();
		
		DrugBooking drugBooking = new DrugBooking();
		drugBooking.setTrade("Дицинон");
		drugBooking.setGeneric("Этамзилат");
		drugBooking.setDoseConcentrationNumber(250.5f);
		drugBooking.setDoseConcentrationUnit("мг/мл");
		drugBooking.setDoseNumber(2);
		drugBooking.setDoseUnit("мл");
		drugBooking.setNumber(2);
		drugBooking.setNumberUnits("амп");
		drugsBooking.add(drugBooking);

		drugBooking = new DrugBooking();
		drugBooking.setTrade("Наропін");
		drugBooking.setGeneric("Ропівакаін");
		drugBooking.setDoseConcentrationNumber(7.5f);
		drugBooking.setDoseConcentrationUnit("мг/мл");
		drugBooking.setDoseNumber(5);
		drugBooking.setDoseUnit("мл");
		drugBooking.setNumber(2);
		drugBooking.setNumberUnits("амп");
		drugsBooking.add(drugBooking);
		
		drugBooking = new DrugBooking();
		drugBooking.setTrade("Дексаметазон");
		drugBooking.setGeneric("Дексаметазон");
		drugBooking.setDoseConcentrationNumber(4);
		drugBooking.setDoseConcentrationUnit("мг/мл");
		drugBooking.setDoseNumber(1);
		drugBooking.setDoseUnit("мл");
		drugBooking.setNumber(2);
		drugBooking.setNumberUnits("амп");
		drugsBooking.add(drugBooking);
		
		drugBooking = new DrugBooking();
		drugBooking.setTrade("Гелофузін");
		drugBooking.setGeneric("Гелофузін");
		drugBooking.setDoseNumber(400);
		drugBooking.setDoseUnit("мл");
		drugBooking.setNumber(1);
		drugBooking.setNumberUnits("фл");
		List<DrugBooking> replacement = new ArrayList<DrugBooking>();
		DrugBooking drug2 = new DrugBooking();
		drug2.setTrade("Гековен");
		replacement.add(drug2);
		drugBooking.setReplacement(replacement);
		drugsBooking.add(drugBooking);
		
		procedureBooking.setDrugsBooking(drugsBooking);

		List<ConsumptionMaterialBooking> consumptionMaterialsBooking = new ArrayList<ConsumptionMaterialBooking>();
		
		ConsumptionMaterialBooking consumptionMaterialBooking = new ConsumptionMaterialBooking();
		consumptionMaterialBooking.setName("Крапельниця");
		consumptionMaterialBooking.setNumber(2);
		consumptionMaterialBooking.setNumberUnits("шт");
		consumptionMaterialsBooking.add(consumptionMaterialBooking);
		
		consumptionMaterialBooking = new ConsumptionMaterialBooking();
		consumptionMaterialBooking.setName("Шприци");
		consumptionMaterialBooking.setMaterialType("2, 5, 10, 20 мл");
		consumptionMaterialBooking.setNumber(1);
		consumptionMaterialBooking.setNumberUnits("шт");
		consumptionMaterialsBooking.add(consumptionMaterialBooking);

		consumptionMaterialBooking = new ConsumptionMaterialBooking();
		consumptionMaterialBooking.setName("Марля 3м");
		consumptionMaterialBooking.setNumberUnits("фл");
		List<ConsumptionMaterialBooking> replacement2 = new ArrayList<ConsumptionMaterialBooking>();

		ConsumptionMaterialBooking replacementMaterial = new ConsumptionMaterialBooking();
		replacementMaterial.setName("бінт");
		replacement2.add(replacementMaterial);

		replacementMaterial = new ConsumptionMaterialBooking();
		replacementMaterial.setName("спирт");
		replacement2.add(replacementMaterial);
		
		consumptionMaterialBooking.setReplacement(replacement2);

		consumptionMaterialsBooking.add(consumptionMaterialBooking);
		
		procedureBooking.setConsumptionMaterialsBooking(consumptionMaterialsBooking);
		
		writeToJsonDbFile(procedureBooking, "dummyProcedureBooking.json");
		
		return procedureBooking;
	}

	@RequestMapping(value = "/department/dummy", method = RequestMethod.GET)
	public @ResponseBody Department getDummyDepartment() {
		logger.info("\n Start getDummyDepartment");
		Department department = new Department();
		department.setName("Реанімація");
		List<PatientDiagnosis> patientesDiagnoses = new ArrayList<>();
		PatientDiagnosis patientDiagnose = new PatientDiagnosis();
		patientDiagnose.setDiagnosis("Столбняк");
		patientDiagnose.setName(" Петренко");
		patientDiagnose.setUrl("patient.html");
		patientesDiagnoses.add(patientDiagnose);
		patientDiagnose = new PatientDiagnosis();
		patientDiagnose.setDiagnosis("Лептоспироз");
		patientDiagnose.setName("Іванчишин");
		patientesDiagnoses.add(patientDiagnose);
		patientDiagnose = new PatientDiagnosis();
		patientDiagnose.setDiagnosis("Енцефаліт");
		patientDiagnose.setName("Сидорчук");
		patientesDiagnoses.add(patientDiagnose);
		department.setPatientesDiagnoses(patientesDiagnoses);
		writeToJsonDbFile(department, departmentFileName);
		return department;
	}

	String applicationFolderPfad = "/home/roman/Documents/01_curepathway/work2/cuwy_sb2w_3_develop-w2/";
	String innerDbFolderPfad = "src/main/webapp/db/";
	//surgical intensive care unit (SICU)
	String departmentFileName = "departmentSICU.json";
	String addressesJsonFileName = "addresses.json";
	String addressesJsFileName = "addresses.json.js";
	String configJsFileName = "config.json.js";
	String icd10uaAllFileName = "icd10uaAll.json.js";
	String icd10uaGroupsFileName = "icd10uaGroups.json.js";
	String icd10FileName = "icd102010en.xml";

	@RequestMapping(value = "/config/create_file", method = RequestMethod.GET)
	public @ResponseBody ConfigHol createConfigFile() {
		ConfigHol configHol = new ConfigHol();
		List<CountryHol> readCountries = cuwyDbService1.readCountries();
		List<DepartmentHol> departmentHol = cuwyDbService1.getDepartmentsHol();
		List<Map<String, Object>> directsHol = cuwyDbService1.getDirectsHol();
		configHol.setCountries(readCountries);
		configHol.setDepartments(departmentHol);
		configHol.setDirects(directsHol);
//		writeToJsonDbFile(readCountries, addressesJsonFileName);
		writeToJsDbFile("var configHol = ", configHol, configJsFileName);
		return configHol;
	}

	@RequestMapping(value = "/address/create_file", method = RequestMethod.GET)
	public @ResponseBody List<CountryHol> createAddressFile() {
		List<CountryHol> readCountries = cuwyDbService1.readCountries();
//		writeToJsonDbFile(readCountries, addressesJsonFileName);
		writeToJsDbFile("var addresses = ", readCountries, addressesJsFileName);
		return readCountries;
	}

	@RequestMapping(value = "/addDepartmentNewPatien", method = RequestMethod.POST)
	public @ResponseBody Department addDepartmentNewPatien(@RequestBody Department department) {
		logger.info("Start addDepartmentNewPatien.");
		logger.warn("\n department = "+department);
		writeToJsonDbFile(department, departmentFileName);
		return department;
	}

	private void writeToJsDbFile(String variable, Object objectForJson, String fileName) {
		File file = new File(applicationFolderPfad + innerDbFolderPfad + fileName);
		ObjectMapper mapper = new ObjectMapper();
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			fileOutputStream.write(variable.getBytes());
			mapper.writeValue(fileOutputStream, objectForJson);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void writeToJsonDbFile(Object department, String fileName) {
		File file = new File(applicationFolderPfad + innerDbFolderPfad + fileName);
		ObjectMapper mapper = new ObjectMapper();
		ObjectWriter writerWithDefaultPrettyPrinter = mapper.writerWithDefaultPrettyPrinter();
		try {
			logger.warn(writerWithDefaultPrettyPrinter.writeValueAsString(department));
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			writerWithDefaultPrettyPrinter.writeValue(fileOutputStream, department);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/article/dummy", method = RequestMethod.GET)
	public @ResponseBody Article getDummyArticle() {
		logger.info("\n Start getDummyArticle");
		Article article = new Article();
		article.setTitle("title");
		return article;
	}

	Map<Integer, Employee> empData = new HashMap<Integer, Employee>();
	private final AtomicInteger counter = new AtomicInteger();

	@RequestMapping(value = "/emp/dummy", method = RequestMethod.GET)
	public @ResponseBody Employee getDummyEmployee() {
		logger.info("\n Start getDummyEmployee");
		Employee emp = new Employee();
		int id = counter.incrementAndGet();
		emp.setId(id);
		emp.setName("Dummy");
		emp.setCreatedDate(new Date());
		empData.put(id, emp);
		return emp;
	}

	@RequestMapping(value = "/patients_department_year_{year}_week_{week}", method = RequestMethod.GET)
	public @ResponseBody List<Map<String, Object>> patientsDepartmentYearWeek(
			@PathVariable Integer year, @PathVariable Integer week) {
		List<Map<String, Object>> historysDepartment = cuwyDbService1.getHistorysDepartmentYearWeek(year, week);
		return historysDepartment;
	}
	@RequestMapping(value = "/patients_department_{departmentId}_year_{year}_week_{week}", method = RequestMethod.GET)
	public @ResponseBody PatientsAdmission patientsDepartmentYearWeek(
			@PathVariable Integer year, @PathVariable Integer week, @PathVariable Integer departmentId) {
		logger.warn("1");
		PatientsAdmission patientsAdmission = new PatientsAdmission();
		logger.warn("2");
		List<HistoryHolDb> historysYearWeek = cuwyDbService1.getHistorysYearWeek(year, week, departmentId);
		logger.warn("3");
		patientsAdmission.setHistorysYearWeek(historysYearWeek);
		logger.warn("4");
		return patientsAdmission;
	}
	
	@RequestMapping(value = "/patients_year_{year}_week_{week}", method = RequestMethod.GET)
	public @ResponseBody PatientsAdmission patientsYearWeek(
			@PathVariable Integer year, @PathVariable Integer week) {
		PatientsAdmission patientsAdmission = new PatientsAdmission();
		List<HistoryHolDb> historysYearWeek = cuwyDbService1.getHistorysYearWeek(year, week);
		patientsAdmission.setHistorysYearWeek(historysYearWeek);
		return patientsAdmission;
	}

	@RequestMapping(value = "/countPatientsPro2Weeks_{year}_{minWeek}_{maxWeek}", method = RequestMethod.GET)
	public @ResponseBody List<Map<String, Object>> countPatientsProWeeks(
			@PathVariable Integer year, @PathVariable Integer minWeek, @PathVariable Integer maxWeek) {
		List<Map<String, Object>> countPatientsProWeeks = cuwyDbService1.countPatientsProWeeks(year,minWeek,maxWeek);
		return countPatientsProWeeks;
	}

	@RequestMapping(value = "/countPatientsProWeeks_{year}_{monthNr}", method = RequestMethod.GET)
	public @ResponseBody List<Map<String, Object>> countPatientsProWeeks(
			@PathVariable Integer year, @PathVariable Integer monthNr) {
		List<Map<String, Object>> countPatientsProWeeks = cuwyDbService1.countPatientsProWeeks(year,monthNr);
		return countPatientsProWeeks;
	}

	@RequestMapping(value = "/locality_{regionId}", method = RequestMethod.GET)
	public @ResponseBody List<Map<String, Object>> seekLocality(@PathVariable Integer regionId) {
		logger.warn(" regionId = "+ regionId);
		List<Map<String, Object>> countPatientsProMonth = cuwyDbService1.seekLocality(regionId);
		return countPatientsProMonth;
	}

	@RequestMapping(value = "/countPatientsProMonth_{year}", method = RequestMethod.GET)
	public @ResponseBody List<Map<String, Object>> countPatientsProMonth(@PathVariable Integer year) {
		List<Map<String, Object>> countPatientsProMonth = cuwyDbService1.countPatientsProMonth(year);
		return countPatientsProMonth;
	}

	@RequestMapping(value = "/countPatientsProYear", method = RequestMethod.GET)
	public @ResponseBody List<Map<String, Object>> countPatientsProYear() {
		List<Map<String, Object>> countPatientsProYear = cuwyDbService1.countPatientsProYear();
		return countPatientsProYear;
	}

	@RequestMapping(value = "/emps", method = RequestMethod.GET)
	public @ResponseBody List<Employee> getAllEmployees() {
		logger.info("Start getAllEmployees.");
		List<Employee> emps = new ArrayList<Employee>();
		Set<Integer> empIdKeys = empData.keySet();
		for(Integer i : empIdKeys){
			emps.add(empData.get(i));
		}
		return emps;
	}

}
