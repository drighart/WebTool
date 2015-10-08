package org.drdevelopment.webtool.api;

import java.util.Map;

public interface Template {

	String get(String uri, String param);

	String get(Map<String, String> values, String uri, String param);

	String get(String uri, Map<String, String> values, String param);

}