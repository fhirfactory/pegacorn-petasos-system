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
package net.fhirfactory.pegacorn.petasos.model.resilience.parcel;

import java.time.Instant;
import java.util.*;

import net.fhirfactory.pegacorn.common.model.FDN;
import net.fhirfactory.pegacorn.common.model.FDNToken;
import net.fhirfactory.pegacorn.common.model.FDNTokenSet;
import net.fhirfactory.pegacorn.common.model.RDN;
import net.fhirfactory.pegacorn.petasos.model.resilience.activitymatrix.ResilienceParcelProcessingStatusEnum;
import net.fhirfactory.pegacorn.petasos.model.uow.UoW;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mark A. Hunter
 * @author Scott Yeadon
 */
public class ResilienceParcel {
    private static final Logger LOG = LoggerFactory.getLogger(ResilienceParcel.class);

    private FDNToken parcelInstanceID = null;
    private FDNToken parcelTypeID = null;
    private FDNToken parcelEpisodeID = null;
    private UoW actualUoW = null;
    private FDNToken associatedWUPInstanceID = null;
    private FDNTokenSet downstreamParcelIDSet;
    private FDNToken upstreamParcelID = null;
    private final static String INSTANCE_QUALIFIER_TYPE = "InstanceQualifier";
    private ResilienceParcelFinalisationStatusEnum parcelFinalisationStatus;
    private ResilienceParcelProcessingStatusEnum processingStatus;
    private Date parcelRegistrationDate;
    private Date parcelStartDate = null;
    private Date parcelFinishedDate = null;
    private Date parcelFinalisationDate = null;

    //
    // Constructors
    //

    public ResilienceParcel(FDNToken wupInstanceID, FDNToken parcelTypeID, UoW theUoW, FDNToken theUpstreamParcelInstanceID) {
        // Clean the slate
        this.parcelInstanceID = null;
        this.parcelTypeID = null;
        this.associatedWUPInstanceID = null;
        this.actualUoW = null;
        this.downstreamParcelIDSet = null;
        this.upstreamParcelID = null;
        this.parcelRegistrationDate = null;
        this.parcelStartDate = null;
        this.parcelFinishedDate = null;
        this.parcelFinalisationDate = null;
        this.parcelEpisodeID = null;
        // Now, add what we have been supplied
        this.associatedWUPInstanceID = wupInstanceID;
        this.parcelTypeID = parcelTypeID;
        this.actualUoW = theUoW;
        this.downstreamParcelIDSet = new FDNTokenSet();
        this.upstreamParcelID = theUpstreamParcelInstanceID;
        this.parcelRegistrationDate = Date.from(Instant.now());
        this.parcelEpisodeID = theUoW.getInstanceID();
        String instanceId = UUID.randomUUID().toString();
        RDN parcelRDN = new RDN(INSTANCE_QUALIFIER_TYPE, instanceId);
        FDN newParcelInstanceID = new FDN(wupInstanceID);
        newParcelInstanceID.appendRDN(parcelRDN);
        this.parcelInstanceID = newParcelInstanceID.getToken();
    }

