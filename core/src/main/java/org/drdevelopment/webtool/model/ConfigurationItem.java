/*
 * This software source code is provided by the USEF Foundation. The copyright
 * and all other intellectual property rights relating to all software source
 * code provided by the USEF Foundation (and changes and modifications as well
 * as on new versions of this software source code) belong exclusively to the
 * USEF Foundation and/or its suppliers or licensors. Total or partial
 * transfer of such a right is not allowed. The user of the software source
 * code made available by USEF Foundation acknowledges these rights and will
 * refrain from any form of infringement of these rights.

 * The USEF Foundation provides this software source code "as is". In no event
 * shall the USEF Foundation and/or its suppliers or licensors have any
 * liability for any incidental, special, indirect or consequential damages;
 * loss of profits, revenue or data; business interruption or cost of cover or
 * damages arising out of or in connection with the software source code or
 * accompanying documentation.
 *
 * For the full license agreement see http://www.usef.info/license.
 */

package org.drdevelopment.webtool.model;

import java.time.LocalDateTime;

public class ConfigurationItem {

	private LocalDateTime created;
	private LocalDateTime modified;
	
	private String name;
	private String value;
	private String plugin;
	private String kindOfInput;

	public ConfigurationItem() {
		super();
	}

	public ConfigurationItem(String name, String value, String plugin, String kindOfInput) {
		super();
		this.created = LocalDateTime.now();
		this.modified = LocalDateTime.now();
		this.name = name;
		this.value = value;
		this.plugin = plugin;
		this.kindOfInput = kindOfInput;
	}

	public LocalDateTime getCreated() {
		return created;
	}

	public void setCreated(LocalDateTime created) {
		this.created = created;
	}

	public LocalDateTime getModified() {
		return modified;
	}

	public void setModified(LocalDateTime modified) {
		this.modified = modified;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getPlugin() {
		return plugin;
	}

	public void setPlugin(String plugin) {
		this.plugin = plugin;
	}

	public String getKindOfInput() {
		return kindOfInput;
	}

	public void setKindOfInput(String kindOfInput) {
		this.kindOfInput = kindOfInput;
	}

	@Override
	public String toString() {
		return "ConfigurationItem [created=" + created + ", modified="
				+ modified + ", name=" + name + ", value=" + value + "]";
	}

}
