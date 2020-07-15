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

import java.time.Instant;
import java.util.Date;
import net.fhirfactory.pegacorn.common.model.FDNToken;

/**
 *
 * @author Mark A. Hunter
 */
public class ContinuityID {
    
    private FDNToken previousParcelInstanceID;
    private FDNToken previousWUAEpisodeID;
    private FDNToken presentParcelInstanceID;
    private FDNToken presentWUAEpisodeID;
    private FDNToken previousWUPInstanceID;
    private FDNToken previousWUPTypeID;
    private FDNToken presentWUPInstanceID;
    private FDNToken presentWUPTypeID;
    private Date creationDate;

    public ContinuityID(FDNToken previousParcelInstanceID, FDNToken presentParcelInstanceID, FDNToken previousWUPInstanceID, FDNToken presentWUPInstanceID, Date creationDate) {
        this.previousParcelInstanceID = null;
        this.previousWUAEpisodeID = null;
        this.presentParcelInstanceID = null;
        this.presentWUAEpisodeID = null;
        this.previousWUPInstanceID = null;
        this.previousWUPTypeID = null;
        this.presentWUPInstanceID = null;
        this.presentWUPTypeID = null;
        this.creationDate = null;
        this.previousParcelInstanceID = previousParcelInstanceID;
        this.presentParcelInstanceID = presentParcelInstanceID;
        this.previousWUPInstanceID = previousWUPInstanceID;
        this.presentWUPInstanceID = presentWUPInstanceID;
        this.creationDate = creationDate;
    }

    public ContinuityID(FDNToken previousParcelInstanceID, FDNToken presentParcelInstanceID, FDNToken previousWUPInstanceID, FDNToken presentWUPInstanceID) {
        this.previousParcelInstanceID = null;
        this.previousWUAEpisodeID = null;
        this.presentParcelInstanceID = null;
        this.presentWUAEpisodeID = null;
        this.previousWUPInstanceID = null;
        this.previousWUPTypeID = null;
        this.presentWUPInstanceID = null;
        this.presentWUPTypeID = null;
        this.creationDate = null;
        this.previousParcelInstanceID = previousParcelInstanceID;
        this.presentParcelInstanceID = presentParcelInstanceID;
        this.previousWUPInstanceID = previousWUPInstanceID;
        this.presentWUPInstanceID = presentWUPInstanceID;
        this.creationDate = Date.from(Instant.now());
    }

    public ContinuityID() {
        this.previousParcelInstanceID = null;
        this.previousWUAEpisodeID = null;
        this.presentParcelInstanceID = null;
        this.presentWUAEpisodeID = null;
        this.previousWUPInstanceID = null;
        this.previousWUPTypeID = null;
        this.presentWUPInstanceID = null;
        this.presentWUPTypeID = null;
        this.creationDate = Date.from(Instant.now());
    }

