package com.melton.ws.pojo;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlType(propOrder={"ip", "agent"})
public class SiteAccessBean {
	public String ip;
	public String agent;
	
	public SiteAccessBean() {} // JAXB needs this
	
	public SiteAccessBean(String ip, String agent) {
		this.ip = ip;
		this.agent = agent;
	}
	
	public String getIp() {
		return ip;
	}
	
	public String getAgent() {
		return agent;
	}
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + " [ip=" + ip + ", agent=" + agent + "]";
	}
	
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SiteAccessBean that = (SiteAccessBean) o;

        if (ip != null ? !ip.equals(that.ip) : that.ip != null) {
            return false;
        }
        if (agent != null ? !agent.equals(that.agent) : that.agent != null) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = ip != null ? ip.hashCode() : 0;
        result = 31 * result + (agent != null ? agent.hashCode() : 0);
        return result;
    }
}
