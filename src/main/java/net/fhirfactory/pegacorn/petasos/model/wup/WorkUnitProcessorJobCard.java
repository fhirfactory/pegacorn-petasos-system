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

package net.fhirfactory.pegacorn.petasos.model.wup;

import net.fhirfactory.pegacorn.common.model.FDN;
import net.fhirfactory.pegacorn.common.model.FDNToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class WorkUnitProcessorJobCard {
    private static final Logger LOG = LoggerFactory.getLogger(WorkUnitProcessorJobCard.class);

    private FDNToken currentParcelID;
    private Date updateDate;
    private WorkUnitProcessorActivityStatusEnum currentStatus;
    private WorkUnitProcessorActivityStatusEnum suggestedStatus;
    private WorkUnitProcessorModeEnum mode;
    private FDNToken wupInstanceID;

    private String toStringValue;

    public WorkUnitProcessorJobCard(FDN wupInstanceID, FDN currentParcelID, WorkUnitProcessorActivityStatusEnum currentStatus, WorkUnitProcessorActivityStatusEnum suggestedStatus, WorkUnitProcessorModeEnum mode, Date updateDate) {
        if(wupInstanceID == null){
            throw( new IllegalArgumentException("WUP Instance Identifier (FDN wupInstanceID) is null in Constructor"));
        }
        if( currentParcelID != null ) {
            this.currentParcelID = currentParcelID.getToken();
        } else
        {
            this.currentParcelID = null;
        }
        this.updateDate = updateDate;
        this.currentStatus = currentStatus;
        this.mode = mode;
        this.wupInstanceID = wupInstanceID.getToken();
        this.suggestedStatus = suggestedStatus;
        generateToString();
    }

    public WorkUnitProcessorJobCard(FDNToken wupInstanceID, FDNToken currentParcelID, WorkUnitProcessorActivityStatusEnum currentStatus, WorkUnitProcessorActivityStatusEnum suggestedStatus, WorkUnitProcessorModeEnum mode, Date updateDate) {
        if(wupInstanceID == null){
            throw( new IllegalArgumentException("WUP Instance Identifier (FDN wupInstanceID) is null in Constructor"));
        }
        if( currentParcelID != null ) {
            this.currentParcelID = currentParcelID;
        } else
        {
            this.currentParcelID = null;
        }
        this.updateDate = updateDate;
        this.currentStatus = currentStatus;
        this.mode = mode;
        this.wupInstanceID = wupInstanceID;
        this.suggestedStatus = suggestedStatus;
        generateToString();
    }

    public WorkUnitProcessorJobCard( WorkUnitProcessorJobCard originalCard){
        if(originalCard == null){
            throw( new IllegalArgumentException("originalCard (WorkUnitProcessorJobCard) is null in Copy Constructor"));
        }
        this.currentParcelID = new FDNToken(originalCard.getCurrentParcelID());
        this.updateDate = originalCard.getUpdateDate();
        this.currentStatus = originalCard.getCurrentStatus();
        this.mode = originalCard.getMode();
        this.wupInstanceID = new FDNToken(originalCard.getWupInstanceID());
        this.suggestedStatus = originalCard.getSuggestedStatus();
        generateToString();
    }

    // Helper/accessor methods for the currentParcelID attribute

    public boolean hasCurrentParcelID(){
        if(this.currentParcelID==null){
            return(false);
        }
        return(true);
    }

    public FDNToken getCurrentParcelID() {
        return currentParcelID;
    }

    public void setCurrentParcelID(FDNToken currentParcelID) {
        this.currentParcelID = currentParcelID;
        generateToString();
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
        generateToString();
    }

    public WorkUnitProcessorActivityStatusEnum getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(WorkUnitProcessorActivityStatusEnum currentStatus) {
        this.currentStatus = currentStatus;
        generateToString();
    }

    public WorkUnitProcessorModeEnum getMode() {
        return mode;
    }

    public void setMode(WorkUnitProcessorModeEnum mode) {
        this.mode = mode;
        generateToString();
    }

    public FDNToken getWupInstanceID() {
        return wupInstanceID;
    }

    public void setWupInstanceID(FDNToken wupInstanceID) {
        this.wupInstanceID = wupInstanceID;
        generateToString();
    }

    @Override
    public String toString(){
        return(this.toStringValue);
    }

    public String generateToString() {
        return "WorkUnitProcessorJobCard={" +
                "(wupInstanceID=" + this.wupInstanceID + ")," +
                "(currentParcelID=" + this.currentParcelID.toString() + ")," +
                "(updateDate=" + this.updateDate.toString() + ")," +
                "(currentStatus=" + this.currentStatus.toString() + ")," +
                "(suggestedStatus=" + this.suggestedStatus.toString() + ")," +
                "(mode=" + this.mode.toString() + ")" + "}";
    }

    public WorkUnitProcessorActivityStatusEnum getSuggestedStatus() {
        return suggestedStatus;
    }

    public void setSuggestedStatus(WorkUnitProcessorActivityStatusEnum suggestedStatus) {
        this.suggestedStatus = suggestedStatus;
    }
}
