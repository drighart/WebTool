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

public class Plugin {

	private String pluginId;
	private LocalDateTime created;
	private LocalDateTime modified;
	
	private Integer dataVersion;
	private Boolean running;

	public Plugin() {
		super();
	}

	public Plugin(String pluginId, Integer dataVersion, Boolean running) {
		super();
		this.created = LocalDateTime.now();
		this.modified = LocalDateTime.now();
		this.pluginId = pluginId;
		this.dataVersion = dataVersion;
		this.running = running;
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

	public String getPluginId() {
		return pluginId;
	}

	public void setPluginId(String pluginId) {
		this.pluginId = pluginId;
	}

	public Integer getDataVersion() {
		return dataVersion;
	}

	public void setDataVersion(Integer dataVersion) {
		this.dataVersion = dataVersion;
	}

	public Boolean getRunning() {
		return running;
	}

	public void setRunning(Boolean running) {
		this.running = running;
	}

	@Override
	public String toString() {
		return "Plugin [pluginId=" + pluginId + ", created=" + created + ", modified=" + modified + ", dataVersion="
				+ dataVersion + ", running=" + running + "]";
	}
	
}
