package com.horkr.util;

import com.google.common.net.InternetDomainName;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class UrlUtil {
    private static final Logger log = LogManager.getLogger(UrlUtil.class);

    private static final String IP_REGEX = "(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)";

    public static String getHost(String url) {
        String host = null;
        try {
            host = new URL(url).getHost();
        } catch (MalformedURLException e) {
            log.error("获取url的host出错", e);
        }
        return host;
    }

    /**
     * 判断一个host是否为顶级域名
     *
     * @param host
     * @return
     */
    public static boolean isTopDomain(String host) {
        if (host.matches(IP_REGEX)) {
            return true;
        }
        List<String> list = Arrays.asList("gov.cn", "edu.cn");
        if (list.contains(host)) {
            return true;
        }
        return InternetDomainName.from(host).isTopPrivateDomain();
    }



    /**
     * 通过url获取顶级域名，可能会获取一个ip
     *
     * @param url
     * @return
     */
    public static String getTopLevelDomain(String url) {
        String topDomain = null;
        try {
            String host = getHost(url);
            if (host.matches(IP_REGEX)) {
                return host;
            }
            InternetDomainName domainName = InternetDomainName.from(host);
            topDomain = domainName.topPrivateDomain().toString();
        } catch (Exception e) {
            log.error("获取顶级域名出错: {}", url, e);
        }
        return topDomain;
    }

}
