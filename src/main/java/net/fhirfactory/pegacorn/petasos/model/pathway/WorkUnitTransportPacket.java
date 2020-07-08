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

package net.fhirfactory.pegacorn.petasos.model.pathway;

import net.fhirfactory.pegacorn.petasos.model.resilience.activitymatrix.ParcelStatusElement;
import net.fhirfactory.pegacorn.petasos.model.uow.UoW;

import java.util.Date;

import net.fhirfactory.pegacorn.petasos.model.wup.WUPJobCard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorkUnitTransportPacket {
    private static final Logger LOG = LoggerFactory.getLogger(WorkUnitTransportPacket.class);

    private String fromElement;
    private String toElement;
    private Date senderSendDate;
    private boolean isARetry;
    private WUPJobCard currentJobCard;
    private ParcelStatusElement currentParcelStatus;
    private UoW payload;
    private String generatedString;
    
    public WorkUnitTransportPacket(String toElement, Date senderSendDate, UoW payload) {
        this.toElement = toElement;
        this.senderSendDate = senderSendDate;
        this.payload = payload;
        this.isARetry = false;
        this.fromElement = null;
        this.currentJobCard = null;
        this.currentParcelStatus = null;
        this.generatedString = null;
        generateString();
    }

    public WorkUnitTransportPacket(WorkUnitTransportPacket originalPacket) {
        this.fromElement = null;
        this.currentJobCard = null;
        this.currentParcelStatus = null;
        this.toElement = null;
        this.senderSendDate = null;
        this.payload = null;
        this.generatedString = null;
        // Assign values if available
        if(originalPacket.hasFromElement()) {
            this.fromElement = originalPacket.getFromElement();
        }
        if(originalPacket.hasToElement()) {
            this.toElement = originalPacket.getToElement();
        }
        if(originalPacket.hasSenderSendDate()) {
            this.senderSendDate = originalPacket.getSenderSendDate();
        }
        this.isARetry = originalPacket.getIsARetry();
        if(originalPacket.hasCurrentJobCard()) {
            this.currentJobCard = originalPacket.getCurrentJobCard();
        }
        if(originalPacket.hasCurrentParcelStatus()) {
            this.currentParcelStatus = originalPacket.getCurrentParcelStatus();
        }
        if(originalPacket.hasPayload()) {
            this.payload = originalPacket.getPayload();
        }
        generateString();
    }

    public boolean hasIsARetry(){
        return(true);
    }

    public boolean getIsARetry() {
        return (this.isARetry);
    }

    public void setRetryCount(boolean retry) {
        this.isARetry = retry;
        generateString();
    }
    
    // CurrentJobCard helper/bean methods

    public boolean hasCurrentJobCard(){
        if(this.currentJobCard==null){
            return(false);
        } else {
            return(true);
        }
    }

    public WUPJobCard getCurrentJobCard() {
        return currentJobCard;
    }

    public void setCurrentJobCard(WUPJobCard currentJobCard) {
        this.currentJobCard = currentJobCard;
        generateString();
    }

    // CurrentParcelStatus helper/bean methods

    public boolean hasCurrentParcelStatus(){
        if(this.currentParcelStatus==null){
            return(false);
        } else {
            return(true);
        }
    }

    public ParcelStatusElement getCurrentParcelStatus() {
        return currentParcelStatus;
    }

    public void setCurrentParcelStatus(ParcelStatusElement currentParcelStatus) {
        this.currentParcelStatus = currentParcelStatus;
        generateString();
    }

    // FromElement helper/bean methods

    public boolean hasFromElement(){
        if(this.fromElement==null){
            return(false);
        } else {
            return(true);
        }
    }

    public String getFromElement() {
        return fromElement;
    }

    public void setFromElement(String fromElement) {
        this.fromElement = fromElement;
        generateString();
    }

    // ToElement helper/bean methods

    public boolean hasToElement(){
        if(this.toElement==null){
            return(false);
        } else {
            return(true);
        }
    }
    public String getToElement() {
        return toElement;
    }

    public void setToElement(String toElement) {
        this.toElement = toElement;
        generateString();
    }

    // SenderSendDate helper/bean methods

    public boolean hasSenderSendDate(){
        if(this.senderSendDate==null){
            return(false);
        } else {
            return(true);
        }
    }
    public Date getSenderSendDate() {
        return senderSendDate;
    }

    public void setSenderSendDate(Date senderSendDate) {
        this.senderSendDate = senderSendDate;
        generateString();
    }

    // Payload helper/bean methods

    public boolean hasPayload(){
        if(this.payload==null){
            return(false);
        } else {
            return(true);
        }
    }
    
    public UoW getPayload() {
        return payload;
    }

    public void setPayload(UoW payload) {
        this.payload = payload;
        generateString();
    }

    // toString method(s)

    private void generateString(){
        String fromElementString;
        String currentJobCardString;
        String currentParcelStatusString;
        String toElementString;
        String senderSendDateString;
        String payloadString;
        String isARetryString;
        if(hasPayload()){
            payloadString = "(payload:" + this.payload.toString() + ")";
        } else {
            payloadString = "(payload:null)";
        }
        if(hasFromElement()){
            fromElementString = "(fromElement:" + this.fromElement.toString() + ")";
        } else {
            fromElementString = "(fromElement:null)";
        }
        if(hasCurrentParcelStatus()){
            currentParcelStatusString = "(currentParcelStatus:" + this.currentParcelStatus.toString() + ")";
        } else {
            currentParcelStatusString = "(currentParcelStatus:null)";
        }
        if(hasToElement()){
            toElementString = "(toElement:" + this.toElement.toString() + ")";
        } else {
            toElementString = "(toElement:null)";
        }
        if(hasSenderSendDate()){
            senderSendDateString = "(senderSendDate:" + this.senderSendDate.toString() + ")";
        } else {
            senderSendDateString = "(senderSendDate:null)";
        }
        if(hasCurrentParcelStatus()){
            currentJobCardString = "(currentJobCard:" + this.currentJobCard.toString() + ")";
        } else {
            currentJobCardString = "(currentJobCard:null)";
        }
        isARetryString = "(isARetry:" + this.isARetry + ")";
        this.generatedString = "WorkUnitTransportPacket={"
                + fromElementString + ","
                + toElementString + ","
                + senderSendDateString + ","
                + currentJobCardString + ","
                + currentParcelStatusString + ","
                + payloadString + ","
                + isARetryString + "}";
    }

    @Override
    public String toString(){
        return(this.generatedString);
    }

}
