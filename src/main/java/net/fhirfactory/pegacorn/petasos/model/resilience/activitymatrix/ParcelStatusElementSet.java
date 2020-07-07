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

package net.fhirfactory.pegacorn.petasos.model.resilience.activitymatrix;

import net.fhirfactory.pegacorn.common.model.FDNToken;
import net.fhirfactory.pegacorn.common.model.FDNTokenSet;
import net.fhirfactory.pegacorn.petasos.model.pathway.ContinuityID;

import java.time.Instant;
import java.util.Date;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

public class ParcelStatusElementSet {
    private ConcurrentHashMap<FDNToken, ParcelStatusElement> parcelInstanceSet;

    public ParcelStatusElementSet() {
        this.parcelInstanceSet = new ConcurrentHashMap<FDNToken, ParcelStatusElement>() ;
    }

    public boolean isEmpty(){
        if(parcelInstanceSet.isEmpty()){
            return(true);
        } else {
            return(false);
        }
    }

    public ParcelStatusElement addElement(ContinuityID activityID){
        if(activityID == null){
            throw(new IllegalArgumentException(".addElement(): activityID is null"));
        }
        ParcelStatusElement newElement = new ParcelStatusElement(activityID);
        newElement.setHasClusterFocus(true);
        if(parcelInstanceSet.containsKey(activityID.getPresentResilienceParcelInstanceID())){
            parcelInstanceSet.replace(activityID.getPresentResilienceParcelInstanceID(), newElement);
        } else {
            parcelInstanceSet.put(activityID.getPresentResilienceParcelInstanceID(), newElement);
        }
        return(newElement);
    }

    public void removeElement(FDNToken parcelInstanceID){
        if(parcelInstanceID == null){
            throw(new IllegalArgumentException(".addElement(): parcelInstanceID is null"));
        }
        if(parcelInstanceSet.containsKey(parcelInstanceID)){
            parcelInstanceSet.remove(parcelInstanceID);
        }
    }

    public ParcelStatusElement getElement(FDNToken parcelInstanceID){
        if(!hasElement(parcelInstanceID)){
            return(null);
        }
        ParcelStatusElement extractedElement = parcelInstanceSet.get(parcelInstanceID);
        return(extractedElement);
    }

    public boolean hasElement(FDNToken parcelInstanceID){
        if(parcelInstanceID==null){
            return(false);
        }
        if(parcelInstanceSet.containsKey(parcelInstanceID)){
            return(true);
        }
        return(false);
    }

    public ParcelStatusElement getClusteredFocusElement(){
        Enumeration<FDNToken> parcelInstanceEnumerator = this.parcelInstanceSet.keys();
        while(parcelInstanceEnumerator.hasMoreElements()){
            FDNToken parcelInstanceID = parcelInstanceEnumerator.nextElement();
            ParcelStatusElement parcelElement = parcelInstanceSet.get(parcelInstanceID);
            if(parcelElement.getHasClusterFocus()){
                return(parcelElement);
            }
        }
        return(null);
    }

    public void setClusteredFocusElement(FDNToken parcelInstanceID){
        Enumeration<FDNToken> parcelInstanceEnumerator = this.parcelInstanceSet.keys();
        while(parcelInstanceEnumerator.hasMoreElements()){
            FDNToken currentParcelInstanceID = parcelInstanceEnumerator.nextElement();
            ParcelStatusElement parcelElement = parcelInstanceSet.get(currentParcelInstanceID);
            if(parcelInstanceID.equals(currentParcelInstanceID)){
                parcelElement.setHasClusterFocus(true);
                parcelElement.setEntryDate(Date.from(Instant.now()));
            } else {
                if(parcelElement.getHasClusterFocus()) {
                    parcelElement.setHasClusterFocus(false);
                    parcelElement.setEntryDate(Date.from(Instant.now()));
                }
            }
        }
    }

    public ParcelStatusElement getSiteWideFocusElement(){
        Enumeration<FDNToken> parcelInstanceEnumerator = this.parcelInstanceSet.keys();
        while(parcelInstanceEnumerator.hasMoreElements()){
            FDNToken parcelInstanceID = parcelInstanceEnumerator.nextElement();
            ParcelStatusElement parcelElement = parcelInstanceSet.get(parcelInstanceID);
            if(parcelElement.getHasSystemWideFocus()){
                return(parcelElement);
            }
        }
        return(null);
    }

    public void setSystemWideElement(FDNToken parcelInstanceID){
        Enumeration<FDNToken> parcelInstanceEnumerator = this.parcelInstanceSet.keys();
        while(parcelInstanceEnumerator.hasMoreElements()){
            FDNToken currentParcelInstanceID = parcelInstanceEnumerator.nextElement();
            ParcelStatusElement parcelElement = parcelInstanceSet.get(currentParcelInstanceID);
            if(parcelInstanceID.equals(currentParcelInstanceID)){
                parcelElement.setHasSystemWideFocus(true);
                parcelElement.setEntryDate(Date.from(Instant.now()));
            } else {
                if(parcelElement.getHasSystemWideFocus()) {
                    parcelElement.setHasSystemWideFocus(false);
                    parcelElement.setEntryDate(Date.from(Instant.now()));
                }
            }
        }
    }

    public FDNTokenSet getWUPInstanceSet(){
        FDNTokenSet wupInstanceTokenSet = new FDNTokenSet();
        if(this.parcelInstanceSet.isEmpty()){
            return(wupInstanceTokenSet);
        }
        Enumeration<FDNToken> parcelInstanceEnumerator = this.parcelInstanceSet.keys();
        while(parcelInstanceEnumerator.hasMoreElements()) {
            FDNToken parcelInstanceID = parcelInstanceEnumerator.nextElement();
            ParcelStatusElement parcelElement = parcelInstanceSet.get(parcelInstanceID);
            wupInstanceTokenSet.addElement(parcelElement.getWupInstanceID());
        }
        return(wupInstanceTokenSet);
    }

    public FDNTokenSet getParcelInstanceSet(){
        FDNTokenSet parcelInstanceTokenSet = new FDNTokenSet();
        if(this.parcelInstanceSet.isEmpty()){
            return(parcelInstanceTokenSet);
        }
        Enumeration<FDNToken> parcelInstanceEnumerator = this.parcelInstanceSet.keys();
        while(parcelInstanceEnumerator.hasMoreElements()) {
            FDNToken parcelInstanceID = parcelInstanceEnumerator.nextElement();
            parcelInstanceTokenSet.addElement(parcelInstanceID);
        }
        return(parcelInstanceTokenSet);
    }
}
