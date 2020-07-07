/*
 * Copyright (c) 2020 mhunter
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

import net.fhirfactory.pegacorn.common.model.FDN;
import net.fhirfactory.pegacorn.common.model.FDNToken;
import net.fhirfactory.pegacorn.petasos.core.agent.WUPContainerResilienceAgent;
import net.fhirfactory.pegacorn.petasos.model.resilience.parcel.ResilienceParcel;
import net.fhirfactory.pegacorn.petasos.model.resilience.activitymatrix.ResilienceParcelProcessingStatusEnum;
import net.fhirfactory.pegacorn.petasos.model.uow.UoW;
import net.fhirfactory.pegacorn.petasos.model.uow.UoWProcessingOutcomeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WUPContainerEgressProcessor extends WUPContainerProcessorBase{
    private static final Logger LOG = LoggerFactory.getLogger(WUPContainerEgressProcessor.class);

    public UoW egressContentProcessor(UoW incomingUoW, String wupIDAsTokenString) {
        LOG.debug(".egressContentProcessor(): Enter, incomingUoW --> {}, wupIDAsTokenString -->", incomingUoW, wupIDAsTokenString);
        UoW uowOutput = new UoW;
        WUPContainerResilienceAgent localAgent = new WUPContainerResilienceAgent();
        FDNToken wupIDToken = new FDNToken(wupIDAsTokenString);
        FDN wupID = new FDN(wupIDToken);
        ResilienceParcel currentParcel = localAgent.getParcelFullStatus(wupID);
        ResilienceParcelProcessingStatusEnum incomingParcelStatus = currentParcel.getPetasosParcelProcessingStatus();
        UoWProcessingOutcomeEnum uowOutcome = incomingUoW.getProcessingOutcome();
        ResilienceParcelJobCard newJobCard;
        // It is better to have some replicated code-bits throughout the state-machine below - since it makes it easier
        // to read. But, beware, it can mean that, sometimes, we allow ourselves to be "inconsistent".
        // Be cognisant that this Bean/Method has been invoked when the encompassed WUP has finished processing the
        // UoW it was given ---> IT HAS FINISHED IT...
        switch( currentParcel.getPetasosParcelProcessingStatus() ){
            case PARCEL_STATUS_REGISTERED: {
                LOG.trace(".egressContentProcessor(): currentParcel.getPetasosParcelProcessingStatus() == PARCEL_STATUS_REGISTERED");
                switch (uowOutcome) {
                    case UOW_OUTCOME_NOTSTARTED:
                        LOG.trace(".egressContentProcessor(): incomingUoW.getUowProcessingOutcome() == UOW_OUTCOME_NOTSTARTED");
                    case UOW_OUTCOME_INCOMPLETE:
                        LOG.trace(".egressContentProcessor(): incomingUoW.getUowProcessingOutcome() == UOW_OUTCOME_INCOMPLETE");
                    case UOW_OUTCOME_SUCCESS:
                        LOG.trace(".egressContentProcessor(): incomingUoW.getUowProcessingOutcome() == UOW_OUTCOME_SUCCESS");
                    case UOW_OUTCOME_FAILED:
                        LOG.trace(".egressContentProcessor(): incomingUoW.getUowProcessingOutcome() == UOW_OUTCOME_FAILED");
                    default:
                        LOG.trace(".egressContentProcessor(): incomingUoW.getUowProcessingOutcome() == --default--");
                }
                break;
            }
            case PARCEL_STATUS_INITIATED: {
                LOG.trace(".egressContentProcessor(): currentParcel.getPetasosParcelProcessingStatus() == PARCEL_STATUS_INITIATED");
                switch (uowOutcome) {
                    case UOW_OUTCOME_NOTSTARTED:
                        LOG.trace(".egressContentProcessor(): incomingUoW.getUowProcessingOutcome() == UOW_OUTCOME_NOTSTARTED");
                    case UOW_OUTCOME_INCOMPLETE:
                        LOG.trace(".egressContentProcessor(): incomingUoW.getUowProcessingOutcome() == UOW_OUTCOME_INCOMPLETE");
                    case UOW_OUTCOME_SUCCESS:
                        LOG.trace(".egressContentProcessor(): incomingUoW.getUowProcessingOutcome() == UOW_OUTCOME_SUCCESS");
                    case UOW_OUTCOME_FAILED:
                        LOG.trace(".egressContentProcessor(): incomingUoW.getUowProcessingOutcome() == UOW_OUTCOME_FAILED");
                    default:
                        LOG.trace(".egressContentProcessor(): incomingUoW.getUowProcessingOutcome() == --default--");
                }
                break;
            }
            case PARCEL_STATUS_ACTIVE: {
                LOG.trace(".egressContentProcessor(): currentParcel.getPetasosParcelProcessingStatus() == PARCEL_STATUS_ACTIVE");
                switch (uowOutcome) {
                    case UOW_OUTCOME_NOTSTARTED:
                        LOG.trace(".egressContentProcessor(): incomingUoW.getUowProcessingOutcome() == UOW_OUTCOME_NOTSTARTED");
                    case UOW_OUTCOME_INCOMPLETE:
                        LOG.trace(".egressContentProcessor(): incomingUoW.getUowProcessingOutcome() == UOW_OUTCOME_INCOMPLETE");
                    case UOW_OUTCOME_SUCCESS:
                        LOG.trace(".egressContentProcessor(): incomingUoW.getUowProcessingOutcome() == UOW_OUTCOME_SUCCESS");
                    case UOW_OUTCOME_FAILED:
                        LOG.trace(".egressContentProcessor(): incomingUoW.getUowProcessingOutcome() == UOW_OUTCOME_FAILED");
                    default:
                        LOG.trace(".egressContentProcessor(): incomingUoW.getUowProcessingOutcome() == --default--");
                }
                break;
            }
            case PARCEL_STATUS_FINISHED: {
                LOG.trace(".egressContentProcessor(): currentParcel.getPetasosParcelProcessingStatus() == PARCEL_STATUS_FINISHED");
                switch (uowOutcome) {
                    case UOW_OUTCOME_NOTSTARTED:
                        LOG.trace(".egressContentProcessor(): incomingUoW.getUowProcessingOutcome() == UOW_OUTCOME_NOTSTARTED");
                    case UOW_OUTCOME_INCOMPLETE:
                        LOG.trace(".egressContentProcessor(): incomingUoW.getUowProcessingOutcome() == UOW_OUTCOME_INCOMPLETE");
                    case UOW_OUTCOME_SUCCESS:
                        LOG.trace(".egressContentProcessor(): incomingUoW.getUowProcessingOutcome() == UOW_OUTCOME_SUCCESS");
                    case UOW_OUTCOME_FAILED:
                        LOG.trace(".egressContentProcessor(): incomingUoW.getUowProcessingOutcome() == UOW_OUTCOME_FAILED");
                    default:
                        LOG.trace(".egressContentProcessor(): incomingUoW.getUowProcessingOutcome() == --default--");
                }
                break;
            }
            case PARCEL_STATUS_FINISHED_ELSEWHERE: {
                LOG.trace(".egressContentProcessor(): currentParcel.getPetasosParcelProcessingStatus() == PARCEL_STATUS_FINISHED_ELSEWHERE");
                switch (uowOutcome) {
                    case UOW_OUTCOME_NOTSTARTED:
                        LOG.trace(".egressContentProcessor(): incomingUoW.getUowProcessingOutcome() == UOW_OUTCOME_NOTSTARTED");
                    case UOW_OUTCOME_INCOMPLETE:
                        LOG.trace(".egressContentProcessor(): incomingUoW.getUowProcessingOutcome() == UOW_OUTCOME_INCOMPLETE");
                    case UOW_OUTCOME_SUCCESS:
                        LOG.trace(".egressContentProcessor(): incomingUoW.getUowProcessingOutcome() == UOW_OUTCOME_SUCCESS");
                    case UOW_OUTCOME_FAILED:
                        LOG.trace(".egressContentProcessor(): incomingUoW.getUowProcessingOutcome() == UOW_OUTCOME_FAILED");
                    default:
                        LOG.trace(".egressContentProcessor(): incomingUoW.getUowProcessingOutcome() == --default--");
                }
                break;
            }
            case PARCEL_STATUS_FINALISED: {
                LOG.trace(".egressContentProcessor(): currentParcel.getPetasosParcelProcessingStatus() == PARCEL_STATUS_FINALISED");
                switch (uowOutcome) {
                    case UOW_OUTCOME_NOTSTARTED:
                        LOG.trace(".egressContentProcessor(): incomingUoW.getUowProcessingOutcome() == UOW_OUTCOME_NOTSTARTED");
                    case UOW_OUTCOME_INCOMPLETE:
                        LOG.trace(".egressContentProcessor(): incomingUoW.getUowProcessingOutcome() == UOW_OUTCOME_INCOMPLETE");
                    case UOW_OUTCOME_SUCCESS:
                        LOG.trace(".egressContentProcessor(): incomingUoW.getUowProcessingOutcome() == UOW_OUTCOME_SUCCESS");
                    case UOW_OUTCOME_FAILED:
                        LOG.trace(".egressContentProcessor(): incomingUoW.getUowProcessingOutcome() == UOW_OUTCOME_FAILED");
                    default:
                        LOG.trace(".egressContentProcessor(): incomingUoW.getUowProcessingOutcome() == --default--");
                }
                break;
            }
            case PARCEL_STATUS_FINALISED_ELSEWHERE: {
                LOG.trace(".egressContentProcessor(): currentParcel.getPetasosParcelProcessingStatus() == PARCEL_STATUS_FINALISED_ELSEWHERE");
                switch (uowOutcome) {
                    case UOW_OUTCOME_NOTSTARTED:
                        LOG.trace(".egressContentProcessor(): incomingUoW.getUowProcessingOutcome() == UOW_OUTCOME_NOTSTARTED");
                    case UOW_OUTCOME_INCOMPLETE:
                        LOG.trace(".egressContentProcessor(): incomingUoW.getUowProcessingOutcome() == UOW_OUTCOME_INCOMPLETE");
                    case UOW_OUTCOME_SUCCESS:
                        LOG.trace(".egressContentProcessor(): incomingUoW.getUowProcessingOutcome() == UOW_OUTCOME_SUCCESS");
                    case UOW_OUTCOME_FAILED:
                        LOG.trace(".egressContentProcessor(): incomingUoW.getUowProcessingOutcome() == UOW_OUTCOME_FAILED");
                    default:
                        LOG.trace(".egressContentProcessor(): incomingUoW.getUowProcessingOutcome() == --default--");
                }
                break;
            }
            case PARCEL_STATUS_FAILED:
                LOG.trace(".egressContentProcessor(): currentParcel.getPetasosParcelProcessingStatus() == PARCEL_STATUS_FAILED");
            default: {
                LOG.trace(".egressContentProcessor(): currentParcel.getPetasosParcelProcessingStatus() == --default--");
                switch (uowOutcome) {
                    case UOW_OUTCOME_NOTSTARTED:
                        LOG.trace(".egressContentProcessor(): incomingUoW.getUowProcessingOutcome() == UOW_OUTCOME_NOTSTARTED");
                    case UOW_OUTCOME_INCOMPLETE:
                        LOG.trace(".egressContentProcessor(): incomingUoW.getUowProcessingOutcome() == UOW_OUTCOME_INCOMPLETE");
                    case UOW_OUTCOME_SUCCESS:
                        LOG.trace(".egressContentProcessor(): incomingUoW.getUowProcessingOutcome() == UOW_OUTCOME_SUCCESS");
                    case UOW_OUTCOME_FAILED:
                        LOG.trace(".egressContentProcessor(): incomingUoW.getUowProcessingOutcome() == UOW_OUTCOME_FAILED");
                    default:
                        LOG.trace(".egressContentProcessor(): incomingUoW.getUowProcessingOutcome() == --default--");
                }
            }
        }
        return (uowOutput);
    }
}
