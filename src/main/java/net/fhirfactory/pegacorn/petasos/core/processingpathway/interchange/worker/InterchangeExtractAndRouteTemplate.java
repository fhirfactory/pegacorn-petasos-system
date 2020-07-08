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

package net.fhirfactory.pegacorn.petasos.core.processingpathway.interchange.worker;

import net.fhirfactory.pegacorn.common.model.FDNToken;
import net.fhirfactory.pegacorn.petasos.core.processingpathway.naming.RouteElementNames;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InterchangeExtractAndRouteTemplate extends RouteBuilder {
    private static final Logger LOG = LoggerFactory.getLogger(InterchangeExtractAndRouteTemplate.class);

    private String ingresPoint;
    private FDNToken wupTypeID;
    private RouteElementNames nameSet;

    public InterchangeExtractAndRouteTemplate(CamelContext context, FDNToken wupTypeID) {
        super(context);
        LOG.debug(".InterchangeExtractAndRouteTemplate(): Entry, context --> ###, wupTypeID", wupTypeID);
        this.wupTypeID = wupTypeID;
        nameSet = new RouteElementNames(wupTypeID);
     }



    @Override
    public void configure(){
        LOG.debug(".configure(): Entry!, wupTypeID --> {} ", this.wupTypeID);

        from(nameSet.getEndPointWUPContainerEgressProcessorEgress())
                .routeId(nameSet.getRouteWUPEgressProcessorEgress2InterchangePayloadTransformerIngres())
                .to(nameSet.getEndPointInterchangePayloadTransformerIngres());

        from(nameSet.getEndPointInterchangePayloadTransformerIngres())
                .routeId(nameSet.getRouteInterchangePayloadTransformer())
                .split().method(InterchangeUoWPayload2NewUoWProcessor.class, "extractUoWPayloadAndCreateNewUoWSet")
                .to(nameSet.getEndPointInterchangePayloadTransformerEgress());

        from(nameSet.getEndPointInterchangePayloadTransformerEgress())
                .routeId(nameSet.getRouteInterchangePayloadTransformerEgress2InterchangePayloadRouterIngres())
                .to(nameSet.getEndPointInterchangeRouterIngres());

        from(nameSet.getEndPointInterchangeRouterIngres())
                .routeId(nameSet.getRouteInterchangeRouter())
                .dynamicRouter(method(InterchangeTargetWUPTypeRouter.class,"forwardUoW2WUPs"));
    }
}
