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

package net.fhirfactory.pegacorn.petasos.core.model.test.samples.parcel;

import net.fhirfactory.pegacorn.common.model.FDN;
import net.fhirfactory.pegacorn.common.model.RDN;
import net.fhirfactory.pegacorn.petasos.core.model.parcel.PetasosParcel;
import net.fhirfactory.pegacorn.petasos.core.model.test.samples.wup.SampleCommunicateIRISWUPIDs;
import net.fhirfactory.pegacorn.petasos.core.model.uow.UoW;
import net.fhirfactory.pegacorn.petasos.core.model.wup.WorkUnitProcessorActivityStatusEnum;
import net.fhirfactory.pegacorn.petasos.core.model.test.samples.uow.SampleCommunicateIRISUoWSet;

public class SampleCommunicateIRISParcelSet {

    SampleCommunicateIRISUoWSet uowSet = new SampleCommunicateIRISUoWSet();
    SampleCommunicateIRISWUPIDs wupIDSet = new SampleCommunicateIRISWUPIDs();

    public PetasosParcel createSynapseReceiverParcel(){
        UoW ingresUoW = uowSet.createIngresOnlyUoW(uowSet.getDefaultRoomMessage());
        FDN ingresPointWUPTypeID = wupIDSet.getSynapseReceiveWUPTypeID();
        FDN ingresPointWUPInstanceID = wupIDSet.getSynapseReceiveWUPInstanceID();
        ingresPointWUPInstanceID.appendRDN(new RDN("WUP", "SomeUsefulQualifier1"));
        PetasosParcel newParcel = new PetasosParcel(ingresPointWUPInstanceID,ingresPointWUPTypeID,ingresUoW,null, WorkUnitProcessorActivityStatusEnum.WUP_ACTIVITY_STATUS_EXECUTING);
        return(newParcel);
    }
/*
    public PetasosParcel createRoomIMEvent2CommunicationParcel(){
        PetasosParcel newParcel = new PetasosParcel();

        return(newParcel);
    }

    public PetasosParcel createRoomIMEvent2GroupParcel(){
        PetasosParcel newParcel = new PetasosParcel();

        return(newParcel);
    }

    public PetasosParcel createCommunicationStagingParcel(){
        PetasosParcel newParcel = new PetasosParcel();

        return(newParcel);
    }

    public PetasosParcel createGroupStagingParcel(){
        PetasosParcel newParcel = new PetasosParcel();

        return(newParcel);
    }
 */
}