    public ResilienceParcel(ResilienceParcel originalParcel) {
        // Clean the slate
        this.parcelInstanceID = null;
        this.parcelTypeID = null;
        this.associatedWUPInstanceID = null;
        this.actualUoW = null;
        this.downstreamParcelIDSet = null;
        this.upstreamParcelID = null;
        this.parcelRegistrationDate = null;
        this.parcelStartDate = null;
        this.parcelFinishedDate = null;
        this.parcelFinalisationDate = null;
        this.parcelEpisodeID = null;
        // Now, add what we have been supplied
        if( originalParcel.hasAssociatedWUPInstanceID() ){
            this.associatedWUPInstanceID = originalParcel.getAssociatedWUPInstanceID();
        }
        if( originalParcel.hasActualUoW() ){
            this.actualUoW = originalParcel.getActualUoW();
        }
        if( originalParcel.hasParcelFinalisationDate()){
            this.parcelFinalisationDate = originalParcel.getParcelFinalisationDate();
        }
        if( originalParcel.hasParcelFinishedDate()){
            this.parcelFinishedDate = originalParcel.getParcelFinishedDate();
        }
        if( originalParcel.hasParcelInstanceID()){
            this.parcelInstanceID = originalParcel.getParcelInstanceID();
        }
        if( originalParcel.hasParcelRegistrationDate() ){
            this.parcelRegistrationDate = originalParcel.getParcelRegistrationDate();
        }
        if( originalParcel.hasParcelStartDate()){
            this.parcelStartDate = originalParcel.getParcelStartDate();
        }
        if( originalParcel.hasParcelTypeID()){
            this.parcelTypeID = originalParcel.getParcelTypeID();
        }
        if( originalParcel.hasUpstreamParcelID()){
            this.upstreamParcelID = originalParcel.getUpstreamParcelID();
        }
        if( originalParcel.hasDownstreamParcelIDSet()){
            this.downstreamParcelIDSet = new FDNTokenSet(originalParcel.getDownstreamParcelIDSet());
        }
        if( originalParcel.hasParcelEpisodeID()){
            this.parcelEpisodeID = new FDNToken(originalParcel.getParcelEpisodeID());
        }
    }

    //
    // Bean/Attribute Methods
    //

    // Helper methods for the this.actualUoW attribute

    public boolean hasActualUoW(){
        if( this.actualUoW == null ) {
            return (false);
        } else {
            return (true);
        }
    }
    /**
     * @return the containedUoW
     */
    public UoW getActualUoW() {
        return actualUoW;
    }

    /**
     * @param actualUoW the containedUoW to set
     */
    public void setActualUoW(UoW actualUoW) {
        this.actualUoW = new UoW(actualUoW);
    }

    // Helper methods for the this.actualUoW attribute

    public boolean hasDownstreamParcelIDSet() {
        if( this.downstreamParcelIDSet == null ){
            return( false);
        }
        if( this.downstreamParcelIDSet.isEmpty()){
            return( false);
        }
        return(true);
    }

    /**
     * @return the downstreamParcelIDSet
     */
    public FDNTokenSet getDownstreamParcelIDSet() {
    	if( this.downstreamParcelIDSet == null ) {
    		return(new FDNTokenSet());
    	}
    	FDNTokenSet fdnSetCopy = new FDNTokenSet(this.downstreamParcelIDSet);
        return (fdnSetCopy);
    }

    /**
     * @param downstreamParcelIDSet the Parcels that continue on the work from this Parcel
     */
    public void setDownstreamParcelIDSet(FDNTokenSet downstreamParcelIDSet) {
        if(downstreamParcelIDSet==null) {
        	this.downstreamParcelIDSet = new FDNTokenSet();
        }
        this.downstreamParcelIDSet = new FDNTokenSet(downstreamParcelIDSet);
    }

    // Helper methods for the this.precursorParcelID attribute

    public boolean hasUpstreamParcelID(){
        if( this.upstreamParcelID == null ){
            return(false);
        }
        return(true);
    }

    /**
     * @return the upstreamParcelInstanceID
     */
    public FDNToken getUpstreamParcelID() {
        return upstreamParcelID;
    }

    /**
     * @param upstreamParcelID the "Upstream" or "Precursor" Parcel to set
     */
    public void setUpstreamParcelID(FDNToken upstreamParcelID) {

        this.upstreamParcelID = upstreamParcelID;
    }


    // Helper methods for the this.parcelInstanceID attribute

    public boolean hasParcelInstanceID(){
        if( this.parcelInstanceID == null ){
            return(false);
        }
        return(true);
    }

    public FDNToken getParcelInstanceID() {
        return this.parcelInstanceID;
    }

    public void setParcelInstanceID(FDNToken parcelInstance) {
        this.parcelInstanceID = parcelInstance;
    }

    // Helper methods for the this.parcelTypeID attribute

    public boolean hasParcelTypeID(){
        if( this.parcelTypeID == null ){
            return(false);
        }
        return(true);
    }

