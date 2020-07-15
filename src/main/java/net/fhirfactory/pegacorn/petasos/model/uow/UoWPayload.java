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
package net.fhirfactory.pegacorn.petasos.model.uow;

import net.fhirfactory.pegacorn.common.model.FDNToken;

/**
 * @author Mark A. Hunter
 */
public class UoWPayload {
    private FDNToken payloadInstanceID;
    private FDNToken payloadTypeID;
    private FDNToken payloadTopicID;
    private String payload;

    public UoWPayload() {
        payloadTypeID = null;
        payload = null;
        payloadTopicID = null;
    }

    public UoWPayload(UoWPayload originalUoWPayload) {
        payloadTypeID = new FDNToken(originalUoWPayload.getPayloadTypeID());
        payload = new String(originalUoWPayload.getPayload());
        payloadTopicID = new FDNToken(originalUoWPayload.getPayloadTopicID());
        payloadInstanceID = new FDNToken(originalUoWPayload.getPayloadInstanceID());
    }

    public UoWPayload(FDNToken payloadType, String payloadContent){
        this.payloadTypeID = new FDNToken(payloadType);
        this.payload = new String(payloadContent);
    }

    public FDNToken getPayloadTypeID() {
        return payloadTypeID;
    }

    public void setPayloadTypeID(FDNToken payloadTypeID) {
        this.payloadTypeID = payloadTypeID;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public FDNToken getPayloadTopicID() {
        return payloadTopicID;
    }

    public void setPayloadTopicID(FDNToken payloadTopicID) {
        this.payloadTopicID = payloadTopicID;
    }

    public FDNToken getPayloadInstanceID() {
        return payloadInstanceID;
    }

    public void setPayloadInstanceID(FDNToken payloadInstanceID) {
        this.payloadInstanceID = payloadInstanceID;
    }
}
