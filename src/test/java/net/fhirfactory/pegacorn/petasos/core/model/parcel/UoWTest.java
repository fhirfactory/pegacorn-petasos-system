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

package net.fhirfactory.pegacorn.petasos.core.model.parcel;

import net.fhirfactory.pegacorn.common.model.FDN;
import net.fhirfactory.pegacorn.common.model.FDNToken;
import net.fhirfactory.pegacorn.common.model.RDN;
import net.fhirfactory.pegacorn.petasos.core.model.test.samples.uow.SampleCommunicateIRISUoWSet;
import net.fhirfactory.pegacorn.petasos.core.model.uow.UoW;
import net.fhirfactory.pegacorn.petasos.core.model.uow.UoWPayload;
import net.fhirfactory.pegacorn.petasos.core.model.uow.UoWPayloadSet;
import net.fhirfactory.pegacorn.petasos.core.model.uow.UoWProcessingOutcomeEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * This test-set does NOT have any real coverage, it's mainly to support the end-to-end process design and
 * implementation of the large WUP framework - particularly the implementation of the Intersection
 * capability.
 *
 */

class UoWTest {
    private static final Logger LOG = LoggerFactory.getLogger(UoWTest.class);
    protected UoW ingresObjectOnlyUoW;
    protected UoW ingresPlusSingleEgressObjectUoW;
    protected UoW ingresPlusTwoEgressObjectsUoW;

    SampleCommunicateIRISUoWSet irisUoWSet = new SampleCommunicateIRISUoWSet();

    @BeforeEach
    void setUp() {

        String roomMessage = irisUoWSet.getDefaultRoomMessage();
        ingresObjectOnlyUoW = irisUoWSet.createIngresOnlyUoW(roomMessage);
        ingresPlusSingleEgressObjectUoW = irisUoWSet.createIngresPlusEgresUoW(roomMessage);
        ingresPlusTwoEgressObjectsUoW = irisUoWSet.createIngresPlus2EgressUoW(roomMessage);
    }

    /**
     * This code works in conjunction with the net.fhirfactory.pegacorn.petasos.core.model.test.sampleuows.SampleCommunicateIRISUoWSet
     * class. The uowTypeFDN identified in this test should match that created in the SampleCommunicateIRISUoWSet
     * methods. It is only a positive test to make sure the framework still does the right thing with respect to
     * add/removing UoW Type FDN.
     *
     */
    @Test
    void getUoWTypeFDN() {
        LOG.info("Test: getUoWTypeFDN()");
        FDN communicateUoWTypeFDN = new FDN();
        communicateUoWTypeFDN.appendRDN(new RDN("System", "PegacornTest(1.0.0-SNAPSHOT)"));
        communicateUoWTypeFDN.appendRDN(new RDN("Subsystem", "Communicate(1.0.0-SNAPSHOT)"));
        communicateUoWTypeFDN.appendRDN(new RDN("SubystemCapability", "Iris(1.0.0-SNAPSHOT)"));
        communicateUoWTypeFDN.appendRDN(new RDN("Service", "iris0-communicate(1.0.0-SNAPSHOT)"));
        communicateUoWTypeFDN.appendRDN(new RDN("ProcessingFactory", "MatrixEventBridge"));
        communicateUoWTypeFDN.appendRDN(new RDN("ProcessingPlant", "RoomEventMessage2FHIR"));
        communicateUoWTypeFDN.appendRDN(new RDN("UoW", "matrix::m.room.instant_message-->fhir"));
        String referenceFDN = communicateUoWTypeFDN.toString();
        LOG.trace("Test: getUoWTypeFDN(): referenceFDN --> {}", referenceFDN);
        String testFDN = ingresObjectOnlyUoW.getUowTypeID().toString();
        LOG.trace("Test: getUoWTypeFDN(): ingresObjectOnlyUoW --> {}", testFDN);
        LOG.trace("Test: ingestObjectOnlyUoW.unqualiefiedName --> {}", ingresObjectOnlyUoW.getUoWInstanceID().getUnqualifiedToken());
        boolean isEqual = referenceFDN.contentEquals(testFDN);
        assertTrue(isEqual);
    }

    /**
     * This code works in conjunction with the net.fhirfactory.pegacorn.petasos.test.sampleuows.SampleCommunicateIRISUoWSet
     * class. It is only a positive test to make sure the framework still does the right thing with respect to
     * add/removing UoW Type FDN.
     *
     */
    @Test
    void setUoWTypeFDN() {
        LOG.info("Test: setUoWTypeFDN()");
        FDN newUoWTypeFDN = new FDN();
        newUoWTypeFDN.appendRDN(new RDN("ProcessingPlant", "Communicate::Iris"));
        newUoWTypeFDN.appendRDN(new RDN("UoW", "a new random string"));
        FDN oldUoWTypeFDN = ingresObjectOnlyUoW.getUowTypeID();
        LOG.trace(".setUoWTypeFDN(): Old UoW FDN Type --> {}", oldUoWTypeFDN);
        ingresObjectOnlyUoW.setUoWTypeID(newUoWTypeFDN);
        String referenceFDN = newUoWTypeFDN.toString();
        LOG.trace("setUoWTypeFDN: Expected New FDN --> {}", referenceFDN);
        String testFDN = ingresObjectOnlyUoW.getUowTypeID().toString();
        LOG.trace("setUoWTypeFDN: Actual FDN --> {}", testFDN);
        boolean isEqual = referenceFDN.contentEquals(testFDN);
        assertTrue(isEqual);
    }

