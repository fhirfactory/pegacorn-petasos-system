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

package net.fhirfactory.pegacorn.petasos.wup.common;

import net.fhirfactory.pegacorn.common.model.FDN;
import net.fhirfactory.pegacorn.common.model.FDNToken;
import net.fhirfactory.pegacorn.common.model.RDN;
import net.fhirfactory.pegacorn.petasos.core.PetasosServicesBroker;
import net.fhirfactory.pegacorn.petasos.core.servicemodule.manager.ServiceModuleIM;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.time.Instant;
import java.util.Date;

public abstract class GenericWUPTemplate extends RouteBuilder {
    private static final Logger LOG = LoggerFactory.getLogger(GenericWUPTemplate.class);

    private FDNToken wupTypeID;
    private FDNToken wupInstanceID;

    @Inject
    public ServiceModuleIM moduleIM;

    @Inject
    public PetasosServicesBroker servicesBroker;

    public GenericWUPTemplate(){
        super();
        FDN serviceModuleTypeFDN = moduleIM.getModuleFunctionalContext();
        FDN wupTypeFDN = new FDN(serviceModuleTypeFDN);
        wupTypeFDN.appendRDN(getProcessingPlantRDN());
        wupTypeFDN.appendRDN(getWorkUnitProcessorRDN());
        this.wupTypeID = wupTypeFDN.getToken();
        FDN serviceModuleDeploymentFDN = moduleIM.getModuleDeploymentContext();
        FDN wupInstanceFDN = new FDN(serviceModuleDeploymentFDN);
        wupInstanceFDN.appendRDN(getProcessingPlantRDN());
        wupInstanceFDN.appendRDN(getWorkUnitProcessorRDN());
        RDN instanceQualifier = new RDN("InstanceQualifier", Date.from(Instant.now()).toString());
        wupInstanceFDN.appendRDN(instanceQualifier);
        this.wupInstanceID = wupInstanceFDN.getToken();
    }

    public abstract RDN getProcessingPlantRDN();
    public abstract RDN getWorkUnitProcessorRDN();

    public FDNToken getWupTypeID(){return(this.wupTypeID);}
    public FDNToken getWupInstanceID(){return(this.wupInstanceID);}
}
