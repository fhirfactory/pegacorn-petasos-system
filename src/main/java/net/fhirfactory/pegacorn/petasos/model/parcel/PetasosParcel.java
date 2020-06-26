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
package net.fhirfactory.pegacorn.petasos.model.parcel;

import java.time.Instant;
import java.util.*;

import net.fhirfactory.pegacorn.common.model.FDN;
import net.fhirfactory.pegacorn.common.model.FDNTokenSet;
import net.fhirfactory.pegacorn.common.model.RDN;
import net.fhirfactory.pegacorn.petasos.model.uow.UoW;
import net.fhirfactory.pegacorn.petasos.model.wup.WorkUnitProcessorActivityStatusEnum;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mark A. Hunter
 * @author Scott Yeadon
 */
public class PetasosParcel {
    private static final Logger LOG = LoggerFactory.getLogger(PetasosParcel.class);

    private FDN parcelInstanceID = null;
    private FDN parcelTypeID = null;
    private UoW actualUoW = null;
    private FDN associatedWUPInstanceID = null;
    private FDNTokenSet downstreamParcelIDSet;
    private WorkUnitProcessorActivityStatusEnum taskProcessorActivityStatus;
    private FDN upstreamParcelID = null;
    private PetasosParcelJobCard parcelJobCard = null;
    private FDNTokenSet alternateWUPInstanceIDSet;
    private FDNTokenSet alternateParcelIDSet;
    private FDN activeWUPInstanceID = null;
    private final static String INSTANCE_QUALIFIER_TYPE = "InstanceQualifier";
    private Date parcelRegistrationDate;
    private Date parcelStartDate = null;
    private Date parcelFinishedDate = null;
    private Date parcelFinalisationDate = null;

    //
    // Constructors
    //

    public PetasosParcel(FDN wupInstanceID, FDN parcelTypeID, UoW theUoW, FDN theUpstreamParcelInstanceID, WorkUnitProcessorActivityStatusEnum theWUPStatus) {
        // Clean the slate
        parcelInstanceID = null;
        parcelTypeID = null;
        associatedWUPInstanceID = null;
        actualUoW = null;
        downstreamParcelIDSet = null;
        taskProcessorActivityStatus = null;
        upstreamParcelID = null;
        parcelJobCard = null;
        alternateWUPInstanceIDSet = null;
        parcelRegistrationDate = null;
        parcelStartDate = null;
        parcelFinishedDate = null;
        parcelFinalisationDate = null;
        activeWUPInstanceID = null;
        alternateWUPInstanceIDSet = null;
        // Now, add what we have been supplied
        this.activeWUPInstanceID = new FDN(wupInstanceID);
        this.associatedWUPInstanceID = new FDN(wupInstanceID);
        this.parcelTypeID = new FDN(parcelTypeID);
        this.actualUoW = theUoW;
        this.downstreamParcelIDSet = new FDNTokenSet();
        this.alternateWUPInstanceIDSet = new FDNTokenSet();
        this.upstreamParcelID = new FDN(theUpstreamParcelInstanceID);
        this.taskProcessorActivityStatus = theWUPStatus;
        this.parcelRegistrationDate = Date.from(Instant.now());
        String instanceId = UUID.randomUUID().toString();
        RDN parcelRDN = new RDN(INSTANCE_QUALIFIER_TYPE, instanceId);
        FDN newParcelInstanceID = new FDN(parcelTypeID);
        newParcelInstanceID.appendRDN(parcelRDN);
        this.parcelInstanceID = newParcelInstanceID;
        this.alternateParcelIDSet = new FDNTokenSet();
    }