    public ContinuityID(ContinuityID originalRecord){
        this.previousParcelInstanceID = null;
        this.previousWUAEpisodeID = null;
        this.presentParcelInstanceID = null;
        this.presentWUAEpisodeID = null;
        this.previousWUPInstanceID = null;
        this.previousWUPTypeID = null;
        this.presentWUPInstanceID = null;
        this.presentWUPTypeID = null;
        this.creationDate = null;
        if(originalRecord.hasCreationDate()){
            this.creationDate = originalRecord.getCreationDate();
        }
        if(originalRecord.hasPresentParcelInstanceID()){
            this.presentParcelInstanceID = new FDNToken(originalRecord.getPresentParcelInstanceID());
        }
        if(originalRecord.hasPresentWUAEpisodeID()){
            this.presentWUAEpisodeID = new FDNToken(originalRecord.getPresentWUAEpisodeID());
        }
        if(originalRecord.hasPresentWUPInstanceID()){
            this.presentWUPInstanceID = new FDNToken(originalRecord.getPresentWUPInstanceID());
        }
        if(originalRecord.hasPresentWUPTypeID()){
            this.presentWUPTypeID = new FDNToken(originalRecord.getPresentWUPTypeID());
        }
        if(originalRecord.hasPreviousParcelInstanceID()){
            this.previousParcelInstanceID = new FDNToken(originalRecord.getPreviousParcelInstanceID());
        }
        if(originalRecord.hasPreviousWUAEpisodeID()){
            this.previousWUAEpisodeID = new FDNToken(originalRecord.getPreviousWUAEpisodeID());
        }
        if(originalRecord.hasPreviousWUPInstanceID()){
            this.previousWUPInstanceID = new FDNToken(originalRecord.getPresentWUPInstanceID());
        }
        if(originalRecord.hasPreviousWUPTypeID()){
            this.previousWUPTypeID = new FDNToken(originalRecord.getPresentWUPTypeID());
        }
    }
    
    
    public boolean hasPreviousParcelInstanceID(){
        if(this.previousParcelInstanceID ==null){
            return(false);
        } else {
            return(true);
        }
    }
    
    public FDNToken getPreviousParcelInstanceID() {
        return previousParcelInstanceID;
    }

    public void setPreviousParcelInstanceID(FDNToken previousParcelInstanceID) {
        this.previousParcelInstanceID = previousParcelInstanceID;
    }

    public boolean hasPresentParcelInstanceID(){
        if(this.presentParcelInstanceID ==null){
            return(false);
        } else {
            return(true);
        }
    }
    public FDNToken getPresentParcelInstanceID() {
        return presentParcelInstanceID;
    }

    public void setPresentParcelInstanceID(FDNToken presentParcelInstanceID) {
        this.presentParcelInstanceID = presentParcelInstanceID;
    }

    public boolean hasPreviousWUPInstanceID(){
        if(this.previousWUPInstanceID==null){
            return(false);
        } else {
            return(true);
        }
    }
    public FDNToken getPreviousWUPInstanceID() {
        return previousWUPInstanceID;
    }

    public void setPreviousWUPInstanceID(FDNToken previousWUPInstanceID) {
        this.previousWUPInstanceID = previousWUPInstanceID;
    }

    public boolean hasPresentWUPInstanceID(){
        if(this.presentWUPInstanceID==null){
            return(false);
        } else {
            return(true);
        }
    }
    public FDNToken getPresentWUPInstanceID() {
        return presentWUPInstanceID;
    }

    public void setPresentWUPInstanceID(FDNToken presentWUPInstanceID) {
        this.presentWUPInstanceID = presentWUPInstanceID;
    }

    public boolean hasCreationDate(){
        if(this.creationDate==null){
            return(false);
        } else {
            return(true);
        }
    }
    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public boolean hasPreviousWUAEpisodeID(){
        if(this.previousWUAEpisodeID ==null) {
            return (false);
        } else {
            return(true);
        }
    }

    public FDNToken getPreviousWUAEpisodeID() {
        return previousWUAEpisodeID;
    }

    public void setPreviousWUAEpisodeID(FDNToken previousWUAEpisodeID) {
        this.previousWUAEpisodeID = previousWUAEpisodeID;
    }

    public boolean hasPresentWUAEpisodeID(){
        if(this.presentWUAEpisodeID ==null){
            return(false);
        } else {
            return(true);
        }
    }

    public FDNToken getPresentWUAEpisodeID() {
        return presentWUAEpisodeID;
    }

    public void setPresentWUAEpisodeID(FDNToken presentWUAEpisodeID) {
        this.presentWUAEpisodeID = presentWUAEpisodeID;
    }

    public boolean hasPreviousWUPTypeID(){
        if(this.previousWUPTypeID==null){
            return(false);
        } else {
            return(true);
        }
    }

    public FDNToken getPreviousWUPTypeID() {
        return previousWUPTypeID;
    }

