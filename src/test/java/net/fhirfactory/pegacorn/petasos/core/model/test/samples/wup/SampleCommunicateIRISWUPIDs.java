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

package net.fhirfactory.pegacorn.petasos.core.model.test.samples.wup;

import net.fhirfactory.pegacorn.common.model.FDN;
import net.fhirfactory.pegacorn.common.model.RDN;

public class SampleCommunicateIRISWUPIDs {
    public FDN getIrisWildflyInstanceFDN(){
        FDN irisWildflyFDN = new FDN();
        irisWildflyFDN.appendRDN(new RDN("System", "Pegacorn-Test(1.0.0-SNAPSHOT)"));
        irisWildflyFDN.appendRDN(new RDN("Subsystem", "Communicate(1.0.0-SNAPSHOT)"));
        irisWildflyFDN.appendRDN(new RDN("Service", "gen0-iris(1.0.0-SNAPSHOT)"));
        irisWildflyFDN.appendRDN(new RDN("Site", "DC1"));
        irisWildflyFDN.appendRDN(new RDN("Pod", "PodStuff1"));
        return(irisWildflyFDN);
    }

    public FDN getIrisTypeFDN(){
        FDN irisWildflyFDN = new FDN();
        irisWildflyFDN.appendRDN(new RDN("System", "Pegacorn-Test(1.0.0-SNAPSHOT)"));
        irisWildflyFDN.appendRDN(new RDN("Subsystem", "Communicate(1.0.0-SNAPSHOT)"));
        irisWildflyFDN.appendRDN(new RDN("Service", "gen0-iris(1.0.0-SNAPSHOT)"));
        return(irisWildflyFDN);
    }

    public FDN getSynapseReceiveWUPTypeID(){
        FDN ingresPointWUPTypeID = getIrisTypeFDN();
        ingresPointWUPTypeID.appendRDN(new RDN("ProcessingFactory","communicate-iris"));
        ingresPointWUPTypeID.appendRDN(new RDN("ProcessingPlant", "MatrixEvent2FHIRBundle"));
        ingresPointWUPTypeID.appendRDN(new RDN("ProcessingEngine", "SynapseEventCapture"));
        return(ingresPointWUPTypeID);
    }

    public FDN getRoomIM2CommunicationWUPTypeID(){
        FDN newWUPTypeID = getIrisTypeFDN();
        newWUPTypeID.appendRDN(new RDN("ProcessingFactory","communicate-iris-matrix-event-ingestion"));
        newWUPTypeID.appendRDN(new RDN("ProcessingPlant", "m.room.message2Communication"));
        newWUPTypeID.appendRDN(new RDN("ProcessingEngine", "m.room.message2Communication"));
        return(newWUPTypeID);
    }

    public FDN getRoomIM2GroupWUPTypeID(){
        FDN newWUPTypeID = getIrisTypeFDN();
        newWUPTypeID.appendRDN(new RDN("ProcessingFactory","communicate-iris"));
        newWUPTypeID.appendRDN(new RDN("ProcessingPlant", "MatrixEvent2FHIRBundle"));
        newWUPTypeID.appendRDN(new RDN("ProcessingEngine", "m.room.message2Group"));
        return(newWUPTypeID);
    }

    public FDN getCommunicationBundleStagingWUPTypeID(){
        FDN newWUPTypeID = getIrisTypeFDN();
        newWUPTypeID.appendRDN(new RDN("ProcessingFactory","communicate-iris"));
        newWUPTypeID.appendRDN(new RDN("ProcessingPlant", "MatrixEvent2FHIRBundle"));
        newWUPTypeID.appendRDN(new RDN("ProcessingEngine", "Communication2Bundle"));
        return(newWUPTypeID);
    }

    public FDN getGroupBundleStagingWUPTypeID(){
        FDN newWUPTypeID = getIrisTypeFDN();
        newWUPTypeID.appendRDN(new RDN("ProcessingFactory","communicate-iris"));
        newWUPTypeID.appendRDN(new RDN("ProcessingPlant", "MatrixEvent2FHIRBundle"));
        newWUPTypeID.appendRDN(new RDN("ProcessingEngine", "Group2Bundle"));
        return(newWUPTypeID);
    }

    public FDN getSynapseReceiveWUPInstanceID(){
        FDN ingresPointWUPTypeID = getIrisWildflyInstanceFDN();
        ingresPointWUPTypeID.appendRDN(new RDN("ProcessingFactory","communicate-iris"));
        ingresPointWUPTypeID.appendRDN(new RDN("ProcessingPlant", "MatrixEvent2FHIRBundle"));
        ingresPointWUPTypeID.appendRDN(new RDN("ProcessingEngine", "SynapseEventCapture"));
        return(ingresPointWUPTypeID);
    }

    public FDN getRoomIM2CommunicationWUPInstanceID(){
        FDN newWUPTypeID = getIrisWildflyInstanceFDN();
        newWUPTypeID.appendRDN(new RDN("ProcessingFactory","communicate-iris-matrix-event-ingestion"));
        newWUPTypeID.appendRDN(new RDN("ProcessingPlant", "m.room.message2Communication"));
        newWUPTypeID.appendRDN(new RDN("ProcessingEngine", "m.room.message2Communication"));
        return(newWUPTypeID);
    }

    public FDN getRoomIM2GroupWUPInstanceID(){
        FDN newWUPTypeID = getIrisWildflyInstanceFDN();
        newWUPTypeID.appendRDN(new RDN("ProcessingFactory","communicate-iris"));
        newWUPTypeID.appendRDN(new RDN("ProcessingPlant", "MatrixEvent2FHIRBundle"));
        newWUPTypeID.appendRDN(new RDN("ProcessingEngine", "m.room.message2Group"));
        return(newWUPTypeID);
    }

    public FDN getCommunicationBundleStagingWUPInstanceID(){
        FDN newWUPTypeID = getIrisWildflyInstanceFDN();
        newWUPTypeID.appendRDN(new RDN("ProcessingFactory","communicate-iris"));
        newWUPTypeID.appendRDN(new RDN("ProcessingPlant", "MatrixEvent2FHIRBundle"));
        newWUPTypeID.appendRDN(new RDN("ProcessingEngine", "Communication2Bundle"));
        return(newWUPTypeID);
    }

    public FDN getGroupBundleStagingWUPInstanceID(){
        FDN newWUPTypeID = getIrisWildflyInstanceFDN();
        newWUPTypeID.appendRDN(new RDN("ProcessingFactory","communicate-iris"));
        newWUPTypeID.appendRDN(new RDN("ProcessingPlant", "MatrixEvent2FHIRBundle"));
        newWUPTypeID.appendRDN(new RDN("ProcessingEngine", "Group2Bundle"));
        return(newWUPTypeID);
    }

}
