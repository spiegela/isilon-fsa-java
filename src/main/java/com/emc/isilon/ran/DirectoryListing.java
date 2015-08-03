package com.emc.isilon.ran;

import java.util.List;

public class DirectoryListing {
	private List<DirectoryEntry> children;

	public List<DirectoryEntry> getChildren() {
		return children;
	}

	public void setChildren(List<DirectoryEntry> children) {
		this.children = children;
	}
}
