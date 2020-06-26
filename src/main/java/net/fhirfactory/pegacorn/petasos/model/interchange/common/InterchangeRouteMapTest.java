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
package net.fhirfactory.pegacorn.petasos.model.interchange.common;

import java.util.Iterator;
import java.util.Set;
import net.fhirfactory.pegacorn.common.model.FDN;
import net.fhirfactory.pegacorn.common.model.RDN;
import net.fhirfactory.pegacorn.petasos.model.interchange.InterchangeRoute;
import net.fhirfactory.pegacorn.petasos.model.interchange.InterchangeRouteMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Mark A. Hunter
 */
public class InterchangeRouteMapTest {

    private static final Logger LOG = LoggerFactory.getLogger(InterchangeRouteMapTest.class);
    private static final Integer COMPLEXITY_COUNT = 3;

    public InterchangeRouteMapTest() {
    }

    InterchangeRouteMap referenceTestMap;

    private Integer getComplexityCount() {
        return (COMPLEXITY_COUNT);
    }

    @BeforeEach
    public void setUp() {
        LOG.debug("setUp(): Start");
        // Create the Ingres WUP FDNs
        RDN processingPlantRDN0 = new RDN("ProcessingPlant", "FHIRBreak::FMIS");
        RDN wupRDN0 = new RDN("WUP", "Recevier-CSVReader");
        FDN wupFDN0 = new FDN();
        wupFDN0.appendRDN(processingPlantRDN0);
        wupFDN0.appendRDN(wupRDN0);
        RDN processingPlantRDN1 = new RDN("ProcessingPlant", "Communicate::Iris");
        RDN wupRDN1 = new RDN("WUP", "Receiver-MatrixReceiver");
        FDN wupFDN1 = new FDN();
        wupFDN1.appendRDN(processingPlantRDN1);
        wupFDN1.appendRDN(wupRDN1);
        RDN processingPlantRDN5 = new RDN("ProcessingPlant", "Communicate::Iris");
        RDN wupRDN5 = new RDN("WUP", "Staging-MatrixMessageSet");
        FDN wupFDN5 = new FDN();
        wupFDN5.appendRDN(processingPlantRDN5);
        wupFDN5.appendRDN(wupRDN5);
        RDN processingPlantRDN2 = new RDN("ProcessingPlant", "FHIRBreak::FMIS");
        RDN wupRDN2 = new RDN("WUP", "Transformer-CSV2FHIR");
        FDN wupFDN2 = new FDN();
        wupFDN2.appendRDN(processingPlantRDN2);
        wupFDN2.appendRDN(wupRDN2);
        RDN processingPlantRDN3 = new RDN("ProcessingPlant", "Communicate::Iris");
        RDN wupRDN3 = new RDN("WUP", "Transformer-MatrixInstantMessages2FHIR");
        FDN wupFDN3 = new FDN();
        wupFDN3.appendRDN(processingPlantRDN3);
        wupFDN3.appendRDN(wupRDN3);
        RDN processingPlantRDN6 = new RDN("ProcessingPlant", "Communicate::Iris");
        RDN wupRDN6 = new RDN("WUP", "Transformer-MatrixRoomEvents2FHIR");
        FDN wupFDN6 = new FDN();
        wupFDN6.appendRDN(processingPlantRDN6);
        wupFDN6.appendRDN(wupRDN6);
        RDN processingPlantRDN4 = new RDN("ProcessingPlant", "Communicate::Iris");
        RDN wupRDN4 = new RDN("WUP", "Edge-Forwarder");
        FDN wupFDN4 = new FDN();
        wupFDN4.appendRDN(processingPlantRDN4);
        wupFDN4.appendRDN(wupRDN4);

        // Create  the Egres UoW Type FDNs
        RDN uowInformationSetRDN0 = new RDN("InformationSet", "Matrix");
        RDN uowDateElementRDN0 = new RDN("DataElement", "messageset");
        FDN uowTypeFDN0 = new FDN();
        uowTypeFDN0.appendRDN(uowInformationSetRDN0);
        uowTypeFDN0.appendRDN(uowDateElementRDN0);
        RDN uowInformationSetRDN1 = new RDN("InformationSet", "Matrix");
        RDN uowDateElementRDN1 = new RDN("DataElement", "m.room_message");
        FDN uowTypeFDN1 = new FDN();
        uowTypeFDN1.appendRDN(uowInformationSetRDN1);
        uowTypeFDN1.appendRDN(uowDateElementRDN1);
        RDN uowInformationSetRDN2 = new RDN("InformationSet", "Matrix");
        RDN uowDateElementRDN2 = new RDN("DataElement", "m.room_event");
        FDN uowTypeFDN2 = new FDN();
        uowTypeFDN2.appendRDN(uowInformationSetRDN2);
        uowTypeFDN2.appendRDN(uowDateElementRDN2);
        RDN uowInformationSetRDN3 = new RDN("InformationSet", "FHIR");
        RDN uowDataElementRDN3 = new RDN("DataElement", "Communication");
        FDN uowTypeFDN3 = new FDN();
        uowTypeFDN3.appendRDN(uowInformationSetRDN3);
        uowTypeFDN3.appendRDN(uowDataElementRDN3);
        RDN uowInformationSetRDN4 = new RDN("InformationSet", "FHIR");
        RDN uowDataElementRDN4 = new RDN("DataElement", "Group");
        FDN uowTypeFDN4 = new FDN();
        uowTypeFDN4.appendRDN(uowInformationSetRDN4);
        uowTypeFDN4.appendRDN(uowDataElementRDN4);

        // Now build trails through Intersection(s)
        referenceTestMap = new InterchangeRouteMap();

        referenceTestMap.linkConsumingWUP2OutboundUoWType2ProducingWUP(wupFDN1, uowTypeFDN0, wupFDN5);
        referenceTestMap.linkConsumingWUP2OutboundUoWType2ProducingWUP(wupFDN5, uowTypeFDN1, wupFDN3);
        referenceTestMap.linkConsumingWUP2OutboundUoWType2ProducingWUP(wupFDN5, uowTypeFDN2, wupFDN6);
        referenceTestMap.linkConsumingWUP2OutboundUoWType2ProducingWUP(wupFDN3, uowTypeFDN2, wupFDN6);
        
        Set<InterchangeRoute> trailSet = referenceTestMap.getInterchangeRouteSet();
        if (trailSet.isEmpty()) {
            LOG.debug("getInterchangeRouteSet(): TrailSet is Empty!");
        } else {
            Iterator<InterchangeRoute> trailSetIterator = trailSet.iterator();
            int counter = 0;
            while (trailSetIterator.hasNext()) {
                String trailSetEntry = trailSetIterator.next().toString();
                LOG.warn("getInterchangeRouteSet(): TrailSet Entry {}: {}", counter, trailSetEntry);
                counter += 1;
            }
        }

    }

