package com.gaoshin.cloud.web.xen.bean;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CloneRequest extends Request {
    private Long hostId;
    private String templateId;
    private String vmName;
    private String vdiUuid;

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getVmName() {
        return vmName;
    }

    public void setVmName(String vmName) {
        this.vmName = vmName;
    }

    public String getVdiUuid() {
        return vdiUuid;
    }

    public void setVdiUuid(String vdiUuid) {
        this.vdiUuid = vdiUuid;
    }

    public Long getHostId() {
        return hostId;
    }

    public void setHostId(Long hostId) {
        this.hostId = hostId;
    }
}
