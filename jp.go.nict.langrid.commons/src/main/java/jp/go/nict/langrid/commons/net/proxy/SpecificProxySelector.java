/*
 * $Id: SpecificProxySelector.java 888 2013-06-14 06:21:33Z t-nakaguchi $
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2008 NICT Language Grid Project.
 *
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU Lesser General Public License as published by 
 * the Free Software Foundation, either version 2.1 of the License, or (at 
 * your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser 
 * General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License 
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package jp.go.nict.langrid.commons.net.proxy;

import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import jp.go.nict.langrid.commons.net.proxy.pac.PacProxySelector;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 888 $
 */
public class SpecificProxySelector extends PacProxySelector {
    /**
     * 
     * 
     */
    public SpecificProxySelector(String httpProxyHost, int httpProxyPort, String ignoreList) throws ParseException {
        this(httpProxyHost, httpProxyPort, httpProxyHost, httpProxyPort, ignoreList);
        proxyEqualy = true;
    }

    /**
     * 
     * 
     */
    public SpecificProxySelector(String httpProxyHost, int httpProxyPort,
            String httpsProxyHost, int httpsProxyPort, String ignoreList) throws ParseException {
        super(makePacScript(httpProxyHost, httpProxyPort, httpsProxyHost, httpsProxyPort, ignoreList));
        proxyEqualy = false;
        this.httpProxyHost = httpProxyHost;
        this.httpProxyPort = httpProxyPort;
        this.httpsProxyHost = httpsProxyHost;
        this.httpsProxyPort = httpsProxyPort;
        this.ignoreList = ignoreList;
        proxyEqualy = false;
    }

    /**
     * 
     * 
     */
    public String getHttpProxyHost() {
        return httpProxyHost;
    }

    /**
     * 
     * 
     */
    public int getHttpProxyPort() {
        return httpProxyPort;
    }

    /**
     * 
     * 
     */
    public String getHttpsProxyHost() {
        return httpsProxyHost;
    }

    /**
     * 
     * 
     */
    public int getHttpsProxyPort() {
        return httpsProxyPort;
    }

    /**
     * 
     * 
     */
    public String getIgnoreList() {
        return ignoreList;
    }

    /**
     * 
     * 
     */
    public boolean isProxyEqualy() {
        return proxyEqualy;
    }

    static String makePacScript(String httpProxyHost, int httpProxyPort,
            String httpsProxyHost, int httpsProxyPort, String ignoreList) throws ParseException {
        String[] ignorePatterns = sparateIgnoreList(ignoreList);
        String ignoreCondition = makeIgnoreCondition(ignorePatterns);
        return MessageFormat.format(JS_TEMPLATE, httpProxyHost, "" + httpProxyPort, httpsProxyHost, "" + httpsProxyPort, ignoreCondition);
    }

    private static String makeIgnoreCondition(String[] ignorePatterns) throws ParseException {
        if(ignorePatterns.length == 0) {
            return "false";
        }
        StringBuilder builder = new StringBuilder();
        String delimiter = "";
        for(String ignorePattern : ignorePatterns) {
            String condition;
            ignorePattern = ignorePattern.trim();
            if(isIPAddress(ignorePattern)) {
                condition = makeIPAddressCondition(ignorePattern);
            } else {
                condition = makeHostCondition(ignorePattern);
            }
            builder.append(delimiter);
            builder.append(condition);
            delimiter = " || ";
        }
        return builder.toString();
    }

    private static String makeHostCondition(String ignorePattern) {
        return MessageFormat.format(HOST_CONDITION_TEMPLATE, ignorePattern);
    }

    static String makeIPAddressCondition(String ignorePattern) {
        StringTokenizer tokenizer = new StringTokenizer(ignorePattern, ".");
        int tokenCount = tokenizer.countTokens();
        StringBuilder ip = new StringBuilder();
        StringBuilder netmask = new StringBuilder();
        String delimiter = "";
        while(tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            int num = translateIPAddressNumber(token);
            ip.append(delimiter);
            ip.append(num);
            netmask.append(delimiter);
            netmask.append(isWildcard(token) ? "0" : "255");
            delimiter = ".";
        }
        for(int i = tokenCount ; i < 4 ; i++) {
            ip.append(delimiter);
            ip.append("0");
            netmask.append(delimiter);
            netmask.append("0");
            delimiter = ".";
        }
        return MessageFormat.format(IPADDRESS_CONDITION_TEMPLATE, ip.toString(), netmask.toString());
    }

    static boolean isIPAddress(String ignorePattern) throws ParseException {
        StringTokenizer tokenizer = new StringTokenizer(ignorePattern, ".");
        int tokenCount = tokenizer.countTokens();
        if(tokenCount > 4) {
            return false;
        }
        boolean wildcard = false;
        boolean wildcardAfterAddress = false;
        while(tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            if(translateIPAddressNumber(token) == -1) {
                return false;
            }
            if(isWildcard(token)) {
                wildcard = true;
            } else if(wildcard) {
                wildcardAfterAddress = true;
            }
        }
        if(wildcardAfterAddress) {
            throw new ParseException("", 0);
        }
        if(!wildcard && tokenCount < 4) {
            throw new ParseException("", 0);
        }
        return true;
    }

    static boolean isWildcard(String token) {
        return "*".equals(token);
    }

    static int translateIPAddressNumber(String token) {
        try {
            if(isWildcard(token)) {
                return 0;
            }
            int num = Integer.parseInt(token);
            if(0 <= num && num <= 255) {
                return num;
            } else {
                return -1;
            }
        } catch(NumberFormatException e) {
            return -1;
        }
    }

    private static String[] sparateIgnoreList(String ignoreList) throws ParseException {
        if(ignoreList == null || ignoreList.trim().equals("")) {
            return new String[0];
        }
        if(ignoreList.indexOf("'") != -1 || ignoreList.indexOf("\"") != -1) {
            throw new ParseException("", 0);
        }
        List<String> ret = new ArrayList<String>();
        StringTokenizer tokenizer = new StringTokenizer(ignoreList, ",; \n");
        while(tokenizer.hasMoreTokens()) {
            ret.add(tokenizer.nextToken());
        }
        return ret.toArray(new String[ret.size()]);
    }

    private String  httpProxyHost;
    private int     httpProxyPort;
    private String  httpsProxyHost;
    private int     httpsProxyPort;
    private String  ignoreList;
    private boolean proxyEqualy;

    private final static String JS_TEMPLATE =
        "function FindProxyForURL(url, host) '{'\n" +
        "  if ( {4} ) '{'\n" +
        "    return ''DIRECT'';\n" +
        "  } else '{'\n" +
        "    if(url.toLowerCase().indexOf(''https'') == 0) '{'\n" +
        "      return ''PROXY {2}:{3}; DIRECT'';\n" +
        "    } else '{'\n" +
        "      return ''PROXY {0}:{1}; DIRECT'';\n" +
        "    }\n" +
        "  }\n" +
        "}\n";

    private final static String IPADDRESS_CONDITION_TEMPLATE =
                                        "isInNet(host, ''{0}'', ''{1}'')";

    private final static String HOST_CONDITION_TEMPLATE =
                                        "shExpMatch(host, ''{0}'')";
}