    /**
     * This code works in conjunction with the net.fhirfactory.pegacorn.petasos.test.sampleuows.SampleCommunicateIRISUoWSet
     * class. It is only a positive test to make sure the framework still does the right thing with respect to
     * add/removing UoW Ingres Content.
     *
     */
    @Test
    void getUowIngressContent() {
        LOG.info("Test: getUoWIngressContent()");
        UoWPayloadSet referencePayloadSet = new UoWPayloadSet();
        UoWPayload referencePayload = new UoWPayload();
        referencePayload.setPayload(irisUoWSet.getDefaultRoomMessage());
        FDNToken newMatrixPayloadType = new FDN();
        newMatrixPayloadType.appendRDN(new RDN("Matrix", "m.room.message"));
        newMatrixPayloadType.appendRDN(new RDN("Matrix", "m.text"));
        referencePayload.setPayloadType(newMatrixPayloadType);
        referencePayloadSet.addPayloadElement(referencePayload);
        UoWPayloadSet testPayloadSet = ingresObjectOnlyUoW.getUowIngresContent();
        String referencePayloadSetAsString = referencePayloadSet.toString();
        LOG.trace(".getUowIngressContent(): Expected (Reference) Payload Set --> {}", referencePayloadSetAsString);
        String testPayloadSetAsString = testPayloadSet.toString();
        LOG.trace(".getUowIngressContent(): Actual Payload Set --> {}", testPayloadSetAsString );
        assertTrue(referencePayloadSetAsString.contentEquals(testPayloadSetAsString));
    }

    /**
     * This code works in conjunction with the net.fhirfactory.pegacorn.petasos.test.sampleuows.SampleCommunicateIRISUoWSet
     * class. It is only a positive test to make sure the framework still does the right thing with respect to
     * add/removing UoW Ingres Content.
     *
     */
    @Test
    void setUowIngressContent() {
        LOG.info("Test: setUowIngressContent()");
        UoWPayloadSet referencePayloadSet = new UoWPayloadSet();
        UoWPayload referencePayload = new UoWPayload();
        referencePayload.setPayload(new String("silly payload content"));
        FDNToken newMatrixPayloadType = new FDN();
        newMatrixPayloadType.appendRDN(new RDN("Matrix", "m.room.message"));
        newMatrixPayloadType.appendRDN(new RDN("Matrix", "garbage"));
        referencePayload.setPayloadType(newMatrixPayloadType);
        referencePayloadSet.addPayloadElement(referencePayload);
        UoWPayloadSet originalPayloadSet = ingresPlusSingleEgressObjectUoW.getUowIngresContent();
        LOG.trace(".getUowIngressContent(): Original Payload Set --> {}", originalPayloadSet );
        ingresPlusSingleEgressObjectUoW.setUowIngresContent(referencePayloadSet);
        UoWPayloadSet testPayloadSet = ingresPlusSingleEgressObjectUoW.getUowIngresContent();
        String referencePayloadSetAsString = referencePayloadSet.toString();
        LOG.trace(".getUowIngressContent(): Expected (Reference) Payload Set --> {}", referencePayloadSetAsString);
        String testPayloadSetAsString = testPayloadSet.toString();
        LOG.trace(".getUowIngressContent(): Actual Payload Set --> {}", testPayloadSetAsString );
        assertTrue(referencePayloadSetAsString.contentEquals(testPayloadSetAsString));
    }

    @Test
    void getUowEgressContent() {

    }

    @Test
    void setUowEgressContent() {
        LOG.info("Test: setUowEgressContent()");
        UoWPayloadSet referencePayloadSet = new UoWPayloadSet();
        UoWPayload referencePayload = new UoWPayload();
        referencePayload.setPayload(new String("silly payload content"));
        FDNToken newMatrixPayloadType = new FDN();
        newMatrixPayloadType.appendRDN(new RDN("Matrix", "m.room.message"));
        newMatrixPayloadType.appendRDN(new RDN("Matrix", "garbage"));
        referencePayload.setPayloadType(newMatrixPayloadType);
        referencePayloadSet.addPayloadElement(referencePayload);
        UoWPayloadSet originalPayloadSet = ingresPlusSingleEgressObjectUoW.getUowEgressContent();
        LOG.trace(".setUowEgressContent(): Original Payload Set --> {}", originalPayloadSet );
        ingresPlusSingleEgressObjectUoW.setUowEgressContent(referencePayloadSet);
        UoWPayloadSet testPayloadSet = ingresPlusSingleEgressObjectUoW.getUowEgressContent();
        String referencePayloadSetAsString = referencePayloadSet.toString();
        LOG.trace(".setUowEgressContent(): Expected (Reference) Payload Set --> {}", referencePayloadSetAsString);
        String testPayloadSetAsString = testPayloadSet.toString();
        LOG.trace(".setUowEgressContent(): Actual Payload Set --> {}", testPayloadSetAsString );
        assertTrue(referencePayloadSetAsString.contentEquals(testPayloadSetAsString));
    }

