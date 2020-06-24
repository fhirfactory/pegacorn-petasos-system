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
package net.fhirfactory.pegacorn.petasos.core.interchange.broker;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import net.fhirfactory.pegacorn.common.model.FDN;
import net.fhirfactory.pegacorn.petasos.core.model.interchange.InterchangeDistributionPoint;
import net.fhirfactory.pegacorn.petasos.core.model.interchange.InterchangeRouteMap;

/**
 *
 * @author Mark A. Hunter (ACT Health)
 */

@Singleton
public class InterchangeBroker
{
    private InterchangeRouteMap interchangeMap;
    private LinkedHashSet<InterchangeDistributionPoint> distributionPoints;
     private AtomicBoolean busy = new AtomicBoolean(false);


    public InterchangeBroker(){
        this.interchangeMap = new InterchangeRouteMap();
        this.distributionPoints = new LinkedHashSet<InterchangeDistributionPoint>();
    }
    
    // Topic Functions (Private)
    
    private void createUoWContentTypeTopic(String topicName){
        // TODO: Create a topic if a WUP indicates it will push UoW onto it OR
        // a WUP registers that it will receive content from it.
    }
    
    private void createDefaultUoWTypeConsumer(String topicName){
        
    }

    @Lock(LockType.READ)
    public Set<String> getTopicSet() {
        LinkedHashSet<String> currentTopicSet = new LinkedHashSet<String>();
        
        return(currentTopicSet);
    }

    @Lock(LockType.WRITE)
    public String registerWUPAsUoWProducer(FDN wupFDN, FDN wupFunctionFDN) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String registerWUPAsUoWConsumer(FDN wupFDN, FDN wupFunctionFDN, Set<FDN> uowTypeFDN) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void notifySuccessfulParcelRegistration(FDN petasosParcelFDN, FDN uowFDN) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void notifyFailedParcelRegistration(FDN uowFDN) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void petasosAgentProxy(){

    }
    
}
