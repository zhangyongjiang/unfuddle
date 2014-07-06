package com.gaoshin.cloud.web.vm.bean;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DataCenter {
	private Long id;
    private String name = null;
    private String description = null;
    private String dns1 = null;
    private String dns2 = null;
    private String internalDns1 = null;
    private String internalDns2 = null;
    private String routerMacAddress;
    private String guestNetworkCidr = null;
    private Long domainId = null;
    private String domain;
    private NetworkType networkType;
    private String dnsProvider;
    private String dhcpProvider;
    private String gatewayProvider;
    private String vpnProvider;
    private String userDataProvider;
    private String loadBalancerProvider;
    private String firewallProvider;
    private long macAddress = 1;
    private String zoneToken;    
    private Long removed; //timestamp
    private AllocationState allocationState;
    private String uuid;    
    boolean securityGroupEnabled;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDns1() {
        return dns1;
    }

    public void setDns1(String dns1) {
        this.dns1 = dns1;
    }

    public String getDns2() {
        return dns2;
    }

    public void setDns2(String dns2) {
        this.dns2 = dns2;
    }

    public String getInternalDns1() {
        return internalDns1;
    }

    public void setInternalDns1(String internalDns1) {
        this.internalDns1 = internalDns1;
    }

    public String getInternalDns2() {
        return internalDns2;
    }

    public void setInternalDns2(String internalDns2) {
        this.internalDns2 = internalDns2;
    }

    public String getRouterMacAddress() {
        return routerMacAddress;
    }

    public void setRouterMacAddress(String routerMacAddress) {
        this.routerMacAddress = routerMacAddress;
    }

    public String getGuestNetworkCidr() {
        return guestNetworkCidr;
    }

    public void setGuestNetworkCidr(String guestNetworkCidr) {
        this.guestNetworkCidr = guestNetworkCidr;
    }

    public Long getDomainId() {
        return domainId;
    }

    public void setDomainId(Long domainId) {
        this.domainId = domainId;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public NetworkType getNetworkType() {
        return networkType;
    }

    public void setNetworkType(NetworkType networkType) {
        this.networkType = networkType;
    }

    public String getDnsProvider() {
        return dnsProvider;
    }

    public void setDnsProvider(String dnsProvider) {
        this.dnsProvider = dnsProvider;
    }

    public String getDhcpProvider() {
        return dhcpProvider;
    }

    public void setDhcpProvider(String dhcpProvider) {
        this.dhcpProvider = dhcpProvider;
    }

    public String getGatewayProvider() {
        return gatewayProvider;
    }

    public void setGatewayProvider(String gatewayProvider) {
        this.gatewayProvider = gatewayProvider;
    }

    public String getVpnProvider() {
        return vpnProvider;
    }

    public void setVpnProvider(String vpnProvider) {
        this.vpnProvider = vpnProvider;
    }

    public String getUserDataProvider() {
        return userDataProvider;
    }

    public void setUserDataProvider(String userDataProvider) {
        this.userDataProvider = userDataProvider;
    }

    public String getLoadBalancerProvider() {
        return loadBalancerProvider;
    }

    public void setLoadBalancerProvider(String loadBalancerProvider) {
        this.loadBalancerProvider = loadBalancerProvider;
    }

    public String getFirewallProvider() {
        return firewallProvider;
    }

    public void setFirewallProvider(String firewallProvider) {
        this.firewallProvider = firewallProvider;
    }

    public long getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(long macAddress) {
        this.macAddress = macAddress;
    }

    public String getZoneToken() {
        return zoneToken;
    }

    public void setZoneToken(String zoneToken) {
        this.zoneToken = zoneToken;
    }

    public Long getRemoved() {
        return removed;
    }

    public void setRemoved(Long removed) {
        this.removed = removed;
    }

    public AllocationState getAllocationState() {
        return allocationState;
    }

    public void setAllocationState(AllocationState allocationState) {
        this.allocationState = allocationState;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public boolean isSecurityGroupEnabled() {
        return securityGroupEnabled;
    }

    public void setSecurityGroupEnabled(boolean securityGroupEnabled) {
        this.securityGroupEnabled = securityGroupEnabled;
    }

}
