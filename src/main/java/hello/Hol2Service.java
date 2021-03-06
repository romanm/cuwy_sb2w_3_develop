package hello;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import org.cuwy1.holDb.model.HistoryHolDb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component("hol2Service")
public class Hol2Service {
	private static final Logger logger = LoggerFactory.getLogger(Hol2Service.class);
	@Autowired private Hol2H2Jdbc hol2H2Jdbc;
	
	public HistoryHolDb saveHistory(HistoryHolDb historyHolDb) {
		Integer tmpId = historyHolDb.getTmpId();
		final Map<String, Object> history = hol2H2Jdbc.getHistory(tmpId);
		if(history == null){
			tmpId = hol2H2Jdbc.nextDbId();
			historyHolDb.setTmpId(tmpId);
			hol2H2Jdbc.insertHistory(tmpId, historyHolDb);
		}else{
			logger.debug("-------------update--------------");
			hol2H2Jdbc.updateHistory(tmpId,historyHolDb);
		}
		return historyHolDb;
	}
	//-------------------epicrise-----------------------------------------------
	public Map<String, Object> saveEpicrise(Map<String, Object> epicrise) {
		final Date savedDate = new Date();
		epicrise.put("savedDate", savedDate);
		final String hidStr = (String) epicrise.get("hid");
		logger.debug(hidStr);
		final Integer hid = Integer.parseInt(hidStr);
		final Map<String, Object> epicrise2 = hol2H2Jdbc.getEpicrise(hid);
		if(epicrise2 == null){
			hol2H2Jdbc.insertEpicrise(hid,epicrise);
		}else{
			logger.debug("-------------update--------------");
			hol2H2Jdbc.updateEpicrise(hid,epicrise);
		}
		return null;
	}

	public Map<String, Object> readEpicrise(Integer historyId) {
		final Map<String, Object> epicriseFromDb = hol2H2Jdbc.getEpicrise(historyId);
		logger.debug(""+epicriseFromDb);
		String epicriseSelf = (String) epicriseFromDb.get("EPICRISE_SELF");
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> epicrise = null;// = new HashMap<String, Object>();
		try {
			epicrise = mapper.readValue(epicriseSelf, Map.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return epicrise;
	}
	//-------------------epicrise--------------------------------------------END

	
}
