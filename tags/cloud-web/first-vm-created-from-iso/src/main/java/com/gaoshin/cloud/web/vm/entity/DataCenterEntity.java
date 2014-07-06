package com.gaoshin.cloud.web.vm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.gaoshin.cloud.web.vm.bean.AllocationState;
import com.gaoshin.cloud.web.vm.bean.NetworkType;

@Entity
@Table
public class DataCenterEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

    @Column
    private String name = null;
    
    @Lob
    private String description = null;
    
    @Column
    private String dns1 = null;
    
    @Column
    private String dns2 = null;
    
    @Column
    private String internalDns1 = null;
    
    @Column
    private String internalDns2 = null;
    
    @Column(updatable = false, nullable=false)
    private String routerMacAddress = "02:00:00:00:00:01";
    
    @Column
    private String guestNetworkCidr = null;
    
    @Column
    private Long domainId = null;

    @Column
    private String domain;
    
    @Column
    @Enumerated(EnumType.STRING) 
    private NetworkType networkType;
    
    @Column
    private String dnsProvider;
    
    @Column
    private String dhcpProvider;
    
    @Column
    private String gatewayProvider;
    
    @Column
    private String vpnProvider;
    
    @Column
    private String userDataProvider;
    
    @Column
    private String loadBalancerProvider;
    
    @Column
    private String firewallProvider;
    
    @Column(updatable = false, nullable=false)
    //@TableGenerator(name="mac_address_sq", table="data_center", pkColumnName="id", valueColumnName="mac_address", allocationSize=1)
    private long macAddress = 1;
    
    @Column
    private String zoneToken;    
    
    @Column
    private Long removed; //timestamp
    
    @Column
    @Enumerated(value=EnumType.STRING)
    private AllocationState allocationState;

    @Column
    private String uuid;    
    
    @Column
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
