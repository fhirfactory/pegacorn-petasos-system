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
    
    private FDNToken previousResilienceParcelInstanceID;
    private FDNToken previousResilienceParcelEpisodeID;
    private FDNToken presentResilienceParcelInstanceID;
    private FDNToken presentResilienceParcelOccurrenceKey;
    private FDNToken previousWUPInstanceID;
    private FDNToken previousWUPTypeID;
    private FDNToken presentWUPInstanceID;
    private FDNToken presentWUPTypeID;
    private Date creationDate;

    public ContinuityID(FDNToken previousResilienceParcelInstanceID, FDNToken presentResilienceParcelInstanceID, FDNToken previousWUPInstanceID, FDNToken presentWUPInstanceID, Date creationDate) {
        this.previousResilienceParcelInstanceID = null;
        this.previousResilienceParcelEpisodeID = null;
        this.presentResilienceParcelInstanceID = null;
        this.presentResilienceParcelOccurrenceKey = null;
        this.previousWUPInstanceID = null;
        this.previousWUPTypeID = null;
        this.presentWUPInstanceID = null;
        this.presentWUPTypeID = null;
        this.creationDate = null;
        this.previousResilienceParcelInstanceID = previousResilienceParcelInstanceID;
        this.presentResilienceParcelInstanceID = presentResilienceParcelInstanceID;
        this.previousWUPInstanceID = previousWUPInstanceID;
        this.presentWUPInstanceID = presentWUPInstanceID;
        this.creationDate = creationDate;
    }

    public ContinuityID(FDNToken previousResilienceParcelInstanceID, FDNToken presentResilienceParcelInstanceID, FDNToken previousWUPInstanceID, FDNToken presentWUPInstanceID) {
        this.previousResilienceParcelInstanceID = null;
        this.previousResilienceParcelEpisodeID = null;
        this.presentResilienceParcelInstanceID = null;
        this.presentResilienceParcelOccurrenceKey = null;
        this.previousWUPInstanceID = null;
        this.previousWUPTypeID = null;
        this.presentWUPInstanceID = null;
        this.presentWUPTypeID = null;
        this.creationDate = null;
        this.previousResilienceParcelInstanceID = previousResilienceParcelInstanceID;
        this.presentResilienceParcelInstanceID = presentResilienceParcelInstanceID;
        this.previousWUPInstanceID = previousWUPInstanceID;
        this.presentWUPInstanceID = presentWUPInstanceID;
        this.creationDate = Date.from(Instant.now());
    }

    public ContinuityID() {
        this.previousResilienceParcelInstanceID = null;
        this.previousResilienceParcelEpisodeID = null;
        this.presentResilienceParcelInstanceID = null;
        this.presentResilienceParcelOccurrenceKey = null;
        this.previousWUPInstanceID = null;
        this.previousWUPTypeID = null;
        this.presentWUPInstanceID = null;
        this.presentWUPTypeID = null;
        this.creationDate = Date.from(Instant.now());
    }

    public ContinuityID(ContinuityID originalRecord){
        this.previousResilienceParcelInstanceID = null;
        this.previousResilienceParcelEpisodeID = null;
        this.presentResilienceParcelInstanceID = null;
        this.presentResilienceParcelOccurrenceKey = null;
        this.previousWUPInstanceID = null;
        this.previousWUPTypeID = null;
        this.presentWUPInstanceID = null;
        this.presentWUPTypeID = null;
        this.creationDate = null;
        if(originalRecord.hasCreationDate()){
            this.creationDate = originalRecord.getCreationDate();
        }
        if(originalRecord.hasPresentResilienceParcelInstanceID()){
            this.presentResilienceParcelInstanceID = new FDNToken(originalRecord.getPresentResilienceParcelInstanceID());
        }
        if(originalRecord.hasPresentResilienceParcelEpisodeID()){
            this.presentResilienceParcelOccurrenceKey = new FDNToken(originalRecord.getPresentResilienceParcelOccurrenceKey());
        }
        if(originalRecord.hasPresentWUPInstanceID()){
            this.presentWUPInstanceID = new FDNToken(originalRecord.getPresentWUPInstanceID());
        }
        if(originalRecord.hasPresentWUPTypeID()){
            this.presentWUPTypeID = new FDNToken(originalRecord.getPresentWUPTypeID());
        }
        if(originalRecord.hasPreviousResilienceParcelInstanceID()){
            this.previousResilienceParcelInstanceID = new FDNToken(originalRecord.getPreviousResilienceParcelInstanceID());
        }
        if(originalRecord.hasPreviousResilienceParcelEpisodeID()){
            this.previousResilienceParcelEpisodeID = new FDNToken(originalRecord.getPreviousResilienceParcelEpisodeID());
        }
        if(originalRecord.hasPreviousWUPInstanceID()){
            this.previousWUPInstanceID = new FDNToken(originalRecord.getPresentWUPInstanceID());
        }
        if(originalRecord.hasPreviousWUPTypeID()){
            this.previousWUPTypeID = new FDNToken(originalRecord.getPresentWUPTypeID());
        }
    }
    
    
    public boolean hasPreviousResilienceParcelInstanceID(){
        if(this.previousResilienceParcelInstanceID ==null){
            return(false);
        } else {
            return(true);
        }
    }
    
    public FDNToken getPreviousResilienceParcelInstanceID() {
        return previousResilienceParcelInstanceID;
    }

    public void setPreviousResilienceParcelInstanceID(FDNToken previousResilienceParcelInstanceID) {
        this.previousResilienceParcelInstanceID = previousResilienceParcelInstanceID;
    }

    public boolean hasPresentResilienceParcelInstanceID(){
        if(this.presentResilienceParcelInstanceID ==null){
            return(false);
        } else {
            return(true);
        }
    }
    public FDNToken getPresentResilienceParcelInstanceID() {
        return presentResilienceParcelInstanceID;
    }

    public void setPresentResilienceParcelInstanceID(FDNToken presentResilienceParcelInstanceID) {
        this.presentResilienceParcelInstanceID = presentResilienceParcelInstanceID;
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

    public boolean hasPreviousResilienceParcelEpisodeID(){
        if(this.previousResilienceParcelEpisodeID ==null) {
            return (false);
        } else {
            return(true);
        }
    }

    public FDNToken getPreviousResilienceParcelEpisodeID() {
        return previousResilienceParcelEpisodeID;
    }

    public void setPreviousResilienceParcelEpisodeID(FDNToken previousResilienceParcelEpisodeID) {
        this.previousResilienceParcelEpisodeID = previousResilienceParcelEpisodeID;
    }

    public boolean hasPresentResilienceParcelEpisodeID(){
        if(this.presentResilienceParcelOccurrenceKey ==null){
            return(false);
        } else {
            return(true);
        }
    }

    public FDNToken getPresentResilienceParcelOccurrenceKey() {
        return presentResilienceParcelOccurrenceKey;
    }

    public void setPresentResilienceParcelOccurrenceKey(FDNToken presentResilienceParcelOccurrenceKey) {
        this.presentResilienceParcelOccurrenceKey = presentResilienceParcelOccurrenceKey;
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
        if(hasPreviousResilienceParcelEpisodeID()) {
            previousParcelTypeIDString = "(previousResilienceParcelEpisodeID:" + this.previousResilienceParcelEpisodeID.toString() + ")";
        } else {
            previousParcelTypeIDString = "(previousResilienceParcelEpisodeID:null)";
        }
        String previousResilienceParcelInstanceIDString;
        if(hasPreviousResilienceParcelInstanceID()) {
            previousResilienceParcelInstanceIDString = "(previousResilienceParcelInstanceIDString:" + this.previousResilienceParcelInstanceID.toString() + ")";
        } else {
            previousResilienceParcelInstanceIDString = "(previousResilienceParcelInstanceIDString:null)";
        }
        String presentResilienceParcelTypeIDString;
        if(hasPresentResilienceParcelEpisodeID()) {
            presentResilienceParcelTypeIDString = "(presentResilienceParcelEpisodeID:" + this.presentResilienceParcelOccurrenceKey.toString() + ")";
        } else {
            presentResilienceParcelTypeIDString = "(presentResilienceParcelEpisodeID:null)";
        }
        String presentResilienceParcelInstanceIDString;
        if(hasPresentResilienceParcelInstanceID()) {
            presentResilienceParcelInstanceIDString = "(presentResilienceParcelInstanceID:" + this.presentResilienceParcelInstanceID.toString() + ")";
        } else {
            presentResilienceParcelInstanceIDString = "(presentResilienceParcelInstanceID:null)";
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
        if(hasPresentResilienceParcelInstanceID()) {
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
