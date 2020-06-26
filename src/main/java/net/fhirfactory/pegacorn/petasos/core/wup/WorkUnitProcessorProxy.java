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

package net.fhirfactory.pegacorn.petasos.core.wup;

import net.fhirfactory.pegacorn.common.model.FDN;
import net.fhirfactory.pegacorn.common.model.FDNToken;
import net.fhirfactory.pegacorn.common.model.RDN;
import net.fhirfactory.pegacorn.petasos.core.interchange.manager.InterchangeManager;
import net.fhirfactory.pegacorn.petasos.core.servicemodule.manager.ServiceModuleIM;
import net.fhirfactory.pegacorn.petasos.core.wupcontainer.manager.WUPContainerManager;
import net.fhirfactory.pegacorn.petasos.model.servicemodule.MapElement;
import net.fhirfactory.pegacorn.petasos.model.servicemodule.MapElementTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;


@ApplicationScoped
public class WorkUnitProcessorProxy {
    private static final Logger LOG = LoggerFactory.getLogger(WorkUnitProcessorProxy.class);

    @Inject
    ServiceModuleIM moduleIM;

    @Inject
    InterchangeManager interchangeManager;

    @Inject
    WUPContainerManager wupContainerManager;

    public void registerWUP(FDNToken wupID){
        LOG.debug(".registerWUP(): Entry, wupID --> {}", wupID );
        // First we register our WUP
        LOG.trace(".registerWUP(): Add the Work Unit Processor to the Service Module map");
        MapElement newElement = new MapElement(wupID, MapElementTypeEnum.WORK_UNIT_PROCESSOR);
        moduleMap.addElement(newElement);

        // Then, we add the Ingres WUPContainer (associated to the WUP) to the ServiceModule map
        LOG.trace(".registerWUP(): Add the associated Ingres WUPContainer to the Service Module map");
        FDN wupFDN = new FDN(wupID);
        RDN wupRDN = wupFDN.getUnqualifiedRDN();
        String ingresWUPContainerNameValue = wupRDN.getNameValue() + "." + nameExtensions.getProcessorWupContainerIngresProcessor();
        FDN ingresWUPContainerID = new FDN(wupFDN);
        ingresWUPContainerID.appendRDN(new RDN("WUPContainer", ingresWUPContainerNameValue));

        MapElement newWUPContainerIngresElement = new MapElement(ingresWUPContainerID.getToken(), MapElementTypeEnum.WUP_CONTAINER_INGRES_PROCESSOR);
        moduleMap.addElement(newWUPContainerIngresElement);
        // Now, add the Egress Contraption (associated to the WUP) to the ServiceModule map
        LOG.trace(".registerWUP(): Add the associated Egress Contraptions to the Service Module map");
        FDN egressContraptionFDN = new FDN(wupFDN);
        String egressContraptionNameValue = wupRDN.getNameValue()+".Egress";
        egressContraptionFDN.appendRDN(new RDN("Contraption", egressContraptionNameValue));
        MapElement newEgressContraptionElement = new MapElement(egressContraptionFDN.getToken(), MapElementTypeEnum.CONTRAPTION_EGRESS);
        moduleMap.addElement(newEgressContraptionElement);
        // Now lets build all the Routes between the WUP and the various Contraption Elements
        LOG.trace(".registerWUP(): Add the associated Routes between the WUP and Contraptions");
        RDN
        //
    }
}
