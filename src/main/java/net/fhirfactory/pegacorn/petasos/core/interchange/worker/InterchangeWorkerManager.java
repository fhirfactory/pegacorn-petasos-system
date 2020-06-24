/*
 * Copyright (c) 2020 MAHun
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

package net.fhirfactory.pegacorn.petasos.core.interchange.worker;

import java.util.HashSet;
import net.fhirfactory.pegacorn.petasos.core.interchange.broker.PetasosProxyAgent;

import java.util.Set;
import javax.enterprise.context.ApplicationScoped;

import net.fhirfactory.pegacorn.common.model.FDN;
import net.fhirfactory.pegacorn.petasos.core.model.interchange.InterchangeDistributionPoint;
import net.fhirfactory.pegacorn.petasos.core.model.interchange.InterchangeRouteMap;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Mark A. Hunter
 * @since 07-Jun-2020
 * 
**/

@ApplicationScoped
public class InterchangeWorkerManager{
    private static final Logger LOG = LoggerFactory.getLogger(InterchangeWorkerManager.class);
    
//    private LinkedHashSet<ParcelFinalisationMap> parcelStatusSet;
    private PetasosProxyAgent proxyAgent;
    private InterchangeRouteMap routeMap;
    private Set<InterchangeDistributionPoint> distributionPointSet;
    
    private Set<RouteBuilder> intersectionRouteBulders = new HashSet<RouteBuilder>();

    public InterchangeWorkerManager(InterchangeRouteMap routeMap, Set<InterchangeDistributionPoint> distributionPoints ){
//        this.parcelStatusSet = new LinkedHashSet<ParcelFinalisationMap>();
        this.proxyAgent = new PetasosProxyAgent();
        this.routeMap = routeMap;
        this.distributionPointSet = distributionPoints;
    }
    
    public void registerIngresParcel(FDN producingWUPFDN, FDN parcelFDN, Set<FDN> uowTypeFDNSet){
        
    }
    
    public void registerSucceedingParcel( String routeID, FDN newParcelFDN){
        
    }
    
    public String generateIntersectionRoutes(String endPoint){
        Set<RouteBuilder> intersectionRouteBuilders = new HashSet<RouteBuilder>();
               String newString = new String();
        return(newString);
    }
}
