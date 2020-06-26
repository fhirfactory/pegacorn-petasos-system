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

package net.fhirfactory.pegacorn.petasos.model.wupcontainer;

import net.fhirfactory.pegacorn.common.model.FDN;
import net.fhirfactory.pegacorn.common.model.RDN;
import net.fhirfactory.pegacorn.petasos.core.agent.PetasosWUPContainerAgent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WUPContainerDefinition {
    private static final Logger LOG = LoggerFactory.getLogger(WUPContainerDefinition.class);
    private FDN associatedWUPInstanceID;
    private FDN associatedWUPTypeID;
    private FDN instanceID;
    private String ingresIngresPoint;
    private String ingresEgressPoint;
    private String egressIngresPoint;
    private String egressEgressPoint;

    private PetasosWUPContainerAgent nodeAgent;

    public WUPContainerDefinition(FDN associatedWUPTypeID, FDN associatedWUPID){
        this.associatedWUPInstanceID = new FDN(associatedWUPID);
        this.associatedWUPTypeID = new FDN(associatedWUPTypeID);
        this.instanceID = new FDN(associatedWUPTypeID);
        this.instanceID.appendRDN(new RDN("Contraption", associatedWUPID.getUnqualifiedRDN().getNameValue()));
        this.ingresEgressPoint = associatedWUPInstanceID.getUnqualifiedToken()+"<WUPIngresPoint>";
        this.egressIngresPoint = associatedWUPInstanceID.getUnqualifiedToken()+"<WUPEgressPoint>";
        this.ingresIngresPoint = this.instanceID.getUnqualifiedToken()+"<ContraptionIngresPoint>";
        this.egressEgressPoint = this.instanceID.getUnqualifiedToken()+"<ContraptionEgressPoint>";
    }

    public FDN getAssociatedWUPInstanceID() {
        return associatedWUPInstanceID;
    }

    public FDN getAssociatedWUPTypeID() {
        return associatedWUPTypeID;
    }

    public FDN getInstanceID() {
        return instanceID;
    }

    public String getIngresIngresPoint() {
        return ingresIngresPoint;
    }

    public String getIngresEgressPoint() {
        return ingresEgressPoint;
    }

    public String getEgressIngresPoint() {
        return egressIngresPoint;
    }

    public String getEgressEgressPoint() {
        return egressEgressPoint;
    }
}
