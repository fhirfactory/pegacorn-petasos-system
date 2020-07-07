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

package net.fhirfactory.pegacorn.petasos.audit.model;

import net.fhirfactory.pegacorn.common.model.FDNTokenSet;
import net.fhirfactory.pegacorn.common.model.FDNToken;
import net.fhirfactory.pegacorn.petasos.model.resilience.parcel.ResilienceParcel;
import net.fhirfactory.pegacorn.petasos.model.uow.UoWProcessingOutcomeEnum;
import net.fhirfactory.pegacorn.petasos.model.uow.UoW;

import java.time.Instant;
import java.util.Date;

public class PetasosParcelAuditTrailEntry {
    private Date auditTrailEntryDate;
    private UoW actualUoW;
    private FDNToken parcelInstanceID;
    private UoWProcessingOutcomeEnum uowOutcome;
    private FDNTokenSet alternativeWUPInstanceIDSet;
    private FDNTokenSet alternativeParcelIDSet;
    private FDNTokenSet downstreamParcelIDSet;
    private FDNToken upstreamParcelID;
    private FDNToken primaryWUPInstanceID;
    private FDNToken parcelTypeID;
    private ResilienceParcelJobCard processingOutcome;
    private Date parcelRegistrationDate;
    private Date parcelStartDate;
    private Date parcelFinishedDate;
    private Date parcelFinalisedDate;

    //
    // Constructor(s)
    //

    public PetasosParcelAuditTrailEntry(ResilienceParcel theParcel ){
        // First, we clean the slate
        this.auditTrailEntryDate = null;
        this.actualUoW = null;
        this.parcelInstanceID = null;
        this.uowOutcome = null;
        this.alternativeWUPInstanceIDSet = null;
        this.downstreamParcelIDSet = null;
        this.upstreamParcelID = null;
        this.primaryWUPInstanceID = null;
        this.processingOutcome = null;
        this.parcelRegistrationDate = null;
        this.parcelTypeID = null;
        this.parcelStartDate = null;
        this.parcelFinishedDate = null;
        this.parcelFinalisedDate = null;
        this.alternativeParcelIDSet = null;
        // Then, we try and add what we get given
        if( theParcel == null ){
            return;
        }
        this.auditTrailEntryDate = Date.from(Instant.now());
//        if(theParcel.hasAlternateWUPInstanceIDSet()) {
//            this.alternativeWUPInstanceIDSet = new FDNTokenSet(theParcel.getAlternateWUPInstanceIDSet());
//        }
        if( theParcel.hasActualUoW()) {
            this.actualUoW = theParcel.getActualUoW();
            this.uowOutcome = this.actualUoW.getProcessingOutcome();
        }
        if(theParcel.hasParcelJobCard()){
            this.processingOutcome = theParcel.getParcelJobCard();
        }
        if(theParcel.hasDownstreamParcelIDSet()){
            this.downstreamParcelIDSet = new FDNTokenSet(theParcel.getDownstreamParcelIDSet());
        }
        if(theParcel.hasUpstreamParcelID()){
            this.upstreamParcelID = theParcel.getUpstreamParcelID();
        }
        if(theParcel.hasParcelTypeID()){
            this.parcelTypeID = theParcel.getParcelTypeID();
        }
        if(theParcel.hasParcelFinalisationDate()){
            this.parcelFinalisedDate = theParcel.getParcelFinalisationDate();
        }
        if(theParcel.hasParcelInstanceID()){
            this.parcelInstanceID = theParcel.getParcelInstanceID();
        }
        if(theParcel.hasParcelFinishedDate()){
            this.parcelFinishedDate = theParcel.getParcelFinishedDate();
        }
        if(theParcel.hasParcelRegistrationDate()){
            this.parcelRegistrationDate = theParcel.getParcelRegistrationDate();
        }
        if(theParcel.hasParcelStartDate()){
            this.parcelStartDate = theParcel.getParcelStartDate();
        }

    }

    //
    // Bean/Attribute Helper Methods
    //
    
    // Helpers for the this.downstreamParcelIDSet attribute
    
    public boolean hasDownstreamParcelIDSet() {
    	if(this.downstreamParcelIDSet == null ) {
    		return(false);
    	}
    	if(this.downstreamParcelIDSet.isEmpty()) {
    		return(false);
    	}
    	return(true);
    }
    
    public void setDownstreamParcelIDSet(FDNTokenSet newDownstreamParcelIDSet) {
    	if(newDownstreamParcelIDSet == null) {
    		this.downstreamParcelIDSet = null;
    		return;
    	}
    	if(newDownstreamParcelIDSet.isEmpty()) {
    		this.downstreamParcelIDSet = new FDNTokenSet();
    		return;
    	}
    	this.downstreamParcelIDSet = new FDNTokenSet(newDownstreamParcelIDSet);
    }
    
