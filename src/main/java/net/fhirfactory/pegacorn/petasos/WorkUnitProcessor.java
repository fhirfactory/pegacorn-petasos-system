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

package net.fhirfactory.pegacorn.petasos;

import net.fhirfactory.pegacorn.common.model.FDN;
import net.fhirfactory.pegacorn.common.model.RDN;
import net.fhirfactory.pegacorn.petasos.core.model.wup.WorkUnitProcessorJobCard;
import net.fhirfactory.pegacorn.petasos.core.model.parcel.PetasosParcel;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

public abstract class WorkUnitProcessor extends RouteBuilder {
    private static final Logger LOG = LoggerFactory.getLogger(WorkUnitProcessor.class);

    private FDN wupTypeID = null;
    private FDN wupInstanceID = null;
    private WorkUnitProcessorJobCard wupInstanceJobCard;
    private PetasosParcel currentParcelOfWork;
    private String wupEgressPoint = null;
    private String wupIngresPoint= null;

    final protected void register(FDN myWUPTypeID){
        this.wupTypeID = new FDN(myWUPTypeID);
        FDN newWUPInstanceID = new FDN(myWUPTypeID);
        newWUPInstanceID.appendRDN(new RDN("InstanceQualifier", Integer.toHexString(Instant.now().getNano())));
        this.wupInstanceID = newWUPInstanceID;
        wupEgressPoint = new String(wupInstanceID.getUnqualifiedToken()+"<WUPEgressPoint>");
        wupIngresPoint = new String(wupInstanceID.getUnqualifiedToken()+"<WUPIngresPoint>");

    }

    protected String ingresFeed(){
        return(this.wupIngresPoint);
    }

    protected String egressFeed(){
        return(this.wupEgressPoint);
    }
}
