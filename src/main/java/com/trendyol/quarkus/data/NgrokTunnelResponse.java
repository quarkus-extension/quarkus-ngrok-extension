package com.trendyol.quarkus.data;

import java.util.ArrayList;
import java.util.List;

public class NgrokTunnelResponse {
    private List<NgrokTunnel> tunnels;

    public NgrokTunnelResponse() {
        this.tunnels = new ArrayList<>();
    }

    public List<NgrokTunnel> getTunnels() {
        return tunnels;
    }

    public void setTunnels(List<NgrokTunnel> tunnels) {
        this.tunnels = tunnels;
    }
}
