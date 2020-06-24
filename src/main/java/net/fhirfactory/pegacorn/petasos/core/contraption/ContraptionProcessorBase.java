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

package net.fhirfactory.pegacorn.petasos.core.contraption;

import net.fhirfactory.pegacorn.common.model.FDN;
import net.fhirfactory.pegacorn.common.model.RDN;
import net.fhirfactory.pegacorn.petasos.core.model.configuration.ServiceModuleInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Map;

public class ContraptionProcessorBase {
    private static final Logger LOG = LoggerFactory.getLogger(ContraptionProcessorBase.class);

    @Inject
    ServiceModuleInterface moduleProperties;

    protected FDN getWUPInstanceIDFromTypeID(FDN wupTypeID){
        LOG.debug(".getWUPInstanceIDFromTypeID(): Entry, wupTypeID --> {}", wupTypeID);
        if( wupTypeID == null){
            throw(new IllegalArgumentException(".getWUPInstanceIDFromTypeID(): WUP Type ID passed in is null"));
        }
        FDN newInstanceID = new FDN();
        if(wupTypeID.getRDNCount() <= 3){
            throw(new IllegalArgumentException(".getWUPInstanceIDFromTypeID(): WUP Type ID does not contain System/Subsystem/Service information"));
        }
        Map<Integer, RDN> rdnSet = wupTypeID.getRDNSet();
        newInstanceID = moduleProperties.getServiceModuleInstanceContext();
        Integer rdnCount = wupTypeID.getRDNCount();
        for(int counter = 3; counter < rdnCount; counter++){
            newInstanceID.appendRDN(rdnSet.get(counter));
        }
        LOG.debug(".getWUPInstanceIDFromTypeID(): Exit, newInstanceID --> {}", newInstanceID);
        return(newInstanceID);
    }
}
