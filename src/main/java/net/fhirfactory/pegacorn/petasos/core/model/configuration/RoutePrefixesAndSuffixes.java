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

package net.fhirfactory.pegacorn.petasos.core.model.configuration;

public class RoutePrefixesAndSuffixes {

    static final String CONTRAPTION_INGRES_INGRES = "contraption_ingres_ingres";
    static final String CONTRAPTION_INGRES_EGRESS = "contraption_ingres_egress";
    static final String CONTRAPTION_EGRESS_INGRES = "NOT USED";
    static final String CONTRAPTION_EGRESS_EGRESS = "contraption_egress_egress";

    static final String WUP_INGRES = "NOT USED";
    static final String WUP_EGRESS = "wup_egress";

    static final String INTERCHANGE_INGRES = "NOT USED";
    static final String INTERCHANGE_MIDPOINT = "interchange_midpoint";
    static final String INTERCHANGE_EGRESS = "NOT USED";

    public static String getContraptionIngresIngres() {
        return CONTRAPTION_INGRES_INGRES;
    }

    public static String getContraptionIngresEgress() {
        return CONTRAPTION_INGRES_EGRESS;
    }

    public static String getContraptionEgressIngres() {
//      return CONTRAPTION_EGRESS_INGRES;
        return(WUP_EGRESS);
    }

    public static String getContraptionEgressEgress() {
        return CONTRAPTION_EGRESS_EGRESS;
    }

    public static String getWupIngres() {
//      return WUP_INGRES;
        return(CONTRAPTION_INGRES_EGRESS);
    }

    public static String getWupEgress() {
        return WUP_EGRESS;
    }

    public static String getInterchangeIngres() {
//      return INTERCHANGE_INGRES;
        return(CONTRAPTION_EGRESS_EGRESS);
    }

    public static String getInterchangeMidpoint() {
        return INTERCHANGE_MIDPOINT;
    }

//    public static String getInterchangeEgress() {
//        return INTERCHANGE_EGRESS;
//    }
}
