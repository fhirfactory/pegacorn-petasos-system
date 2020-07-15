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
package net.fhirfactory.pegacorn.petasos.model.topology;

import net.fhirfactory.pegacorn.common.model.FDNToken;
import net.fhirfactory.pegacorn.petasos.model.resilience.mode.DeploymentResilienceMode;
import net.fhirfactory.pegacorn.petasos.model.resilience.mode.ConcurrencyMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fhirfactory.pegacorn.common.model.FDNTokenSet;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Mark A. Hunter
 *
 *
 */
public class NodeElement {

    private static final Logger LOG = LoggerFactory.getLogger(NodeElement.class);

    private NodeElementInstanceTypeEnum topologyElementType;
    private ElementMapStatusEnum topologyElementStatus;
    private FDNToken elementInstanceID;
    private FDNToken elementFunctionTypeID;
    private FDNTokenSet links;
    private FDNTokenSet endpoints;
    private FDNTokenSet containedElements;
    private FDNToken containingElementID;
    private String elementVersion;
    private DeploymentResilienceMode resilienceMode;
    private ConcurrencyMode concurrencyMode;
    private HashMap<String, String> elementExtensions;

    public FDNToken getContainingElementID() {
        return containingElementID;
    }

    public void setContainingElementID(FDNToken containingElementID) {
        this.containingElementID = containingElementID;
    }

    public NodeElement() {
        this.endpoints = new FDNTokenSet();
        this.links = new FDNTokenSet();
        this.containedElements = new FDNTokenSet();
        this.elementExtensions = new HashMap<String, String>();
        this.elementInstanceID = null;
        this.topologyElementStatus = null;
        this.topologyElementType = null;
    }

    public NodeElement(FDNToken elementInstanceID) {
        this.endpoints = new FDNTokenSet();
        this.links = new FDNTokenSet();
        this.containedElements = new FDNTokenSet();
        this.elementExtensions = new HashMap<String, String>();
        this.elementInstanceID = elementInstanceID;
        this.concurrencyMode = ConcurrencyMode.PETASOS_WUA_CONCURRENCY_MODE_STANDALONE;
        this.resilienceMode = DeploymentResilienceMode.PETASOS_MODE_STANDALONE;
        this.topologyElementStatus = ElementMapStatusEnum.NOT_INSTANTIATED;
    }

    public NodeElementInstanceTypeEnum getTopologyElementType() {
        return topologyElementType;
    }

    public void setTopologyElementType(NodeElementInstanceTypeEnum topologyElementType) {
        this.topologyElementType = topologyElementType;
    }

    public ElementMapStatusEnum getTopologyElementStatus() {
        return topologyElementStatus;
    }

    public void setTopologyElementStatus(ElementMapStatusEnum topologyElementStatus) {
        this.topologyElementStatus = topologyElementStatus;
    }

    public FDNToken getElementInstanceID() {
        return elementInstanceID;
    }

    public void setElementInstanceID(FDNToken elementInstanceID) {
        this.elementInstanceID = elementInstanceID;
    }

    public FDNTokenSet getLinks() {
        return links;
    }

    public void setLinks(FDNTokenSet links) {
        this.links = links;
    }

    public FDNTokenSet getEndpoints() {
        return endpoints;
    }

    public void setEndpoints(FDNTokenSet endpoints) {
        this.endpoints = endpoints;
    }

    public DeploymentResilienceMode getResilienceMode() {
        return resilienceMode;
    }

    public void setResilienceMode(DeploymentResilienceMode resilienceMode) {
        this.resilienceMode = resilienceMode;
    }

    public ConcurrencyMode getConcurrencyMode() {
        return concurrencyMode;
    }

    public void setConcurrencyMode(ConcurrencyMode concurrencyMode) {
        this.concurrencyMode = concurrencyMode;
    }

    public boolean hasContainedElements() {
        if (this.containedElements == null) {
            return (false);
        } else {
            return (true);
        }
    }

    public FDNTokenSet getContainedElements() {
        return (this.containedElements);
    }

    public void setContainedElements(FDNTokenSet newContainedElementSet) {
        this.containedElements = newContainedElementSet;
    }

    public void addContainedElement(FDNToken newContainedElement) {
        if (!hasContainedElements()) {
            this.containedElements = new FDNTokenSet();
        }
        this.containedElements.addElement(newContainedElement);
    }
    
    public void removeContainedElement(FDNToken containedElementToBeRemoved){
        if(hasContainedElements()){
            this.containedElements.removeElement(containedElementToBeRemoved);
        }
    }

    public FDNToken getElementFunctionTypeID() {
        return elementFunctionTypeID;
    }

    public void setElementFunctionTypeID(FDNToken elementFunctionTypeID) {
        this.elementFunctionTypeID = elementFunctionTypeID;
    }

    public String getElementVersion() {
        return elementVersion;
    }

    public void setElementVersion(String elementVersion) {
        this.elementVersion = elementVersion;
    }

    public Map<String, String> getElementExtensions() {
        return elementExtensions;
    }

    public void setElementExtensions(HashMap<String, String> elementExtensions) {
        this.elementExtensions = elementExtensions;
    }
}