    public FDNToken getParcelTypeID() {
        return this.parcelTypeID;
    }

    public void setParcelTypeFDN(FDNToken parcelType) {
        this.parcelTypeID = parcelType;
    }

    // Helper methods for the this.parcelRegistrationDate attribute

    public boolean hasParcelRegistrationDate(){
        if( parcelRegistrationDate == null ){
            return(false);
        }
        return(true);
    }

    public Date getParcelRegistrationDate() {
        return parcelRegistrationDate;
    }

    public void setParcelRegistrationDate(Date parcelRegistrationDate) {
        this.parcelRegistrationDate = parcelRegistrationDate;
    }

    // Helper methods for the this.parcelStartDate attribute

    public boolean hasParcelStartDate(){
        if( this.parcelStartDate == null ){
            return(false);
        }
        return(true);
    }
    public Date getParcelStartDate() {
        return parcelStartDate;
    }

    public void setParcelStartDate(Date parcelStartDate) {
        this.parcelStartDate = parcelStartDate;
    }

    // Helper methods for the this.parcelFinishedDate attribute

    public boolean hasParcelFinishedDate(){
        if( this.parcelFinishedDate == null ){
            return(false);
        }
        return(true);
    }

    public Date getParcelFinishedDate() {
        return parcelFinishedDate;
    }

    public void setParcelFinishedDate(Date parcelFinishedDate) {
        this.parcelFinishedDate = parcelFinishedDate;
    }

    // Helper methods for the this.parcelFinalisationDate attribute

    public boolean hasParcelFinalisationDate(){
        if( this.parcelFinalisationDate == null ){
            return(false);
        }
        return(true);
    }

    public Date getParcelFinalisationDate() {
        return parcelFinalisationDate;
    }

    public void setParcelFinalisationDate(Date parcelFinalisationDate) {
        this.parcelFinalisationDate = parcelFinalisationDate;
    }

    // Helper methods for the this.associatedWUPInstanceID attribute

    public boolean hasAssociatedWUPInstanceID(){
        if(this.associatedWUPInstanceID == null){
            return(false);
        }
        return(true);
    }

    public FDNToken getAssociatedWUPInstanceID() {
        return associatedWUPInstanceID;
    }

    public void setAssociatedWUPInstanceID(FDNToken associatedWUPInstanceID) {
        this.associatedWUPInstanceID = associatedWUPInstanceID;
    }

    public void setParcelTypeID(FDNToken parcelTypeID) {
        this.parcelTypeID = parcelTypeID;
    }

    public boolean hasParcelFinalisationStatus(){
        if(this.parcelFinalisationStatus==null){
            return(false);
        } else {
            return(true);
        }
    }

    public ResilienceParcelFinalisationStatusEnum getParcelFinalisationStatus() {
        return parcelFinalisationStatus;
    }

    public void setParcelFinalisationStatus(ResilienceParcelFinalisationStatusEnum parcelFinalisationStatus) {
        this.parcelFinalisationStatus = parcelFinalisationStatus;
    }

    public boolean hasProcessingStatus(){
        if(this.processingStatus == null){
            return(false);
        } else {
            return(true);
        }
    }

    public ResilienceParcelProcessingStatusEnum getProcessingStatus() {
        return processingStatus;
    }

    public void setProcessingStatus(ResilienceParcelProcessingStatusEnum processingStatus) {
        this.processingStatus = processingStatus;
    }

    // toString()

