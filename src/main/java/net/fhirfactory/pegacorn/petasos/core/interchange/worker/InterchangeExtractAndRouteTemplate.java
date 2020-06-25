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

package net.fhirfactory.pegacorn.petasos.core.interchange.worker;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InterchangeExtractAndRouteTemplate extends RouteBuilder {
    private static final Logger LOG = LoggerFactory.getLogger(InterchangeExtractAndRouteTemplate.class);

    private String ingresPoint;

    public InterchangeExtractAndRouteTemplate(CamelContext context, String fromPoint) {
        super(context);
        LOG.debug(".WUPContainerEgressRouteTemplate(): Entry, context --> ###, fromPoint (WUP Egress Point) --> {}", fromPoint);
        this.ingresPoint = fromPoint;
     }

    public String getIngresPoint() {
        return this.ingresPoint;
    }

    @Override
    public void configure(){
        LOG.debug(".configure(): Entry!");
        String routeIdentifier = "From-"+ getIngresPoint()+"-To-"; // TODO add number of the Processor instance
        LOG.debug(".configure(): Creating route --> {}", routeIdentifier);
        from(getIngresPoint())
                .routeId(routeIdentifier)
                .split().method(InterchangeUoWPayload2NewUoWProcessor.class, "extractUoWPayloadAndCreateNewUoWSet")
                .dynamicRouter(method(InterchangeTargetWUPTypeRouter.class,"forwardUoW2WUPs"));
    }
}
