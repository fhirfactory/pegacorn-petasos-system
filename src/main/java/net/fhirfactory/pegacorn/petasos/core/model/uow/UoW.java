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
package net.fhirfactory.pegacorn.petasos.core.model.uow;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Instant;

import net.fhirfactory.pegacorn.common.model.FDN;
import net.fhirfactory.pegacorn.common.model.RDN;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mark A. Hunter
 */
public class UoW {
    private static final Logger LOG = LoggerFactory.getLogger(UoW.class);

    public static final String HASH_ATTRIBUTE = "InstanceQualifier";
    /**
     * The FDN (fully distinguished name) of this UoW Type.
     */
    private FDN uowTypeID;
    /**
     * The FDN (fully distinguished name) of this UoW instance. Built from a
     * combination of the required "Component Function FDN" that would be
     * required to process it plus some unique characteristic.
     */
    private FDN uowInstanceID;
    /**
     * The set of (JSON) objects that represent the ingress (or starting set) of
     * information of this UoW.
     */
    private UoWPayloadSet uowIngresContent;
    /**
     * The set of (JSON) objects created as part of the completion of this UoW.
     */
    private UoWPayloadSet uowEgressContent;
    /**
     * The (enum) outcome status of the processing of this UoW.
     */
    private UoWProcessingOutcomeEnum uowProcessingOutcome;

    /**
     * The enclosing PetasosParcel for this UoW Instance.
     */
    private FDN enclosingParcelInstanceID;

    //
    // Constructors
    //

    public UoW(FDN uowTypeID, UoWPayloadSet theInput) {
        LOG.debug(".UoW(): Constructor: functionFDN --> {}, uowTypeFDN --> {}, UoWPayloadSet -->{}", uowTypeID, theInput);
        String generatedInstanceValue = Long.toString(Instant.now().getNano());
        this.uowIngresContent = new UoWPayloadSet(theInput);
        this.uowEgressContent = new UoWPayloadSet();
        this.uowProcessingOutcome = UoWProcessingOutcomeEnum.UOW_OUTCOME_NOTSTARTED;
        this.uowTypeID = new FDN(uowTypeID);
        FDN instanceFDN = new FDN(uowTypeID);
        RDN newRDN = new RDN(HASH_ATTRIBUTE, generatedInstanceValue);
        instanceFDN.appendRDN(newRDN);
        this.uowInstanceID = instanceFDN;
        if (LOG.isTraceEnabled()) {
            LOG.trace("UoW(FDN, FDN, UoWPayloadSet): this.uowTypeFDN --> {}", this.getUowTypeID());
        }
    }

    public UoW(UoW originalUoW) {
        this.uowInstanceID = new FDN(originalUoW.getUoWInstanceID());
        this.uowIngresContent = new UoWPayloadSet(originalUoW.getUowIngresContent());
        this.uowIngresContent = new UoWPayloadSet();
        this.uowIngresContent.getPayloadElements().addAll(originalUoW.getUowIngresContent().getPayloadElements());
        this.uowEgressContent = new UoWPayloadSet();
        this.uowEgressContent.getPayloadElements().addAll(originalUoW.getUowEgressContent().getPayloadElements());
        this.uowProcessingOutcome = originalUoW.getUowProcessingOutcome();
    }

    public UoW(FDN requiredFunctionTypeID, FDN uowTypeID) {
        LOG.debug(".UoW(): Constructor: functionFDN --> {}, uowTypeFDN --> {}, UoWPayloadSet -->{}", requiredFunctionTypeID, uowTypeID);
        String generatedInstanceValue = Long.toString(Instant.now().getNano());
        this.uowTypeID = uowTypeID;
        FDN instanceFDN = new FDN(uowTypeID);
        RDN newRDN = new RDN(HASH_ATTRIBUTE, generatedInstanceValue);
        this.uowInstanceID.appendRDN(newRDN);
    }

    /**
     * @return FDN - the UoW Instance FDN (which will be unique for each UoW within the system).
     */
    public FDN getUoWInstanceID() {
        return uowInstanceID;
    }

    public void setUoWInstanceID(FDN uowID) {
        this.uowInstanceID = uowID;
    }

    /**
     * @return FDN - the UoW Type FDN (which describes the type or context of UoW).
     */
    public FDN getUowTypeID() {
        return this.uowTypeID;
    }

    public void setUoWTypeID(FDN uowTypeID) {
        this.uowTypeID = uowTypeID;
    }

    /**
     *
     * @return UoWPayloadSet - the Ingres Payload Set (captured as a set of JSON Strings).
     */
    public UoWPayloadSet getUowIngresContent() {
        return uowIngresContent;
    }

    public void setUowIngresContent(UoWPayloadSet uowIngresContent) {
        this.uowIngresContent.getPayloadElements().clear();
        this.uowIngresContent.getPayloadElements().addAll(uowIngresContent.getPayloadElements());
    }

    /**
     *
     * @return UoWPayloadSet - the Egress Payload Set (captured as a set of JSON Strings).
     */
    public UoWPayloadSet getUowEgressContent() {
        return uowEgressContent;
    }

    public void setUowEgressContent(UoWPayloadSet uowEgressContent) {
        this.uowEgressContent.getPayloadElements().clear();
        this.uowEgressContent.getPayloadElements().addAll(uowEgressContent.getPayloadElements());
    }

    /**
     *
     * @return UoWProcessingOutcomeEnum - the status of the processing performed on (or applied to) this UoW.
     */
    public UoWProcessingOutcomeEnum getUowProcessingOutcome() {
        return uowProcessingOutcome;
    }

    public void setUowProcessingOutcome(UoWProcessingOutcomeEnum uowProcessingOutcome) {
        this.uowProcessingOutcome = uowProcessingOutcome;
    }


    @Override
    public String toString() {
        LOG.debug("toString(): Entry");

        String uowToString = new String();
        if (uowInstanceID != null) {
            uowToString = uowToString + "UoW [uowInstanceFDN=" + uowInstanceID.toString();
        } else {
            uowToString = uowToString + "UoW [uowInstanceFDN=null";
        }
        if (uowTypeID != null) {
            uowToString = uowToString + ", uowTypeFDN=" + uowTypeID.toString();
        } else {
            uowToString = uowToString + ", uowTypeFDN=null";
        }
        uowToString = uowToString + ", uowIngressContent=" + uowIngresContent.toString()
                + ", uowEgressContent=" + uowEgressContent.toString()
                + ", uowProcessingOutcome=" + uowProcessingOutcome.getUoWProcessingOutcome();
        return (uowToString);
    }

    public String toJSONString() {
        ObjectMapper jsonMapper = new ObjectMapper();
        try {
            String jsonString = jsonMapper.writeValueAsString(this);
            return (jsonString);
        } catch (JsonProcessingException jsonException) {
            // TODO fix the exception handling for JSON String generation
            return (null);
        }
    }

    public FDN getEnclosingParcelInstanceID() {
        return enclosingParcelInstanceID;
    }

    public void setEnclosingParcelInstanceID(FDN enclosingParcelInstanceID) {
        this.enclosingParcelInstanceID = enclosingParcelInstanceID;
    }
}
