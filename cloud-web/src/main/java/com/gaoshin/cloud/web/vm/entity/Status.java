package com.gaoshin.cloud.web.vm.entity;

public enum Status {
	Creating(true, false, false),
    Connecting(true, false, false),
    Up(true, false, false),
    Down(true, true, true),
    Disconnected(true, true, true),
    Alert(true, true, true),
    Removed(true, false, true),
    Error(true, false, true),
    Rebalancing(false, false, false);
    
    private final boolean updateManagementServer;
    private final boolean checkManagementServer;
    private final boolean lostConnection;
    
    private Status(boolean updateConnection, boolean checkManagementServer, boolean lostConnection) {
    	this.updateManagementServer = updateConnection;
    	this.checkManagementServer = checkManagementServer;
    	this.lostConnection = lostConnection;
    }
    
    public boolean updateManagementServer() {
    	return updateManagementServer;
    }
    
    public boolean checkManagementServer() {
    	return checkManagementServer;
    }
    
    public boolean lostConnection() {
        return lostConnection;
    }

    public enum Event {
        AgentConnected(false, "Agent connected"),
        PingTimeout(false, "Agent is behind on ping"),
        ShutdownRequested(false, "Shutdown requested by the agent"),
        AgentDisconnected(false, "Agent disconnected"),
        HostDown(false, "Host is found to be down by the investigator"),
        Ping(false, "Ping is received from the host"),
        ManagementServerDown(false, "Management Server that the agent is connected is going down"),
        WaitedTooLong(false, "Waited too long from the agent to reconnect on its own.  Time to do HA"),
        Remove(true, "Host is removed"),
        Ready(false, "Host is ready for commands"),
        RequestAgentRebalance(false, "Request rebalance for the certain host"),
        StartAgentRebalance(false, "Start rebalance for the certain host"),
        RebalanceCompleted(false, "Host is rebalanced successfully"),
        RebalanceFailed(false, "Failed to rebalance the host"),
        Error(false, "An internal error happened");

        private final boolean isUserRequest;
        private final String comment;
        private Event(boolean isUserRequest, String comment) {
        	this.isUserRequest = isUserRequest;
            this.comment = comment;
        }

        public String getDescription() {
            return comment;
        }
        
        public boolean isUserRequest() {
        	return isUserRequest;
        }
    }
}
