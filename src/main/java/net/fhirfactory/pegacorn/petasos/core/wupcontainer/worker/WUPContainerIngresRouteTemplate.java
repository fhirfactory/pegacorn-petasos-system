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

public class WUPContainerIngresRouteTemplate extends RouteBuilder {
    private static final Logger LOG = LoggerFactory.getLogger(WUPContainerIngresRouteTemplate.class);

    private String ingresEgressPoint;
    private String ingresIngresPoint;

    @Inject
    WUPContainerIngresProcessor ingresProcessor;

    public WUPContainerIngresRouteTemplate(CamelContext context, String fromPoint, String toPoint) {
        super(context);
        LOG.debug(".WUPContainerIngresRouteTemplate(): Entry, context --> ###, fromPoint --> {}, toPoint --> {}", fromPoint, toPoint);
        this.ingresIngresPoint = fromPoint;
        this.ingresEgressPoint = toPoint;
}

    public String getIngresEgressPoint() {
        return ingresEgressPoint;
    }

    public String getIngresIngresPoint() {
        return ingresIngresPoint;
    }
    @Override
    public void configure(){
        LOG.debug(".configure(): Entry!");
        String routeIdentifier = "Contraption::Ingres::"+ getIngresIngresPoint()+"-->"+ getIngresEgressPoint();

        from(getIngresIngresPoint())
                .bean(ingresProcessor)
                .to(ingresIngresPoint)
                .end();
    }
}
