package com.framework.middleware.shareBean;

import java.io.Serializable;
import java.util.Date;

public class Operation implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;

    private String billno;

    private String billtype;

    private String operation;

    private Date operationtime;

    private Integer operationbyid;

    private String operationbycode;

    private String operationbyname;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBillno() {
        return billno;
    }

    public void setBillno(String billno) {
        this.billno = billno;
    }

    public String getBilltype() {
        return billtype;
    }

    public void setBilltype(String billtype) {
        this.billtype = billtype;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Date getOperationtime() {
        return operationtime;
    }

    public void setOperationtime(Date operationtime) {
        this.operationtime = operationtime;
    }

    public Integer getOperationbyid() {
        return operationbyid;
    }

    public void setOperationbyid(Integer operationbyid) {
        this.operationbyid = operationbyid;
    }

    public String getOperationbycode() {
        return operationbycode;
    }

    public void setOperationbycode(String operationbycode) {
        this.operationbycode = operationbycode;
    }

    public String getOperationbyname() {
        return operationbyname;
    }

    public void setOperationbyname(String operationbyname) {
        this.operationbyname = operationbyname;
    }
}