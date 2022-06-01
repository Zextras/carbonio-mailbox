package com.zimbra.cs.util.proxyconfgen;

import com.zimbra.cs.account.Provisioning;

/**
 * @author zimbra
 */
class LoginEnablerVar extends WebEnablerVar {

  public LoginEnablerVar() {
    super(
        "web.login.upstream.disable",
        "#",
        "Indicates whether upstream Login servers blob in nginx.conf.web should be populated "
            + "(false unless zimbraReverseProxyUpstreamLoginServers is populated)");
  }

  @Override
  public String format(Object o) {
    String[] upstreams =
        serverSource.getMultiAttr(Provisioning.A_zimbraReverseProxyUpstreamLoginServers);
    if (upstreams.length == 0) {
      return "#";
    } else {
      return "";
    }
  }
}
