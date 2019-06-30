package com.framework.middleware.shareBean;

import java.io.Serializable;
import java.util.Date;

public class Channel implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String code;

    private String name;

    private String status;

    private Date enabledate;

    private Date disabledate;

    private Date createdate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getEnabledate() {
        return enabledate;
    }

    public void setEnabledate(Date enabledate) {
        this.enabledate = enabledate;
    }

    public Date getDisabledate() {
        return disabledate;
    }

    public void setDisabledate(Date disabledate) {
        this.disabledate = disabledate;
    }

    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    @Override
    public String toString() {
        return "This is business.Channel!  Channel.code:"+code+" Channel.name:  "+name;
    }
}