    public PetasosParcel(PetasosParcel originalParcel) {
        // Clean the slate
        parcelInstanceID = null;
        parcelTypeID = null;
        associatedWUPInstanceID = null;
        actualUoW = null;
        downstreamParcelIDSet = null;
        taskProcessorActivityStatus = null;
        upstreamParcelID = null;
        parcelJobCard = null;
        alternateWUPInstanceIDSet = null;
        parcelRegistrationDate = null;
        parcelStartDate = null;
        parcelFinishedDate = null;
        parcelFinalisationDate = null;
        activeWUPInstanceID = null;
        alternateWUPInstanceIDSet = null;
        // Now, add what we have been supplied
        if( originalParcel.hasAssociatedWUPInstanceID() ){
            this.associatedWUPInstanceID = new FDN(originalParcel.getAssociatedWUPInstanceID());
        }
        if( originalParcel.hasActiveWUPInstanceID() ){
            this.activeWUPInstanceID = new FDN(originalParcel.getActiveWUPInstanceID());
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
            this.parcelInstanceID = new FDN(originalParcel.getParcelInstanceID());
        }
        if( originalParcel.hasParcelRegistrationDate() ){
            this.parcelRegistrationDate = originalParcel.getParcelRegistrationDate();
        }
        if( originalParcel.hasParcelStartDate()){
            this.parcelStartDate = originalParcel.getParcelStartDate();
        }
        if( originalParcel.hasParcelTypeID()){
            this.parcelTypeID = new FDN(originalParcel.getParcelTypeID());
        }
        if( originalParcel.hasParcelJobCard()){
            this.parcelJobCard = originalParcel.getParcelJobCard();
        }
        if( originalParcel.hasUpstreamParcelID()){
            this.upstreamParcelID = new FDN(originalParcel.getUpstreamParcelID());
        }
        if( originalParcel.hasDownstreamParcelIDSet()){
            this.downstreamParcelIDSet = new FDNTokenSet(originalParcel.getDownstreamParcelIDSet());
        }
        if( originalParcel.hasTaskProcessorActivityStatus()){
            this.taskProcessorActivityStatus = originalParcel.getTaskProcessorActivityStatus();
        }
        if(originalParcel.hasAlternateWUPInstanceIDSet()){
            this.alternateWUPInstanceIDSet = new FDNTokenSet(originalParcel.getAlternateWUPInstanceIDSet());
        }
        if(originalParcel.hasAlternateParcelIDSet()){
            this.alternateParcelIDSet = new FDNTokenSet(originalParcel.getAlternateParcelIDSet());
        }
    }

    //
    // Bean/Attribute Methods
    //

    // Helper methods for the this.activeWUPID attribute

    public boolean hasActiveWUPInstanceID(){
        if( this.activeWUPInstanceID == null ){
            return(false);
        }
        return(true);
    }

    /**
     * @return the activeWUPInstanceID
     */
    public FDN getActiveWUPInstanceID() {
        return activeWUPInstanceID;
    }

    /**
     * @param activeWUPID the precursorParcel to set
     */
    public void setActiveWUPInstanceID(FDN activeWUPID) {

        this.activeWUPInstanceID = activeWUPID;
    }

    // Helper methods for the this.alternateWUPInstanceIDSet attribute

    public boolean hasAlternateWUPInstanceIDSet(){
        if( this.alternateWUPInstanceIDSet == null ){
            return(false);
        }
        if( this.alternateWUPInstanceIDSet.isEmpty()){
            return(false);
        }
        return(false);
    }

    public FDNTokenSet getAlternateWUPInstanceIDSet(){
        if(!this.hasAlternateWUPInstanceIDSet()){
            return(null);
        }
        return(this.alternateWUPInstanceIDSet);
    }

    public void setAlternateWUPInstanceIDSet(FDNTokenSet newAlternateWUPInstanceIDSet){
        if(newAlternateWUPInstanceIDSet == null){
            return;
        }
        if(newAlternateWUPInstanceIDSet.isEmpty()){
            return;
        }
        FDNTokenSet newFDNSet = new FDNTokenSet(newAlternateWUPInstanceIDSet);
        this.alternateWUPInstanceIDSet = newFDNSet;
    }

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

    // Helper methods for the this.taskProcessorState attribute

    public boolean hasTaskProcessorActivityStatus(){
        if( this.taskProcessorActivityStatus == null ){
            return(false);
        }
        return(true);
    }

    /**
     * @return the taskProcessorState
     */
    public WorkUnitProcessorActivityStatusEnum getTaskProcessorActivityStatus() {
        return taskProcessorActivityStatus;
    }

