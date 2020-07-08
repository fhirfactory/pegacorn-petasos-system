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

package net.fhirfactory.pegacorn.petasos.core.processingpathway.wupcontainer.worker;

import net.fhirfactory.pegacorn.common.model.FDNToken;
import net.fhirfactory.pegacorn.petasos.core.processingpathway.naming.RouteElementNames;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class WUPContainerRouteTemplate extends RouteBuilder {
    private static final Logger LOG = LoggerFactory.getLogger(WUPContainerRouteTemplate.class);

    private FDNToken wupTypeID;
    private FDNToken wupInstanceID;
    private RouteElementNames nameSet;

    @Inject
    WUPContainerEgressGatekeeper egressGatekeeper;

    @Inject
    WUPContainerEgressProcessor egressProcessor;

    public WUPContainerRouteTemplate(CamelContext context, FDNToken wupTypeID, FDNToken wupInstanceID) {
        super(context);
        LOG.debug(".WUPContainerRouteTemplate(): Entry, context --> ###, wupTypeID", wupTypeID);
        this.wupTypeID = wupTypeID;
        this.wupInstanceID = wupInstanceID;
        nameSet = new RouteElementNames(wupTypeID);
    }

    @Override
    public void configure() {
        LOG.debug(".configure(): Entry!, for wupTypeID --> {}", wupTypeID);

        from(nameSet.getEndPointWUPContainerIngresProcessorIngres())
                .routeId(nameSet.getRouteWUPContainerIngressProcessor())
                .bean(WUPContainerIngresProcessor.class, "ingresContentProcessor(*, Exchange,"+this.wupTypeID+","+this.wupInstanceID+")")
                .to(nameSet.getEndPointWUPContainerIngresProcessorEgress());

        from(nameSet.getEndPointWUPContainerIngresProcessorEgress())
                .routeId(nameSet.getRouteIngresProcessorEgress2IngresGatekeeperIngres())
                .to(nameSet.getEndPointWUPContainerIngresGatekeeperIngres());

        from(nameSet.getEndPointWUPContainerIngresGatekeeperIngres())
                .routeId(nameSet.getRouteWUPContainerIngresGateway())
                .bean(WUPContainerIngresGatekeeper.class, "ingresGatekeeper(*, Exchange,"+this.wupTypeID+","+this.wupInstanceID+")")
                .to(nameSet.getEndPointWUPContainerIngresGatekeeperEgress());

        from(nameSet.getEndPointWUPContainerIngresGatekeeperEgress())
                .routeId(nameSet.getRouteIngresGatekeeperEgress2WUPIngres())
                .to(nameSet.getEndPointWUPIngres());

        from(nameSet.getEndPointWUPEgress())
                .routeId(nameSet.getRouteWUPEgress2WUPEgressGatekeeperIngres())
                .to(nameSet.getEndPointWUPContainerEgressGatekeeperIngres());

        from(nameSet.getEndPointWUPContainerEgressGatekeeperIngres())
                .routeId(nameSet.getRouteWUPContainerEgressGateway())
                .bean(WUPContainerEgressGatekeeper.class,"egressGatekeeper(*, Exchange,"+this.wupTypeID+","+this.wupInstanceID+")")
                .to(nameSet.getEndPointWUPContainerIngresGatekeeperEgress());

        from(nameSet.getEndPointWUPContainerEgressGatekeeperEgress())
                .routeId(nameSet.getRouteWUPEgressGatekeeperEgress2WUPEgressProcessorIngres())
                .to(nameSet.getEndPointWUPContainerEgressProcessorIngres());

        from(nameSet.getEndPointWUPContainerEgressProcessorIngres())
                .routeId(nameSet.getRouteWUPContainerEgressProcessor())
                .bean(WUPContainerEgressProcessor.class, "egressContentProcessor(*, Exchange,"+this.wupTypeID+","+this.wupInstanceID+")")
                .to(nameSet.getEndPointWUPContainerEgressProcessorEgress());
    }
}
