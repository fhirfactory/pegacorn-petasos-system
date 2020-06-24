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

package net.fhirfactory.pegacorn.petasos.core.model.handover;

import net.fhirfactory.pegacorn.common.model.FDNToken;
import net.fhirfactory.pegacorn.petasos.core.model.uow.UoW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.Date;

public class HandoverParcel {
    private static final Logger LOG = LoggerFactory.getLogger(HandoverParcel.class);
    private FDNToken previousParcelID;
    private UoW handoverUoW;
    private Date handoverDate;

    public HandoverParcel(FDNToken previousParcelID, UoW handoverUoW, Date handoverDate) {
        this.previousParcelID = previousParcelID;
        this.handoverUoW = handoverUoW;
        this.handoverDate = handoverDate;
    }

    public HandoverParcel(FDNToken previousParcelID, UoW handoverUoW) {
        this.previousParcelID = previousParcelID;
        this.handoverUoW = handoverUoW;
        this.handoverDate = Date.from(Instant.now());
    }

    public FDNToken getPreviousParcelID() {
        return previousParcelID;
    }

    public void setPreviousParcelID(FDNToken previousParcelID) {
        this.previousParcelID = previousParcelID;
    }

    public UoW getHandoverUoW() {
        return handoverUoW;
    }

    public void setHandoverUoW(UoW handoverUoW) {
        this.handoverUoW = handoverUoW;
    }

    public Date getHandoverDate() {
        return handoverDate;
    }

    public void setHandoverDate(Date handoverDate) {
        this.handoverDate = handoverDate;
    }
}
