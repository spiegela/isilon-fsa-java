package com.emc.isilon.ran;

import java.util.List;

public class AccessControlList {
	private String accesstype, op;
	private List<String> accessrights;
	private List<String> inherit_flags;
	private Trustee trustee;
	
	public AccessControlList() {
	}

	public AccessControlList(String op, String accesstype, Trustee trustee, List<String> accessrights) {
		this.op = op;
		this.accesstype = accesstype;
		this.trustee = trustee;
		this.accessrights = accessrights;
	}
	public String getAccesstype() {
		return accesstype;
	}
	public void setAccesstype(String accesstype) {
		this.accesstype = accesstype;
	}
	public List<String> getAccessrights() {
		return accessrights;
	}
	public void setAccessrights(List<String> accessrights) {
		this.accessrights = accessrights;
	}
	public List<String> getInherit_flags() {
		return inherit_flags;
	}
	public void setInherit_flags(List<String> inheritflags) {
		this.inherit_flags = inheritflags;
	}
	public Trustee getTrustee() {
		return trustee;
	}
	public void setTrustee(Trustee trustee) {
		this.trustee = trustee;
	}
	public String getOp() {
		return op;
	}
	public void setOp(String op) {
		this.op = op;
	}
}
