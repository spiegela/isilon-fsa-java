package com.emc.isilon.ran;

import java.util.List;

public class EntryAccessControl {
	private List<AccessControlList> acl;
	private String authoritative, mode;
	private Trustee owner, group;
	
	public EntryAccessControl() {
	}

	public EntryAccessControl(List<AccessControlList> acl, String authoritative) {
		this.acl = acl;
		this.authoritative = authoritative;
	}
	public String getAuthoritative() {
		return authoritative;
	}
	public List<AccessControlList> getAcl() {
		return acl;
	}
	public void setAcl(List<AccessControlList> acl) {
		this.acl = acl;
	}
	public void setAuthoritative(String authoritative) {
		this.authoritative = authoritative;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public Trustee getOwner() {
		return owner;
	}
	public void setOwner(Trustee owner) {
		this.owner = owner;
	}
	public Trustee getGroup() {
		return group;
	}
	public void setGroup(Trustee group) {
		this.group = group;
	}
}
