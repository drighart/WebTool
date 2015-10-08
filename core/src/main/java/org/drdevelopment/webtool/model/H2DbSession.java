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

public class H2DbSession {
    
    private Integer id;
    private String username;
    private LocalDateTime sessionStart;
    private String statement;
    private LocalDateTime statementStart;
    
	public H2DbSession(Integer id, String username, LocalDateTime sessionStart, String statement,
			LocalDateTime statementStart) {
		super();
		this.id = id;
		this.username = username;
		this.sessionStart = sessionStart;
		this.statement = statement;
		this.statementStart = statementStart;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public LocalDateTime getSessionStart() {
		return sessionStart;
	}

	public void setSessionStart(LocalDateTime sessionStart) {
		this.sessionStart = sessionStart;
	}

	public String getStatement() {
		return statement;
	}

	public void setStatement(String statement) {
		this.statement = statement;
	}

	public LocalDateTime getStatementStart() {
		return statementStart;
	}

	public void setStatementStart(LocalDateTime statementStart) {
		this.statementStart = statementStart;
	}

	@Override
	public String toString() {
		return "H2DbSession [id=" + id + ", username=" + username + ", sessionStart=" + sessionStart + ", statement="
				+ statement + ", statementStart=" + statementStart + "]";
	}

}
