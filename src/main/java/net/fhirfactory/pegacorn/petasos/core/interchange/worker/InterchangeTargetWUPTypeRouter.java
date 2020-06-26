/*
 * Copyright (c) 2020 Mark A. Hunter (ACT Health)
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

import net.fhirfactory.pegacorn.common.model.FDN;
import net.fhirfactory.pegacorn.common.model.FDNToken;
import net.fhirfactory.pegacorn.common.model.FDNTokenSet;
import net.fhirfactory.pegacorn.petasos.core.servicemodule.cache.PayloadToWUPDistributionList;
import net.fhirfactory.pegacorn.petasos.model.uow.UoW;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

/**
 * @author Mark A. Hunter
 */
public class InterchangeTargetWUPTypeRouter {
    private static final Logger LOG = LoggerFactory.getLogger(InterchangeTargetWUPTypeRouter.class);

    private static final String CURRENT_END_POINT_SET = "CurrentEndpointSetFor";

    @Inject
    PayloadToWUPDistributionList distributionList;

    String forwardUoW2WUPs(UoW incomingTraffic, Exchange camelExchange) {
        LOG.debug(".forwardUoW2WUPs(): Entry, incomingTraffic --> {}, camelExchange --> ###", incomingTraffic);
        FDN currentUoWID = incomingTraffic.getUowTypeID();
        String propertyName = CURRENT_END_POINT_SET+currentUoWID.getUnqualifiedRDN().getNameValue();
        FDNTokenSet targetWUPSet = camelExchange.getProperty(propertyName, FDNTokenSet.class);
        boolean alreadyInstalled = true;
        if(targetWUPSet == null){
            alreadyInstalled = false;
            targetWUPSet = distributionList.getDistrubtionForPayload(incomingTraffic.getUowTypeID().getToken());
            if(targetWUPSet == null){
                LOG.debug(".forwardUoW2WUPs(): Exit, nobody was interested in processing this UoW");
                return(null);
            }
        }
        if(targetWUPSet.isEmpty()){
            LOG.debug(".forwardUoW2WUPs(): Exit, finished iterating through interested/registered endpoints");
            return(null);
        }
        if(alreadyInstalled){
            camelExchange.removeProperty(propertyName);
        }
        FDNToken thisOne = targetWUPSet.getElements().iterator().next();
        FDN thisIterationEndPoint = new FDN(thisOne);
        targetWUPSet.removeElement(thisOne);
        camelExchange.setProperty(propertyName, targetWUPSet);
        String endpointDetail = thisIterationEndPoint.getUnqualifiedToken();
        LOG.debug(".forwardUoW2WUPs(): Exiting, returning another registered/interested endpoint: endpointDetail -->{}", endpointDetail);
        return(endpointDetail);
    }
}
