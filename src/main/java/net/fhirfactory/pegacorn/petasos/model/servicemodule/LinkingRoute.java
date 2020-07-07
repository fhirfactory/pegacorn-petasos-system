/*
 * Copyright (c) 2020 Mark A. Hunter
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.fhirfactory.pegacorn.petasos.model.servicemodule;

import net.fhirfactory.pegacorn.common.model.FDNToken;

public class LinkingRoute {
    private FDNToken fromEndpoint;
    private FDNToken toEndpoint;
    private FDNToken routeToken;
    private String routeName;
    private LinkingRouteStatusEnum routeStatus;
    private LinkingRouteTypeEnum routeType;

    public FDNToken getFromEndpoint() {
        return fromEndpoint;
    }

    public void setFromEndpoint(FDNToken fromEndpoint) {
        this.fromEndpoint = fromEndpoint;
    }

    public FDNToken getToEndpoint() {
        return toEndpoint;
    }

    public void setToEndpoint(FDNToken toEndpoint) {
        this.toEndpoint = toEndpoint;
    }

    public FDNToken getRouteToken() {
        return routeToken;
    }

    public void setRouteToken(FDNToken routeToken) {
        this.routeToken = routeToken;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public LinkingRouteStatusEnum getRouteStatus() {
        return routeStatus;
    }

    public void setRouteStatus(LinkingRouteStatusEnum routeStatus) {
        this.routeStatus = routeStatus;
    }

    public LinkingRouteTypeEnum getRouteType() {
        return routeType;
    }

    public void setRouteType(LinkingRouteTypeEnum routeType) {
        this.routeType = routeType;
    }
}