    public void setPreviousWUPTypeID(FDNToken previousWUPTypeID) {
        this.previousWUPTypeID = previousWUPTypeID;
    }

    public boolean hasPresentWUPTypeID(){
        if(this.previousWUPTypeID==null){
            return(false);
        } else {
            return(true);
        }
    }
    public FDNToken getPresentWUPTypeID() {
        return presentWUPTypeID;
    }

    public void setPresentWUPTypeID(FDNToken presentWUPTypeID) {
        this.presentWUPTypeID = presentWUPTypeID;
    }

    @Override
    public String toString(){
        String previousParcelTypeIDString;
        if(hasPreviousWUAEpisodeID()) {
            previousParcelTypeIDString = "(previousWUAEpisodeID:" + this.previousWUAEpisodeID.toString() + ")";
        } else {
            previousParcelTypeIDString = "(previousWUAEpisodeID:null)";
        }
        String previousResilienceParcelInstanceIDString;
        if(hasPreviousParcelInstanceID()) {
            previousResilienceParcelInstanceIDString = "(previousParcelInstanceID:" + this.previousParcelInstanceID.toString() + ")";
        } else {
            previousResilienceParcelInstanceIDString = "(previousParcelInstanceID:null)";
        }
        String presentResilienceParcelTypeIDString;
        if(hasPresentWUAEpisodeID()) {
            presentResilienceParcelTypeIDString = "(presentWUAEpisodeID:" + this.presentWUAEpisodeID.toString() + ")";
        } else {
            presentResilienceParcelTypeIDString = "(presentWUAEpisodeID:null)";
        }
        String presentResilienceParcelInstanceIDString;
        if(hasPresentParcelInstanceID()) {
            presentResilienceParcelInstanceIDString = "(presentParcelInstanceID:" + this.presentParcelInstanceID.toString() + ")";
        } else {
            presentResilienceParcelInstanceIDString = "(presentParcelInstanceID:null)";
        }
        String previousWUPTypeIDString;
        if(hasPreviousWUPTypeID()) {
            previousWUPTypeIDString = "(previousWUPTypeID:" + this.previousWUPTypeID.toString() + ")";
        } else {
            previousWUPTypeIDString = "(previousWUPTypeID:null)";
        }
        String previousWUPInstanceIDString;
        if(hasPreviousWUPInstanceID()) {
            previousWUPInstanceIDString = "(previousWUPInstanceIDString:" + this.previousWUPInstanceID.toString() + ")";
        } else {
            previousWUPInstanceIDString = "(previousWUPInstanceIDString:null)";
        }
        String presentWUPTypeIDString;
        if(hasPresentWUPTypeID()) {
            presentWUPTypeIDString = "(presentWUPTypeID:" + this.presentWUPTypeID.toString() + ")";
        } else {
            presentWUPTypeIDString = "(presentWUPTypeID:null)";
        }
        String presentWUPInstanceIDString;
        if(hasPresentParcelInstanceID()) {
            presentWUPInstanceIDString = "(presentWUPInstanceID:" + this.presentWUPInstanceID.toString() + ")";
        } else {
            presentWUPInstanceIDString = "(presentWUPInstanceID:null)";
        }
        String creationDateString;
        if(hasCreationDate()) {
            creationDateString = "(creationDate:" + this.creationDate.toString() + ")";
        } else {
            creationDateString = "(creationDate:null)";
        }
        String theString = "ContinuityID={"
                + previousParcelTypeIDString + ","
                + previousResilienceParcelInstanceIDString + ","
                + presentResilienceParcelTypeIDString + ","
                + presentResilienceParcelInstanceIDString + ","
                + previousWUPTypeIDString + ","
                + previousWUPInstanceIDString + ","
                + presentWUPTypeIDString + ","
                + presentWUPInstanceIDString + ","
                + creationDateString + "}";
        return(theString);
    }
}
