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

package net.fhirfactory.pegacorn.petasos.core.model.map;

import net.fhirfactory.pegacorn.common.model.FDNToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashSet;

/**
 *
 * @author Mark A. Hunter
 * 
 *
 */

public class MapElement{
	private static final Logger LOG = LoggerFactory.getLogger(MapElement.class);

	private MapElementTypeEnum elementType;
	private MapElementStatusEnum elementStatus;
	private FDNToken elementID;
	private LinkedHashSet<FDNToken> downstreamRoutes;
	private LinkedHashSet<FDNToken> upstreamRoutes;

	public MapElement(){
		upstreamRoutes = new LinkedHashSet<FDNToken>();
		downstreamRoutes = new LinkedHashSet<FDNToken>();
		elementID = null;
		elementStatus = null;
		elementType = null;
	}

	public MapElement(FDNToken newElementToken, MapElementTypeEnum newElementType){
		upstreamRoutes = new LinkedHashSet<FDNToken>();
		downstreamRoutes = new LinkedHashSet<FDNToken>();
		elementID = newElementToken;
		elementType = newElementType;
		elementStatus = MapElementStatusEnum.NOT_INSTANTIATED;
	}

	public MapElementTypeEnum getElementType() {
		return elementType;
	}

	public void setElementType(MapElementTypeEnum elementType) {
		this.elementType = elementType;
	}

	public MapElementStatusEnum getElementStatus() {
		return elementStatus;
	}

	public void setElementStatus(MapElementStatusEnum elementStatus) {
		this.elementStatus = elementStatus;
	}

	public FDNToken getElementID() {
		return elementID;
	}

	public void setElementID(FDNToken elementID) {
		this.elementID = elementID;
	}

	public LinkedHashSet<FDNToken> getDownstreamRoutes() {
		return downstreamRoutes;
	}

	public void setDownstreamRoutes(LinkedHashSet<FDNToken> downstreamRoutes) {
		this.downstreamRoutes = downstreamRoutes;
	}

	public LinkedHashSet<FDNToken> getUpstreamRoutes() {
		return upstreamRoutes;
	}

	public void setUpstreamRoutes(LinkedHashSet<FDNToken> upstreamRoutes) {
		this.upstreamRoutes = upstreamRoutes;
	}
}