    @Override
    public String toString(){
        String newString = new String("ResilienceParcel={");
        String parcelInstanceIDString;
        if(hasParcelInstanceID()) {
            parcelInstanceIDString = "(parcelInstanceID:" + parcelInstanceID.toString() + ")";
        }else{
            parcelInstanceIDString = "(parcelInstanceID:null)";
        }
        String parcelTypeIDString;
        if(hasParcelTypeID()){
            parcelTypeIDString = "(parcelTypeID:" + parcelTypeID.toString() + ")";
        } else {
            parcelTypeIDString = "(parcelTypeID:null)";
        }
        String associatedWUPInstanceIDString;
        if(hasAssociatedWUPInstanceID()){
            associatedWUPInstanceIDString = "(parcelTypeID=" + associatedWUPInstanceID.toString() + ")";
        } else {
            associatedWUPInstanceIDString = "(parcelTypeID=null)";
        }
        String upstreamParcelIDString;
        if(hasUpstreamParcelID()) {
        	upstreamParcelIDString = "(precursorParcelID:" + upstreamParcelID.toString() + ")";
        } else {
        	upstreamParcelIDString = "(precursorParcelID:null)";
        }
        String downstreamParcelIDSetString = new String();
        if(hasDownstreamParcelIDSet()){
        	downstreamParcelIDSetString = "(downstreamParcelIDSet:"+this.downstreamParcelIDSet.toString()+")";
        } else {
        	downstreamParcelIDSetString = "(successorParcelIDSet:null)";
        }
        String actualUoWString;
        if(hasActualUoW()){
            actualUoWString = "(actualUoW:" + actualUoW.toString() + ")";
        } else {
            actualUoWString = "(actualUoW:null)";
        }
        String parcelRegistrationDateString;
        if(hasParcelRegistrationDate()){
            parcelRegistrationDateString = "(parcelRegistrationDate:" + parcelRegistrationDate.toString() + ")";
        } else {
            parcelRegistrationDateString = "(parcelRegistrationDate:null)";
        }
        String parcelStartDateString;
        if(hasParcelStartDate()){
            parcelStartDateString = "(parcelStartDateString:" + parcelStartDate.toString() + ")";
        } else {
            parcelStartDateString = "(parcelStartDateString:null)";
        }
        String parcelFinishedDateString;
        if(hasParcelFinishedDate()){
            parcelFinishedDateString = "(parcelFinishedDate:" + parcelFinishedDate.toString() + ")";
        } else {
            parcelFinishedDateString = "(parcelFinishedDate:null)";
        }
        String parcelFinalisationDateString;
        if(hasParcelFinalisationDate()) {
            parcelFinalisationDateString = "(parcelFinalisationDate:" + parcelFinalisationDate.toString() + ")";
        } else {
            parcelFinalisationDateString = "(parcelFinalisationDate:null)";
        }
        String parcelFinalisationStatusEnumString;
        if(hasParcelFinalisationStatus()){
            parcelFinalisationStatusEnumString = "(parcelFinalisationStatus:" + parcelFinalisationStatus.toString() + ")";
        } else {
            parcelFinalisationStatusEnumString = "(parcelFinalisationStatus:null)";
        }
        String parcelProcessingStatusEnumString;
        if(hasProcessingStatus()){
            parcelProcessingStatusEnumString = "(processingStatus:" + processingStatus.toString() + ")";
        } else {
            parcelProcessingStatusEnumString = "(processingStatus:null)";
        }
        String parcelOccurrenceString;
        if(hasParcelEpisodeID()){
            parcelOccurrenceString = "(parcelEpisodeID:" + parcelEpisodeID.toString() + ")";
        } else {
            parcelOccurrenceString = "(parcelEpisodeID:null)";
        }
        newString = newString
                + parcelInstanceIDString + ","
                + parcelOccurrenceString + ","
                + parcelTypeIDString + ","
                + associatedWUPInstanceIDString + ","
                + upstreamParcelIDString + ","
                + downstreamParcelIDSetString + ","
                + actualUoWString + ","
                + parcelRegistrationDateString + ","
                + parcelStartDateString + ","
                + parcelFinishedDateString + ","
                + parcelFinalisationDateString + ","
                + parcelProcessingStatusEnumString + ","
                + parcelFinalisationDateString + "}";
        return(newString);
    }

    public boolean hasParcelEpisodeID(){
        if(this.parcelEpisodeID == null){
            return(false);
        } else {
            return(true);
        }
    }

    public FDNToken getParcelEpisodeID() {
        return parcelEpisodeID;
    }

    public void setParcelEpisodeID(FDNToken parcelEpisodeID) {
        this.parcelEpisodeID = parcelEpisodeID;
    }
}