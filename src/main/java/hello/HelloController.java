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

import org.cuwy1.hol.model.ConsumptionMaterialBooking;
import org.cuwy1.hol.model.DepartmentHol;
import org.cuwy1.hol.model.DrugBooking;
import org.cuwy1.hol.model.PatientDiagnosisHol;
import org.cuwy1.hol.model.ProcedureBooking;
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

	@RequestMapping(value = "/jsonservlet", method = RequestMethod.POST)
	public @ResponseBody Article createEmployee(@RequestBody Article article) {
		logger.info("Start createEmployee.");
		logger.warn("\n article = "+article);
		return article;
	}

	@RequestMapping(value = "/addDepartmentNewPatien", method = RequestMethod.POST)
	public @ResponseBody Department addDepartmentNewPatien(@RequestBody Department department) {
		logger.info("Start addDepartmentNewPatien.");
		logger.warn("\n department = "+department);
		writeTofile(department, departmentFileName);
		return department;
	}

	@RequestMapping(value = "/readIcd10Childs", method = RequestMethod.POST)
	public @ResponseBody Icd10Class readIcd10Childs(@RequestBody Icd10Class icd10Class) {
		return icd10Class;
	}

	@RequestMapping(value = "/icd10ua/dummy", method = RequestMethod.GET)
	public @ResponseBody Icd10UaClass getDummyIcd10Ua() {
		Icd10UaClass icd10UaClass = new Icd10UaClass();
		List<Icd10UaClass> icd10UaChilds = cuwyDbService1.getIcd10UaChilds();
		icd10UaClass.setIcd10Classes(new ArrayList<Icd10UaClass>());
		for (Icd10UaClass icd10UaClass2 : icd10UaChilds) 
			icd10UaClass.getIcd10Classes().add(icd10UaClass2);
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

	@RequestMapping(value = "/hol/department_{departmentId}", method = RequestMethod.GET)
	public @ResponseBody DepartmentHol getHolDepartment(@PathVariable Integer departmentId) throws IOException {
		logger.info("\n Start /hol/department_"+departmentId);
		DepartmentHol departmentHol = cuwyDbService1.getDepartmentsHol(departmentId);
		List<PatientDiagnosisHol> departmentsHolPatientsDiagnose = cuwyDbService1.getDepartmentsHolPatientsDiagnose(departmentId);
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
		drugBooking.setDoseConcentrationNumber(250);
		drugBooking.setDoseConcentrationUnit("мг/мл");
		drugBooking.setDoseNumber(2);
		drugBooking.setDoseUnit("мл");
		drugBooking.setNumber(2);
		drugBooking.setNumberUnits("амп");
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
		
		procedureBooking.setConsumptionMaterialsBooking(consumptionMaterialsBooking);
		
		writeTofile(procedureBooking, "dummyProcedureBooking.json");
		
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
		writeTofile(department, departmentFileName);
		return department;
	}

	String applicationFolderPfad = "/home/roman/Documents/01_curepathway/work1/cuwy_sb2w_template_3--1/";
	String innerDbFolderPfad = "src/main/webapp/db/";
	//surgical intensive care unit (SICU)
	String departmentFileName = "departmentSICU.json";
	String icd10FileName = "icd102010en.xml";

//	private void writeTofile(Department department, String departmentFileName) {
	private void writeTofile(Object department, String departmentFileName) {
		File file = new File(applicationFolderPfad + innerDbFolderPfad + departmentFileName);
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
