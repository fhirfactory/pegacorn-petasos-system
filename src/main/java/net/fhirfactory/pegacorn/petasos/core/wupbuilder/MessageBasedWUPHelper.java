/*
 * Copyright (c) 2020 mhunter
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

package net.fhirfactory.pegacorn.petasos.core.wupbuilder;

import net.fhirfactory.pegacorn.common.model.FDNToken;
import net.fhirfactory.pegacorn.common.model.FDNTokenSet;
import net.fhirfactory.pegacorn.petasos.core.processingpathway.interchange.manager.InterchangeIM;
import net.fhirfactory.pegacorn.petasos.core.servicemodule.manager.ServiceModuleIM;
import net.fhirfactory.pegacorn.petasos.core.processingpathway.wupcontainer.manager.WUPContainerIM;
import net.fhirfactory.pegacorn.petasos.model.servicemodule.ElementNameExtensions;
import net.fhirfactory.pegacorn.petasos.model.servicemodule.MapElement;
import net.fhirfactory.pegacorn.petasos.model.servicemodule.MapElementTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Iterator;


@ApplicationScoped
public class MessageBasedWUPHelper {
    private static final Logger LOG = LoggerFactory.getLogger(MessageBasedWUPHelper.class);

    @Inject
    ServiceModuleIM moduleIM;

    @Inject
    InterchangeIM interchangeIM;

    @Inject
    WUPContainerIM wupContainerIM;

    @Inject
    ElementNameExtensions nameExtensions;

    public void registerWUP(FDNToken wupID){
        LOG.debug(".registerWUP(): Entry, wupID --> {}", wupID );
        // First we register our WUP
        LOG.trace(".registerWUP(): Add the Work Unit Processor to the Service Module map");
        MapElement newWUP = new MapElement(wupID, MapElementTypeEnum.WORK_UNIT_PROCESSOR);
        moduleIM.addElement(newWUP);

        // Then, we add the WUPContainer Elements
        LOG.trace(".registerWUP(): Building the surrounding WUPContainer for this WUP");
        wupContainerIM.buildWUPContainer(newWUP);

        // Now, we connect the WUP/WUPContainer into the Interchange framework
        LOG.trace(".registerWUP(): Connecting into Interchange framework");
        interchangeIM.connectToInterchangeFramework(newWUP);

        LOG.debug(".registerWUP(): Exit, wupID --> {}", wupID );
    }

    public void subscribeToUnitsOfWork(FDNToken wupID, FDNTokenSet uowTypeSet){
        LOG.debug(".subscribeToUnitsOfWork(): Entry, uowTypeSet --> {}", uowTypeSet );
        if( uowTypeSet == null){
            throw( new IllegalArgumentException(".subscribeToUnitsOfWork(): uowTypeSet was null"));
        }
        if(uowTypeSet.isEmpty()){
            return;
        }
        Iterator<FDNToken> uowTypeIterator = uowTypeSet.getElements().iterator();
        while(uowTypeIterator.hasNext()){
            FDNToken currentUoWTypeID = uowTypeIterator.next();
            interchangeIM.associateWUP2UoWType(wupID, currentUoWTypeID);
        }
    }
}
