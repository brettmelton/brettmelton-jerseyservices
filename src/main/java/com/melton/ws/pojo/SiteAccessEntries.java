package com.melton.ws.pojo;

import java.util.ArrayList;
import java.util.List;

public class SiteAccessEntries {

	private List<SiteAccessBean> siteAccessList;
	
	public SiteAccessEntries() {
		siteAccessList = new ArrayList<SiteAccessBean>();
	}
	
	public List<SiteAccessBean> getSiteAccessList() {
		return siteAccessList;
	}
	
	public void setSiteAccessList(List<SiteAccessBean> siteAccessList) {
		this.siteAccessList = siteAccessList;
	}	
}
