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

import net.fhirfactory.pegacorn.common.model.FDNToken;
import net.fhirfactory.pegacorn.petasos.model.uow.UoW;

import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorkUnitTransportPacket {
    private static final Logger LOG = LoggerFactory.getLogger(WorkUnitTransportPacket.class);

    private FDNToken fromElement;
    private FDNToken toElement;
    private Date senderSendDate;
    private boolean isARetry;
    private ContinuityID activityID;
    private UoW payload;
    
    public WorkUnitTransportPacket(FDNToken toElement, Date senderSendDate, UoW payload) {
        this.toElement = toElement;
        this.senderSendDate = senderSendDate;
        this.payload = payload;
        this.isARetry = false;
        this.fromElement = null;
        this.activityID = null;
    }

    public WorkUnitTransportPacket(WorkUnitTransportPacket originalPacket) {
        this.fromElement = originalPacket.getFromElement();
        this.toElement = originalPacket.getToElement();
        this.senderSendDate = originalPacket.getSenderSendDate();
        this.isARetry = originalPacket.getIsARetry();
        this.activityID = originalPacket.getActivityID();
        this.payload = originalPacket.getPayload();
    }

    public boolean hasIsARetry(){
        return(true);
    }

    public boolean getIsARetry() {
        return (this.isARetry);
    }

    public void setRetryCount(boolean retry) {
        this.isARetry = retry;
    }
    
    public boolean hasFromElement(){
        if(this.fromElement==null){
            return(false);
        } else {
            return(true);
        }
    }

    public FDNToken getFromElement() {
        return fromElement;
    }

    public void setFromElement(FDNToken fromElement) {
        this.fromElement = fromElement;
    }

    public boolean hasToElement(){
        if(this.toElement==null){
            return(false);
        } else {
            return(true);
        }
    }
    public FDNToken getToElement() {
        return toElement;
    }

    public void setToElement(FDNToken toElement) {
        this.toElement = toElement;
    }

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
    }

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
    }

    public boolean hasActivityID(){
        if(this.activityID ==null){
            return(false);
        } else {
            return(true);
        }
    }
    
    public ContinuityID getActivityID() {
        return activityID;
    }

    public void setActivityID(ContinuityID activityID) {
        this.activityID = activityID;
    }
    
    

}
