package org.drdevelopment.webtool.plugin.model;

import java.util.ArrayList;
import java.util.List;

import org.drdevelopment.webtool.plugin.authentication.User;

public class UserImpl implements User {
	private String name;
	private String fullName;
	private String emailAddress;
	private List<String> roles;
	private List<String> groups;
	
	public UserImpl(String name, String fullName, String emailAddress) {
		super();
		this.name = name;
		this.fullName = fullName;
		this.emailAddress = emailAddress;
		this.roles = new ArrayList<>();
		this.groups = new ArrayList<>();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public List<String> getRoles() {
		return roles;
	}

	@Override
	public List<String> getGroups() {
		return groups;
	}

	@Override
	public String getEmailAddress() {
		return emailAddress;
	}

	@Override
	public String getFullName() {
		return fullName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((emailAddress == null) ? 0 : emailAddress.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserImpl other = (UserImpl) obj;
		if (emailAddress == null) {
			if (other.emailAddress != null)
				return false;
		} else if (!emailAddress.equals(other.emailAddress))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UserImpl [name=" + name + ", fullName=" + fullName + ", emailAddress=" + emailAddress + ", roles="
				+ roles + ", groups=" + groups + "]";
	}

}