    /**
     * @param taskProcessorActivityStatus the taskProcessorState to set
     */
    public void setTaskProcessorActivityStatus(WorkUnitProcessorActivityStatusEnum taskProcessorActivityStatus) {
        this.taskProcessorActivityStatus = taskProcessorActivityStatus;
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
    public FDN getUpstreamParcelID() {
        return upstreamParcelID;
    }

    /**
     * @param upstreamParcelID the "Upstream" or "Precursor" Parcel to set
     */
    public void setUpstreamParcelID(FDN upstreamParcelID) {

        this.upstreamParcelID = upstreamParcelID;
    }

    // Helper methods for the this.petasosParcelStatus attribute

    public boolean hasParcelJobCard(){
        if( this.parcelJobCard == null ){
            return(false);
        }
        return(true);
    }

    public void setParcelJobCard(PetasosParcelJobCard parcelJobCard) {
        this.parcelJobCard = parcelJobCard;
    }

    public PetasosParcelJobCard getParcelJobCard() {
        return this.parcelJobCard;
    }
    
    public void updatePetasosParcelProcessingStatus(PetasosParcelProcessingStatusEnum parcelProcessingStatus) {
    	PetasosParcelJobCard newStatus = new PetasosParcelJobCard(parcelJobCard);
    	this.parcelJobCard = newStatus;
    }
    
    public PetasosParcelProcessingStatusEnum getPetasosParcelProcessingStatus() {
    	if(this.hasParcelJobCard()) {
    		return(this.parcelJobCard.getParcelStatus());
    	}
    	return(null);
    }

    // Helper methods for the this.parcelInstanceID attribute

    public boolean hasParcelInstanceID(){
        if( this.parcelInstanceID == null ){
            return(false);
        }
        return(true);
    }

    public FDN getParcelInstanceID() {
        return this.parcelInstanceID;
    }

    public void setParcelInstanceID(FDN parcelInstance) {
        this.parcelInstanceID = parcelInstance;
    }

    // Helper methods for the this.parcelTypeID attribute

    public boolean hasParcelTypeID(){
        if( this.parcelTypeID == null ){
            return(false);
        }
        return(true);
    }

    public FDN getParcelTypeID() {
        return this.parcelTypeID;
    }

    public void setParcelTypeFDN(FDN parcelType) {
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

    // Helper methods for the this.alternateParcelIDSet attribute

    public boolean hasAlternateParcelIDSet(){
        if(this.alternateParcelIDSet==null){
            return(false);
        }
        return(true);
    }

    public FDNTokenSet getAlternateParcelIDSet() {
        return alternateParcelIDSet;
    }

    public void setAlternateParcelIDSet(FDNTokenSet alternateParcelIDSet) {
        this.alternateParcelIDSet = alternateParcelIDSet;
    }

    // Helper methods for the this.associatedWUPInstanceID attribute

    public boolean hasAssociatedWUPInstanceID(){
        if(this.associatedWUPInstanceID == null){
            return(false);
        }
        return(true);
    }

    public FDN getAssociatedWUPInstanceID() {
        return associatedWUPInstanceID;
    }

    public void setAssociatedWUPInstanceID(FDN associatedWUPInstanceID) {
        this.associatedWUPInstanceID = associatedWUPInstanceID;
    }

    // toString()

    @Override
    public String toString(){
        String newString = new String("PetasosParcel(");
        String parcelInstanceIDString;
        if(hasParcelInstanceID()) {
            parcelInstanceIDString = "(parcelInstanceID=" + parcelInstanceID.toString() + ")";
        }else{
            parcelInstanceIDString = "(parcelInstanceID=null)";
        }
        String parcelTypeIDString;
        if(hasParcelTypeID()){
            parcelTypeIDString = "(parcelTypeID=" + parcelTypeID.toString() + ")";
        } else {
            parcelTypeIDString = "(parcelTypeID=null)";
        }
        String associatedWUPInstanceIDString;
        if(hasAssociatedWUPInstanceID()){
            associatedWUPInstanceIDString = "(parcelTypeID=" + associatedWUPInstanceID.toString() + ")";
        } else {
            associatedWUPInstanceIDString = "(parcelTypeID=null)";
        }
        String upstreamParcelIDString;
        if(hasUpstreamParcelID()) {
        	upstreamParcelIDString = "(precursorParcelID=" + upstreamParcelID.toString() + ")";
        } else {
        	upstreamParcelIDString = "(precursorParcelID=null)";
        }
        String taskProcessorActivityStatusString;
        if(hasTaskProcessorActivityStatus()){
            taskProcessorActivityStatusString = "(taskProcessorActivityStatus=" + taskProcessorActivityStatus.toString() + ")";
        } else {
            taskProcessorActivityStatusString = "(taskProcessorActivityStatus=null)";
        }
        String downstreamParcelIDSetString = new String();
        if(hasDownstreamParcelIDSet()){
        	downstreamParcelIDSetString = "(downstreamParcelIDSet="+this.downstreamParcelIDSet.toString()+")";
        } else {
        	downstreamParcelIDSetString = "(successorParcelIDSet=null)";
        }
        String actualUoWString;
        if(hasActualUoW()){
            actualUoWString = "(actualUoW=" + actualUoW.toString() + ")";
        } else {
            actualUoWString = "(actualUoW=null)";
        }
        String petasosParcelStatusString;
        if(this.hasParcelJobCard()){
            petasosParcelStatusString = "(parcelJobCard=" + parcelJobCard.toString() + ")";
        } else {
            petasosParcelStatusString = "(parcelJobCard=null)";
        }
        String alternateWUPInstanceIDSetString = new String();
        if(hasAlternateWUPInstanceIDSet()){
        	alternateWUPInstanceIDSetString = "(alternateWUPInstanceIDSet="+this.alternateWUPInstanceIDSet.toString()+")";
        } else {
            alternateWUPInstanceIDSetString = "(alternateWUPInstanceIDSet=null)";
        }
        String parcelRegistrationDateString;
        if(hasParcelRegistrationDate()){
            parcelRegistrationDateString = "(parcelRegistrationDate=" + parcelRegistrationDate.toString() + ")";
        } else {
            parcelRegistrationDateString = "(parcelRegistrationDate=null)";
        }
        String parcelStartDateString;
        if(hasParcelStartDate()){
            parcelStartDateString = "(parcelStartDateString=" + parcelStartDate.toString() + ")";
        } else {
            parcelStartDateString = "(parcelStartDateString=null)";
        }
        String parcelFinishedDateString;
        if(hasParcelFinishedDate()){
            parcelFinishedDateString = "(parcelFinishedDate=" + parcelFinishedDate.toString() + ")";
        } else {
            parcelFinishedDateString = "(parcelFinishedDate=null)";
        }
        String parcelFinalisationDateString;
        if(hasParcelFinalisationDate()) {
            parcelFinalisationDateString = "(parcelFinalisationDate=" + parcelFinalisationDate.toString() + ")";
        } else {
            parcelFinalisationDateString = "(parcelFinalisationDate=null)";
        }
        String activeWUPInstanceIDString;
        if(hasActiveWUPInstanceID()){
            activeWUPInstanceIDString = "(activeWUPInstanceID=" + activeWUPInstanceID.toString() + ")";
        } else {
            activeWUPInstanceIDString = "(activeWUPInstanceID=null)";
        }
        String alternativeParcelIDSetString;
        if(hasAlternateParcelIDSet()){
            alternativeParcelIDSetString = "(alternateParcelIDSet="+this.alternateParcelIDSet.toString()+")";
        } else {
            alternativeParcelIDSetString = "(alternateParcelIDSet=null)";
        }

        newString = newString
                + parcelInstanceIDString + ","
                + parcelTypeIDString + ","
                + associatedWUPInstanceIDString + ","
                + upstreamParcelIDString + ","
                + taskProcessorActivityStatusString + ","
                + downstreamParcelIDSetString + ","
                + actualUoWString + ","
                + petasosParcelStatusString + ","
                + alternateWUPInstanceIDSetString + ","
                + parcelRegistrationDateString + ","
                + parcelStartDateString + ","
                + parcelFinishedDateString + ","
                + parcelFinalisationDateString + ","
                + activeWUPInstanceIDString + ")";
        return(newString);
    }
}