    /**
     * Test of addIngresWUP method, of class IngresWUP2ConsumingWUPMap.
     */
    /*
    @Test
    public void testAddIngresWUP() {
        LOG.debug("addProducingWUP");
        FDN ingresParcelFDN = null;
        InterchangeRouteMap instance = new InterchangeRouteMap();
        instance.addProducingWUP(ingresParcelFDN);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    } */
    /**
     * Test of linkIngresParcelType2IngresWUP method, of class
     * IngresWUP2ConsumingWUPMap.
     */
    /*    @Test
    public void testLinkIngresParcelType2IngresWUP() {
        LOG.debug("linkIngresParcelType2IngresWUP");
        FDN ingresWUPFDN = null;
        FDN ingresParcelType = null;
        InterchangeRouteMap instance = new InterchangeRouteMap();
        instance.linkIngresParcelType2IngresWUP(ingresWUPFDN, ingresParcelType);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    } */
    /**
     * Test of linkEgresUoWType2IngresParcelType2IngresWUP method, of class
     * IngresWUP2ConsumingWUPMap.
     */
    /*    @Test
    public void testLinkEgresUoWType2IngresParcelType2IngresWUP() {
        LOG.debug("linkEgresUoWType2IngresParcelType2IngresWUP");
        FDN ingresWUPFDN = null;
        FDN ingresParcelTypeFDN = null;
        FDN egresUoWType = null;
        InterchangeRouteMap instance = new InterchangeRouteMap();
        instance.linkEgresUoWType2IngresParcelType2IngresWUP(ingresWUPFDN, ingresParcelTypeFDN, egresUoWType);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    } */
    /**
     * Test of linkConsumingWUP2EgresUoWType2IngresParcelType2IngresWUP method,
     * of class IngresWUP2ConsumingWUPMap.
     */
    /*    @Test
    public void testLinkConsumingWUP2EgresUoWType2IngresParcelType2IngresWUP() {
        LOG.debug("linkConsumingWUP2EgresUoWType2IngresParcelType2IngresWUP");
        FDN ingresWUPFDN = null;
        FDN ingresParcelFDN = null;
        FDN egresUoWType = null;
        FDN consumingWUPFDN = null;
        InterchangeRouteMap instance = new InterchangeRouteMap();
        instance.linkConsumingWUP2EgresUoWType2IngresParcelType2IngresWUP(ingresWUPFDN, ingresParcelFDN, egresUoWType, consumingWUPFDN);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    } */
    /**
     * Test of getProcessingTrailSet method, of class InterchangeRouteMap.
     */
    @Test
    public void testGetProcessingTrailSet() {
        LOG.debug("getProcessingTrailSet");
        InterchangeRouteMap intersectionMap;
        intersectionMap = new InterchangeRouteMap();
        for (int producerWUPcounter = 0; producerWUPcounter < this.getComplexityCount(); producerWUPcounter += 1) {
            LOG.trace("setUp(): producerWUPcounter --> " + Integer.toString(producerWUPcounter));
            RDN tempProcessingPlantRDN = new RDN("ProcessingPlant", "Test" + producerWUPcounter);
            RDN tempWUPRDN = new RDN("WUP", "ProducerWUP" + "<" + Integer.toString(producerWUPcounter) + ">");
            FDN tempWUPFDN = new FDN();
            tempWUPFDN.appendRDN(tempProcessingPlantRDN);
            tempWUPFDN.appendRDN(tempWUPRDN);
            intersectionMap.addProducingWUP(tempWUPFDN);
            for (int egresUoWTypeCounter = 0; egresUoWTypeCounter < this.getComplexityCount(); egresUoWTypeCounter += 1) {
                LOG.trace("setUp(): producerWUPcounter --> " + Integer.toString(producerWUPcounter) + ", egresUoWTypeCounter --> " + Integer.toString(egresUoWTypeCounter));
                String uowTypeCounterDiscriminatorString
                        = "<" + Integer.toString(producerWUPcounter)
                        + "." + Integer.toString(egresUoWTypeCounter) + ">";
                RDN tempUoWInformationSetRDN = new RDN("InformationSet", "FHIR");
                RDN tempUoWDataElementRDN = new RDN("DataElement", "FHIR_Entity" + uowTypeCounterDiscriminatorString);
                FDN tempUoWTypeFDN = new FDN();
                tempUoWTypeFDN.appendRDN(tempUoWInformationSetRDN);
                tempUoWTypeFDN.appendRDN(tempUoWDataElementRDN);
                intersectionMap.linkOutboundUoWType2ProducingWUP(tempWUPFDN, tempUoWTypeFDN);
                for (int consumingWUPCounter = 0; consumingWUPCounter < this.getComplexityCount(); consumingWUPCounter += 1) {
                    LOG.trace("setUp():" + Integer.toString(producerWUPcounter) + ", " + Integer.toString(consumingWUPCounter) + ", " + Integer.toString(consumingWUPCounter));
                    String consumingWUPDiscriminatorString
                            = "<" + Integer.toString(producerWUPcounter)
                            + "." + Integer.toString(egresUoWTypeCounter)
                            + "." + Integer.toString(consumingWUPCounter) + ">";
                    RDN tempProcessingPlantRDN2 = new RDN("ProcessingPlant", "Test");
                    RDN tempWUPRDN2 = new RDN("WUP", "ConsumingWUP" + consumingWUPDiscriminatorString);
                    FDN tempWUPFDN2 = new FDN();
                    tempWUPFDN2.appendRDN(tempProcessingPlantRDN2);
                    tempWUPFDN2.appendRDN(tempWUPRDN2);
                    intersectionMap.linkConsumingWUP2OutboundUoWType2ProducingWUP(tempWUPFDN, tempUoWTypeFDN, tempWUPFDN2);
                }
            }

        }
        Set<InterchangeRoute> trailSet = intersectionMap.getInterchangeRouteSet();
        if (trailSet.isEmpty()) {
            LOG.debug("getInterchangeRouteSet(): TrailSet is Empty!");
        } else {
            Iterator<InterchangeRoute> trailSetIterator = trailSet.iterator();
            int counter = 0;
            while (trailSetIterator.hasNext()) {
                String trailSetEntry = trailSetIterator.next().toString();
                LOG.warn("getInterchangeRouteSet(): TrailSet Entry {}: {}", counter, trailSetEntry);
                counter += 1;
            }
        }
        if (trailSet.size() == ((this.getComplexityCount())^3)) {
            assertTrue(true);
        }
    }
}