    public FDNTokenSet getDownstreamParcelIDSet() {
    	if(this.downstreamParcelIDSet == null) {
    		return(null);
    	}
    	FDNTokenSet fdnSetCopy = new FDNTokenSet(this.downstreamParcelIDSet);
    	return(fdnSetCopy);
    }

    // Helpers for the this.alternativeWUPInstance attribute

    public boolean hasAlternativeWUPInstanceIDSet(){
        if(this.alternativeWUPInstanceIDSet == null ){
            return(false);
        }
        if(this.alternativeWUPInstanceIDSet.isEmpty()){
            return(false);
        }
        return(true);
    }
    
    public void setAlternativeWUPInstanceIDSet(FDNTokenSet alternativeWUPInstanceIDSet) {
    	if(alternativeWUPInstanceIDSet == null )
    	{
    		this.alternativeWUPInstanceIDSet = new FDNTokenSet();
    	}
        this.alternativeWUPInstanceIDSet = new FDNTokenSet(alternativeWUPInstanceIDSet);
    }

    public FDNTokenSet getAlternativeWUPInstanceIDSet(){
        if(hasAlternativeWUPInstanceIDSet()){
        	FDNTokenSet newFDNSet = new FDNTokenSet(this.alternativeWUPInstanceIDSet);
            return(this.alternativeWUPInstanceIDSet);
        }
        return(null);
    }

    // Helpers for the this.auditTrailEntryDate attribute

    public boolean hasAuditTrailEntryDate(){
        if(this.auditTrailEntryDate == null){
            return(false);
        }
        return(true);
    }

    public Date getAuditTrailEntryDate() {
        return auditTrailEntryDate;
    }

    public void setAuditTrailEntryDate(Date auditTrailEntryDate) {
        this.auditTrailEntryDate = auditTrailEntryDate;
    }

    // Helpers for the this.work attribute

    public boolean hasActualUoW(){
        if(this.actualUoW == null ){
            return(false);
        }
        return(true);
    }

    public UoW getActualUoW() {
        return actualUoW;
    }

    public void setActualUoW(UoW actualUoW) {
        this.actualUoW = actualUoW;
    }

    // Helpers for the this.uowOutcome attribute

    public boolean hasUoWOutcome(){
        if(this.uowOutcome == null){
            return(false);
        }
        return(true);
    }
    public UoWProcessingOutcomeEnum getUowOutcome() {
        return uowOutcome;
    }

    public void setUowOutcome(UoWProcessingOutcomeEnum uowOutcome) {
        this.uowOutcome = uowOutcome;
    }

    // Helpers for the this.upstreamParcelID attribute

    public boolean hasUpstreamParcelID(){
        if(this.upstreamParcelID==null){
            return(false);
        }
        return(true);
    }

    public FDNToken getUpstreamParcelID() {
        return(upstreamParcelID);
    }

    public void setUpstreamParcelID(FDNToken upstreamParcelID) {
        this.upstreamParcelID = upstreamParcelID;
    }

    // Helpers for the this.primaryWUPInstanceID attribute

    public boolean hasPrimaryWUPInstanceID(){
        if(this.primaryWUPInstanceID==null){
            return(false);
        }
        return(true);
    }

    public FDNToken getPrimaryWUPInstanceID() {
        return primaryWUPInstanceID;
    }

    public void setPrimaryWUPInstanceID(FDNToken primaryWUPInstanceID) {
        this.primaryWUPInstanceID = primaryWUPInstanceID;
    }

    // Helpers for the this.processingOutcome attribute

    public boolean hasProcessingOutcome(){
        if(this.processingOutcome==null){
            return(false);
        }
        return(true);
    }

    public ResilienceParcelJobCard getProcessingOutcome() {
        return processingOutcome;
    }

    public void setProcessingOutcome(ResilienceParcelJobCard processingOutcome) {
        this.processingOutcome = processingOutcome;
    }

    // Helpers for the this.parcelRegistrationDate attribute

    public boolean hasParcelRegistrationDate(){
        if(this.parcelRegistrationDate==null){
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

    // Helpers for the this.parcelStartDate attribute

    public boolean hasParcelStartDate(){
        if(this.parcelStartDate==null){
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

    // Helpers for the this.parcelFinishedDate attribute

    public boolean hasParcelFinishedDate(){
        if(this.parcelFinishedDate==null){
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

    // Helpers for the this.parcelFinalisedDate attribute

    public boolean hasParcelFinalisedDate(){
        if(this.parcelFinalisedDate==null){
            return(false);
        }
        return(true);
    }

    public Date getParcelFinalisedDate() {
        return parcelFinalisedDate;
    }

    public void setParcelFinalisedDate(Date parcelFinalisedDate) {
        this.parcelFinalisedDate = parcelFinalisedDate;
    }
}
