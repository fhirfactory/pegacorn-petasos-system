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

package net.fhirfactory.pegacorn.petasos.core.wupcontainer.worker;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class WUPContainerEgressRouteTemplate extends RouteBuilder {
    private static final Logger LOG = LoggerFactory.getLogger(WUPContainerEgressRouteTemplate.class);

    private String egressEgressPoint;
    private String egressIngresPoint;

    @Inject
    WUPContainerEgressProcessor egressHandoverParcelProcessor;

    public WUPContainerEgressRouteTemplate(CamelContext context, String fromPoint, String toPoint) {
        super(context);
        LOG.debug(".WUPContainerEgressRouteTemplate(): Entry, context --> ###, fromPoint --> {}, toPoint --> {}", fromPoint, toPoint);
        this.egressIngresPoint = fromPoint;
        this.egressEgressPoint = toPoint;
}

    public String getEgressEgressPoint() {
        return egressEgressPoint;
    }

    public String getEgressIngresPoint() {
        return egressIngresPoint;
    }
    @Override
    public void configure(){
        LOG.debug(".configure(): Entry!");
        String routeIdentifier = "Contraption::Egress::"+ getEgressIngresPoint()+"-->"+ getEgressEgressPoint();
        LOG.debug(".configure(): Creating route --> {}", routeIdentifier);
        from(getEgressIngresPoint())
                .routeId(routeIdentifier)
                .bean(egressHandoverParcelProcessor)
                .to(egressIngresPoint)
                .end();
    }
}
