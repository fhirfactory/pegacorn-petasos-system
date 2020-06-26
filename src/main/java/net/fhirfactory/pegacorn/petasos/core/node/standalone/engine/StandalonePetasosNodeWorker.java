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

package net.fhirfactory.pegacorn.petasos.core.node.standalone.engine;

import javax.annotation.PostConstruct;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fhirfactory.pegacorn.deploymentproperties.PetasosProperties;
import net.fhirfactory.pegacorn.petasos.audit.api.PetasosAuditWriter;
import net.fhirfactory.pegacorn.petasos.core.node.standalone.cache.LocalNodeActiveParcel2IdleParcelMap;
import net.fhirfactory.pegacorn.petasos.core.node.standalone.cache.LocalNodeDownstreamParcelRegistryMap;
import net.fhirfactory.pegacorn.petasos.core.node.standalone.cache.LocalNodeParcelCache;
import net.fhirfactory.pegacorn.petasos.core.node.standalone.cache.LocalNodeParcelTypeID2ActiveWUPInstanceIDMap;
import net.fhirfactory.pegacorn.petasos.core.node.standalone.cache.LocalNodeParcelTypeID2ParcelInstanceIDSetMap;
import net.fhirfactory.pegacorn.petasos.core.node.standalone.cache.LocalNodeParcelTypeID2WUPInstanceIDSetMap;

@Startup
@Singleton
public class StandalonePetasosNodeWorker {
	private static final Logger LOG = LoggerFactory.getLogger(StandalonePetasosNodeWorker.class);
	
	@Inject
	PetasosProperties systemProperties;

	@Inject
    PetasosAuditWriter auditWriter;
	
	@Inject
	LocalNodeParcelCache parcelSet;
	
	@Inject 
	LocalNodeParcelTypeID2ParcelInstanceIDSetMap parcelInstance2ParcelSetMap;
	
	@Inject
	LocalNodeParcelTypeID2WUPInstanceIDSetMap parcelType2SupportingWUPMap;
	
	@Inject 
	LocalNodeDownstreamParcelRegistryMap downstreamParcelRegistrationMap;
	
	@Inject
	LocalNodeActiveParcel2IdleParcelMap parcelTypeID2ActiveParcelInstanceID;
	
	@Inject
	LocalNodeParcelTypeID2ActiveWUPInstanceIDMap parcelTypeID2ActiveWUPInstanceID;

	@Inject
	TimerService timerService;

	@PostConstruct
	public void initialize() {
		timerService.createTimer(0, 500, "Petasos Worker Checkpoint Timer");
	}
	
	@Timeout
	public void petasosNodeWorker(Timer checkpointTimer) {
		
		
	}
	
}
