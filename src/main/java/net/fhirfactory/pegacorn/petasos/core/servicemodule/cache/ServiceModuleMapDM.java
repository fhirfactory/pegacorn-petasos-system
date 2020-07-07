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

package net.fhirfactory.pegacorn.petasos.core.servicemodule.cache;

import net.fhirfactory.pegacorn.common.model.FDNToken;
import net.fhirfactory.pegacorn.petasos.model.servicemodule.LinkingRoute;
import net.fhirfactory.pegacorn.petasos.model.servicemodule.MapElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public class ServiceModuleMapDM {
    private static final Logger LOG = LoggerFactory.getLogger(ServiceModuleMapDM.class);

    private FDNToken serviceModuleID;
    private ConcurrentHashMap<FDNToken, MapElement> elementSet;
    private ConcurrentHashMap<FDNToken, LinkingRoute> routeSet;

    public ServiceModuleMapDM() {
        this.serviceModuleID = null;
        this.elementSet = new ConcurrentHashMap<FDNToken, MapElement>();
        this.routeSet = new ConcurrentHashMap<FDNToken, LinkingRoute>();
    }

    public FDNToken getServiceModuleID() {
        return serviceModuleID;
    }

    public void setServiceModuleID(FDNToken serviceModuleID) {
        this.serviceModuleID = serviceModuleID;
    }

    /**
     * This function adds an entry to the Element Set.
     * <p>
     * Note that the default behaviour is to UPDATE the values with
     * the set if there already exists an instance for the specified
     * FDNToken (identifier).
     *
     * @param newElement The MapElement to be added to the Set
     */
    public void registerElement(MapElement newElement) {
        LOG.debug(".registerElement(): Entry, newElement --> {}", newElement);
        if (newElement == null) {
            throw (new IllegalArgumentException(".registerElement(): newElement is null"));
        }
        if (newElement.getElementID() == null) {
            throw (new IllegalArgumentException(".registerElement(): bad elementID within newElement"));
        }
        if (this.elementSet.containsKey(newElement.getElementID())) {
            this.elementSet.replace(newElement.getElementID(), newElement);
        } else {
            this.elementSet.put(newElement.getElementID(), newElement);
        }
    }

    public void unregisterElement(FDNToken elementID) {
        LOG.debug(".unregisterElement(): Entry, elementID --> {}", elementID);
        if (elementID == null) {
            throw (new IllegalArgumentException(".unregisterElement(): elementID is null"));
        }
        if (this.elementSet.containsKey(elementID)) {
            LOG.trace(".unregisterElement(): Element found, now removing it...");
            this.elementSet.remove(elementID);
        } else {
            LOG.trace(".unregisterElement(): No element with that elementID is in the map");
        }
        LOG.debug(".unregisterElement(): Exit");
    }

    public Set<MapElement> getElementSet() {
        LOG.debug(".getElementSet(): Entry");
        LinkedHashSet<MapElement> elementSet = new LinkedHashSet<MapElement>();
        if (this.elementSet.isEmpty()) {
            LOG.debug(".getElementSet(): Exit, The module map is empty, returning null");
            return (null);
        }
        elementSet.addAll(this.elementSet.values());
        if (LOG.isDebugEnabled()) {
            LOG.debug(".getElementSet(): Exit, returning an element set, size --> {}", elementSet.size());
        }
        return (elementSet);
    }

    public MapElement getElement(FDNToken elementID){
        LOG.debug(".getElement(): Entry, elementID --> {}", elementID);
        if (elementID==null) {
            LOG.debug(".getElement(): Exit, provided a null elementID , so returning null");
            return (null);
        }
        if (this.elementSet.containsKey(elementID)) {
            LOG.trace(".getElement(): Element found!!! WooHoo!");
            MapElement retrievedElement = this.elementSet.get(elementID);
            LOG.debug(".getElement(): Exit, returning element --> {}", retrievedElement);
            return(retrievedElement);
        } else {
            LOG.trace(".getElement(): Couldn't find element!");
            LOG.debug(".getElement(): Exit, returning null as an element with the specified ID was not in the map");
            return(null);
        }
    }

    public void registerRoute(LinkingRoute newRoute) {
        LOG.debug(".registerRoute(): Entry, newRoute --> {}", newRoute);
        if (newRoute == null) {
            throw (new IllegalArgumentException(".registerRoute(): newElement is null"));
        }
        if (newRoute.getRouteToken() == null) {
            throw (new IllegalArgumentException(".registerRoute(): bad Route Token within newRoute"));
        }
        if (this.routeSet.containsKey(newRoute.getRouteToken())) {
            this.routeSet.replace(newRoute.getRouteToken(), newRoute);
        } else {
            this.routeSet.put(newRoute.getRouteToken(), newRoute);
        }
    }

    public void unregisterRoute(FDNToken routeID) {
        LOG.debug(".unregisterRoute(): Entry, routeID --> {}", routeID);
        if (routeID == null) {
            throw (new IllegalArgumentException(".unregisterElement(): routeID is null"));
        }
        if (this.routeSet.containsKey(routeID)) {
            LOG.trace(".unregisterRoute(): Route found, now removing it...");
            this.routeSet.remove(routeID);
        } else {
            LOG.trace(".unregisterRoute(): No route with that routeID is in the map");
        }
        LOG.debug(".unregisterRoute(): Exit");
    }

    public Set<LinkingRoute> getRouteSet() {
        LOG.debug(".getRouteSet(): Entry");
        LinkedHashSet<LinkingRoute> routeSet = new LinkedHashSet<LinkingRoute>();
        if (this.routeSet.isEmpty()) {
            LOG.debug(".getRouteSet(): Exit, The module route set is empty, returning null");
            return (null);
        }
        routeSet.addAll(this.routeSet.values());
        if (LOG.isDebugEnabled()) {
            LOG.debug(".getRouteSet(): Exit, returning an route set, size --> {}", routeSet.size());
        }
        return (routeSet);
    }

    public LinkingRoute getRoute(FDNToken routeID){
        LOG.debug(".getRoute(): Entry, routeID --> {}", routeID);
        if (routeID==null) {
            LOG.debug(".getRoute(): Exit, provided a null routeID , so returning null");
            return (null);
        }
        if (this.elementSet.containsKey(routeID)) {
            LOG.trace(".getRoute(): Route found!!! WooHoo!");
            LinkingRoute retrievedElement = this.routeSet.get(routeID);
            LOG.debug(".getRoute(): Exit, returning route --> {}", retrievedElement);
            return(retrievedElement);
        } else {
            LOG.trace(".getRoute(): Couldn't find element!");
            LOG.debug(".getRoute(): Exit, returning null as an element with the specified ID was not in the map");
            return(null);
        }
    }
}
