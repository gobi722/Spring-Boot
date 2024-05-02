package com.kriyatec.hibernate.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "transactions")
public class Transaction {
	@Id
	@Column(name = "txnId")
	private String txnId;

	@Column(name = "token")
	private String token;

	@Column(name = "rrn")
	private String rrn;

	@Column(name = "txnGroup")
	private String txnGroup;

	@Column(name = "txnSets")
	private String txnSets;

	@Column(name = "isoData")
	private String isoData;


    public String getTxnId() {
        return txnId;
    }

    public void setTxnId(String txnId) {
        this.txnId = txnId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRrn() {
        return rrn;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
    }

    public String getTxnGroup() {
        return txnGroup;
    }

    public void setTxnGroup(String txnGroup) {
        this.txnGroup = txnGroup;
    }

    public String getTxnSets() {
        return txnSets;
    }

    public void setTxnSets(String txnSets) {
        this.txnSets = txnSets;
    }

    public String getIsoData() {
        return isoData;
    }

    public void setIsoData(String isoData) {
        this.isoData = isoData;
    }
}
