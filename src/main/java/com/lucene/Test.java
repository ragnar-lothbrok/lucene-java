package com.lucene;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.snapdeal.adtech.dto.AdTechPogRequest;
import com.snapdeal.adtech.dto.AdTechPogResponseSRO;
import com.snapdeal.adtech.services.IAdTechQuotaClientService;
import com.snapdeal.base.startup.config.AppEnvironmentContext;
import com.snapdeal.base.transport.service.ITransportService.Protocol;

@Service
public class Test {

	@Autowired
	@Qualifier("adTechClientService")
	private IAdTechQuotaClientService adTechQuotaClientService;
	
	@Autowired
	private AppEnvironmentContext appEnvironmentContext;

	@PostConstruct
	public void adTechQuotaClientService() {
		adTechQuotaClientService.setUrl("http://10.20.83.93:8083/service/adTechQuota/getAdPogDetails");

		
		AdTechPogRequest adTechPogRequest = new AdTechPogRequest();
		List<String> pogids = new ArrayList<String>();
		pogids.add("1234567");
		pogids.add("1217135466");

		adTechPogRequest.setPogIds(pogids);
		
		new ObjectMapper();
		
		AdTechPogResponseSRO p = new AdTechPogResponseSRO();
		
		
		adTechPogRequest.setRequestProtocol(Protocol.PROTOCOL_JSON);
		adTechPogRequest.setResponseProtocol(Protocol.PROTOCOL_JSON);
		try {
			System.out.println(adTechQuotaClientService.getPogDetails(adTechPogRequest));
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
}
