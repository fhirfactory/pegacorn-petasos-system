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


package net.fhirfactory.pegacorn.petasos.core.servicemodule.manager;

import net.fhirfactory.pegacorn.common.model.FDN;
import net.fhirfactory.pegacorn.common.model.FDNToken;
import net.fhirfactory.pegacorn.petasos.core.servicemodule.cache.ServiceModuleMapDM;
import net.fhirfactory.pegacorn.petasos.model.servicemodule.ElementNameExtensions;
import net.fhirfactory.pegacorn.petasos.model.servicemodule.LinkingRoute;
import net.fhirfactory.pegacorn.petasos.model.servicemodule.MapElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * This class WILL do more in the future, but it is for now just a proxy to the
 * ServiceModuleMapDM.
 */
@ApplicationScoped
public class ServiceModuleIM {
    private static final Logger LOG = LoggerFactory.getLogger(ServiceModuleIM.class);

    private static final Integer WUA_RETRY_LIMIT = 3;
    private static final Integer WUA_TIMEOUT_LIMIT = 10000; // 10 Seconds
    private static final Integer WUA_SLEEP_INTERVAL = 250; // 250 milliseconds
    private static final Integer WUA_CLEAN_UP_AGE_LIMIT = 60*1000; // 60 Seconds

    @Inject
    ServiceModuleMapDM moduleMapDM;

    @Inject
    ElementNameExtensions nameExtensions;

    public void registerElement(MapElement newElement) {
        LOG.debug(".registerElement(): Entry, newElement --> {}", newElement);
        moduleMapDM.registerElement(newElement);
    }

    public void unregisterElement(FDNToken elementID) {
        LOG.debug(".unregisterElement(): Entry, elementID --> {}", elementID);
        moduleMapDM.unregisterRoute(elementID);
    }

    public Set<MapElement> getElementSet() {
        LOG.debug(".getElementSet(): Entry");
        return(moduleMapDM.getElementSet());
    }

    public MapElement getElement(FDNToken elementID){
        LOG.debug(".getElement(): Entry, elementID --> {}", elementID);
        return(moduleMapDM.getElement(elementID));
    }

    public void registerCamelRoute(LinkingRoute newRoute) {
        LOG.debug(".registerRoute(): Entry, newRoute --> {}", newRoute);
        moduleMapDM.registerRoute(newRoute);
    }

    public void unregisterRoute(FDNToken routeID) {
        LOG.debug(".unregisterRoute(): Entry, routeID --> {}", routeID);
        moduleMapDM.unregisterRoute(routeID);
    }

    public Set<LinkingRoute> getRouteSet() {
        LOG.debug(".getRouteSet(): Entry");
        return(moduleMapDM.getRouteSet());
    }

    public LinkingRoute getRoute(FDNToken routeID){
        LOG.debug(".getRoute(): Entry, routeID --> {}", routeID);
        return(moduleMapDM.getRoute(routeID));
    }

    public FDN getModuleFunctionalContext(){
        FDN myFDN = new FDN();
        return(myFDN);
    }

    public FDN getModuleDeploymentContext(){
        FDN myFDN = new FDN();
        return(myFDN);
    }

    public Integer getWorkUnitActivityRetryLimit(){
        return(WUA_RETRY_LIMIT);
    }

    public Integer getWorkUnitActivityTimeoutLimit(){
        return(WUA_TIMEOUT_LIMIT);
    }

    public Integer getWorkUnitActivitySleepInterval(){
        return(WUA_SLEEP_INTERVAL);
    }

    public Integer getWorkUnitActivityCleanUpAgeLimit(){
        return(WUA_CLEAN_UP_AGE_LIMIT);
    }

}
