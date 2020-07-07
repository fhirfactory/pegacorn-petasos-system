/*
 * Copyright (c) 2020 Mark A. Hunter (ACT Health)
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

package net.fhirfactory.pegacorn.petasos.core.processingpathway.interchange.manager;

import net.fhirfactory.pegacorn.common.model.FDNToken;
import net.fhirfactory.pegacorn.petasos.core.servicemodule.cache.UoWTypeSubscriptionMapDM;
import net.fhirfactory.pegacorn.petasos.model.servicemodule.MapElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 *
 */

@ApplicationScoped
public class InterchangeIM {
    private static final Logger LOG = LoggerFactory.getLogger(InterchangeIM.class);

    @Inject
    UoWTypeSubscriptionMapDM subscriptionMap;

    @Inject
    InterchangeController interchangeController;

    public void connectToInterchangeFramework(MapElement wupElement) {
        LOG.debug(".connectToInterchangeFramework(): Entry, wupElement --> {}", wupElement);
        interchangeController.buildWUPInterchangeRoutes(wupElement.getElementID());
        LOG.debug(".connectToInterchangeFramework(): Exit");
    }

    public void associateWUP2UoWType(FDNToken wupID, FDNToken uowTypeID) {
        LOG.debug(".associateWUP2UoWType(): Entry, wupID --> {}, uowTypeID --> {}", wupID, uowTypeID);
        subscriptionMap.addSubscriberToUoWType(uowTypeID, wupID);
        LOG.debug(".associateWUP2UoWType(): Exit");
    }


}