    /**
     * This code works in conjunction with the net.fhirfactory.pegacorn.petasos.test.sampleuows.SampleCommunicateIRISUoWSet
     * class. It is only a positive test to make sure the framework still does the right thing with respect to
     * add/removing UoW Ingres Content.
     *
     */
    @Test
    void getUowProcessingOutcome() {
        LOG.info("Test: getUowProcessingOutcome()");
        assertTrue( this.ingresObjectOnlyUoW.getUowProcessingOutcome() == UoWProcessingOutcomeEnum.UOW_OUTCOME_NOTSTARTED );
    }

    @Test
    void setUowProcessingOutcome() {
        LOG.info("Test: getUowProcessingOutcome()");
        boolean testSuccess = true;
        this.ingresObjectOnlyUoW.setUowProcessingOutcome(UoWProcessingOutcomeEnum.UOW_OUTCOME_NOTSTARTED);
        testSuccess = testSuccess && (this.ingresObjectOnlyUoW.getUowProcessingOutcome() == UoWProcessingOutcomeEnum.UOW_OUTCOME_NOTSTARTED);
        this.ingresObjectOnlyUoW.setUowProcessingOutcome(UoWProcessingOutcomeEnum.UOW_OUTCOME_INCOMPLETE);
        testSuccess = testSuccess && (this.ingresObjectOnlyUoW.getUowProcessingOutcome() == UoWProcessingOutcomeEnum.UOW_OUTCOME_INCOMPLETE);
        this.ingresObjectOnlyUoW.setUowProcessingOutcome(UoWProcessingOutcomeEnum.UOW_OUTCOME_FAILED);
        testSuccess = testSuccess && (this.ingresObjectOnlyUoW.getUowProcessingOutcome() == UoWProcessingOutcomeEnum.UOW_OUTCOME_FAILED);
        this.ingresObjectOnlyUoW.setUowProcessingOutcome(UoWProcessingOutcomeEnum.UOW_OUTCOME_SUCCESS);
        testSuccess = testSuccess && (this.ingresObjectOnlyUoW.getUowProcessingOutcome() == UoWProcessingOutcomeEnum.UOW_OUTCOME_SUCCESS);
        assertTrue(testSuccess);
    }

    /**
     * This code works in conjunction with the net.fhirfactory.pegacorn.petasos.test.sampleuows.SampleCommunicateIRISUoWSet
     * class. The requiredFunctionFDN identified in this test should match that created in the SampleCommunicateIRISUoWSet
     * methods.
     *
     */
    @Test
    void getRequiredFunctionFDN() {
        LOG.info("Test: getRequiredFunctionFDN()");
        FDN referenceFunctionFDN = new FDN();
        referenceFunctionFDN.appendRDN(new RDN("ProcessingPlant", "Communicate::Iris"));
        referenceFunctionFDN.appendRDN(new RDN("WUP", "Transformer-MatrixInstantMessages2FHIR"));
        String referenceFDN = referenceFunctionFDN.toString();
        LOG.trace("Test: getRequiredFunctionFDN(): referenceFDN --> {}", referenceFDN);
        String testFDN = ingresObjectOnlyUoW.getRequiredFunctionTypeID().toString();
        LOG.trace("Test: getRequiredFunctionFDN(): ingresObjectOnlyUoW --> {}", testFDN);
        boolean isEqual = referenceFDN.contentEquals(testFDN);
        assertTrue(isEqual);
    }

    @Test
    void setRequiredFunctionFDN() {
        LOG.info("Test: setRequiredFunctionFDN()");
        FDN newRequiredFunctionFDN = new FDN();
        newRequiredFunctionFDN.appendRDN(new RDN("ProcessingPlant", "Communicate::Iris"));
        newRequiredFunctionFDN.appendRDN(new RDN("WUP", "a new random string"));
        FDN oldRequiredFunctionFDN = ingresObjectOnlyUoW.getRequiredFunctionTypeID();
        LOG.trace(".setRequiredFunctionFDN(): Old UoW FDN Type --> {}", oldRequiredFunctionFDN);
        ingresObjectOnlyUoW.setRequiredFunctionTypeID(newRequiredFunctionFDN);
        String referenceFDN = newRequiredFunctionFDN.toString();
        LOG.trace("setRequiredFunctionFDN: Expected New FDN --> {}", referenceFDN);
        String testFDN = ingresObjectOnlyUoW.getRequiredFunctionTypeID().toString();
        LOG.trace("setRequiredFunctionFDN: Actual FDN --> {}", testFDN);
        boolean isEqual = referenceFDN.contentEquals(testFDN);
        assertTrue(isEqual);    }

    @Test
    void testToString() {
    }
}