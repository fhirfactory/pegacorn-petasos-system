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

package net.fhirfactory.pegacorn.petasos.core.processingpathway.wupcontainer.worker;

import com.sun.corba.se.spi.orbutil.threadpool.Work;
import net.fhirfactory.pegacorn.common.model.FDNToken;
import net.fhirfactory.pegacorn.petasos.core.processingpathway.naming.RouteElementNames;
import net.fhirfactory.pegacorn.petasos.model.pathway.WorkUnitTransportPacket;
import net.fhirfactory.pegacorn.petasos.model.resilience.activitymatrix.ParcelStatusElement;
import net.fhirfactory.pegacorn.petasos.model.uow.UoW;
import net.fhirfactory.pegacorn.petasos.model.wup.WUPJobCard;
import org.apache.camel.Exchange;

import java.time.Instant;
import java.util.Date;

public class WUPEgressConduit {
    public WorkUnitTransportPacket forwardIntoWUP(UoW incomingUoW, Exchange camelExchange, FDNToken wupTypeID, FDNToken wupInstanceID){
        RouteElementNames elementNames = new RouteElementNames(wupTypeID);
        WUPJobCard jobCard = camelExchange.getProperty("WUPJobCard", WUPJobCard.class);
        ParcelStatusElement statusElement = camelExchange.getProperty("ParcelStatusElement", ParcelStatusElement.class);
        WorkUnitTransportPacket transportPacket = new WorkUnitTransportPacket(elementNames.getEndPointWUPEgressConduitEgress(), Date.from(Instant.now()),incomingUoW);
        transportPacket.setCurrentJobCard(jobCard);
        transportPacket.setCurrentParcelStatus(statusElement);
        transportPacket.setToElement(elementNames.getEndPointWUPContainerEgressProcessorIngres());
        return(transportPacket);
    }
}
