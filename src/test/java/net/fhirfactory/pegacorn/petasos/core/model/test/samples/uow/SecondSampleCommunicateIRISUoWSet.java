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
package net.fhirfactory.pegacorn.petasos.core.model.test.samples.uow;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import net.fhirfactory.pegacorn.common.model.FDN;
import net.fhirfactory.pegacorn.common.model.FDNToken;
import net.fhirfactory.pegacorn.common.model.RDN;
import net.fhirfactory.pegacorn.fhir.r4.model.common.CommunicationPC;
import net.fhirfactory.pegacorn.fhir.r4.model.common.GroupPC;
import net.fhirfactory.pegacorn.petasos.core.model.uow.UoW;
import net.fhirfactory.pegacorn.petasos.core.model.uow.UoWPayload;
import net.fhirfactory.pegacorn.petasos.core.model.uow.UoWPayloadSet;
import net.fhirfactory.pegacorn.petasos.core.model.uow.UoWProcessingOutcomeEnum;
import net.fhirfactory.pegacorn.referencevalues.PegacornSystemReference;
import org.hl7.fhir.r4.model.*;
import org.hl7.fhir.utilities.xhtml.NodeType;
import org.hl7.fhir.utilities.xhtml.XhtmlNode;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Mark A. Hunter
 * @since 08-Jun-2020
 */

public class SecondSampleCommunicateIRISUoWSet {
    private static final Logger LOG = LoggerFactory.getLogger(SecondSampleCommunicateIRISUoWSet.class);

    private PegacornSystemReference defaultSystemReferences = new PegacornSystemReference() ;
    FhirContext fhirContextHandle = FhirContext.forR4();

    public String getDefaultRoomMessage() {
        LOG.debug(".getDefaultRoomMessage(): Entry");
        JSONObject newMatrixRoomInstantMessage = new JSONObject();
        JSONObject newMatrixRoomInstantMessageContent = new JSONObject();
        newMatrixRoomInstantMessageContent.put("body", "this is an instant message into a room");
        newMatrixRoomInstantMessageContent.put("format", "org.matrix.custom.html");
        newMatrixRoomInstantMessageContent.put("formatted_body", "<b>This is an example text message</b>");
        newMatrixRoomInstantMessageContent.put("msgtype", "m.text");
        newMatrixRoomInstantMessage.put("content", newMatrixRoomInstantMessageContent);
        newMatrixRoomInstantMessage.put("event_id", "$143273582443PhrSn:example.org");
        newMatrixRoomInstantMessage.put("origin_server_ts", "1432735824653");
        newMatrixRoomInstantMessage.put("room_id", "!jEsUZKDJdhlrceRyVU:example.org");
        newMatrixRoomInstantMessage.put("sender", "@example:example.org");
        newMatrixRoomInstantMessage.put("type", "m.room.message");
        JSONObject newMatrixRoomInstantMessageUnsigned = new JSONObject();
        newMatrixRoomInstantMessageUnsigned.put("age", "1234");
        newMatrixRoomInstantMessage.put("unsigned", newMatrixRoomInstantMessageUnsigned);
        String roomMessageAsString = newMatrixRoomInstantMessage.toString();
        LOG.debug(".getDefaultRoomMessage(): Exit, Room Instant Message --> {}", roomMessageAsString );
        return (roomMessageAsString);
    }


    public UoW createIngresOnlyUoW(String roomMessage) {
        LOG.debug(".createIngresOnlyUoW(): Entry, roomMessage --> {}", roomMessage );
        UoW newUoW;
        // Create RequiredWUPFDN
        FDN requiredFunctionFDN = new FDN();
        requiredFunctionFDN.appendRDN(new RDN("ProcessingPlant", "Communicate::Iris"));
        requiredFunctionFDN.appendRDN(new RDN("WUP", "Transformer-MatrixInstantMessages2FHIR"));
        // Create UoW Type
        FDN uowTypeFDN = new FDN();
        uowTypeFDN.appendRDN(new RDN("ProcessingPlant", "Communicate::Iris"));
        uowTypeFDN.appendRDN(new RDN("UoW", "matrix::m.room.instant_message-->fhir"));
        // Create Ingres Payload
        UoWPayload communicateUoWIngresPayload = new UoWPayload();
        communicateUoWIngresPayload.setPayload(roomMessage);
        FDNToken newMatrixPayloadType = new FDN();
        newMatrixPayloadType.appendRDN(new RDN("Matrix", "m.room.message"));
        newMatrixPayloadType.appendRDN(new RDN("Matrix", "m.text"));
        communicateUoWIngresPayload.setPayloadType(newMatrixPayloadType);
        UoWPayloadSet communicateUoWIngresPayloadSet = new UoWPayloadSet();
        communicateUoWIngresPayloadSet.addPayloadElement(communicateUoWIngresPayload);
        newUoW = new UoW(requiredFunctionFDN, uowTypeFDN, communicateUoWIngresPayloadSet);
        newUoW.setUowProcessingOutcome(UoWProcessingOutcomeEnum.UOW_OUTCOME_NOTSTARTED);
        LOG.debug(".createIngresOnlyUoW(): Exit, UoW created --> {}", newUoW);
        return (newUoW);
    }

    public UoW createIngresPlusEgresUoW(String roomMessage) {
        LOG.debug(".createIngresPlusEgresUoW(): Entry, Room Instant Message (m.room.message) --> {}", roomMessage);
        JSONObject roomMessageObject = new JSONObject(roomMessage);
        UoW newUoW = createIngresOnlyUoW(roomMessage);
        // Create Egress Payload
        UoWPayload communicateUoWEgressPayload = new UoWPayload();
        String fhirCommunication = buildCommunicationEntity(roomMessageObject);
        communicateUoWEgressPayload.setPayload(fhirCommunication);
        // Create Egress Payload Type FDN
        FDNToken newCommunicationPayloadType = new FDN();
        newCommunicationPayloadType.appendRDN(new RDN("FHIR", "Communication"));
        communicateUoWEgressPayload.setPayloadType(newCommunicationPayloadType);
        // Populate the Egress Payload Set
        UoWPayloadSet egressPayloadSet = new UoWPayloadSet();
        egressPayloadSet.addPayloadElement(communicateUoWEgressPayload);
        // Add Payload Set to the UoW
        newUoW.setUowEgressContent(egressPayloadSet);
        newUoW.setUowProcessingOutcome(UoWProcessingOutcomeEnum.UOW_OUTCOME_SUCCESS);
        LOG.debug(".createIngresPlusEgresUoW(): Exit, UoW Created --> {}", newUoW);
        return (newUoW);
    }

    private String buildCommunicationEntity(JSONObject roomMessage) {
        LOG.debug(".buildCommunicationEntity(): Entry, Room Instant Message (m.room.message) --> {}", roomMessage);
        if (roomMessage == null) {
            LOG.debug(".buildCommunicationEntity(): Exit, Room Instant Message (m.room.message) --> pointer is null");
            return(null);
        }
        if (roomMessage.isEmpty()) {
            LOG.debug(".buildCommunicationEntity(): Exit, Room Instant Message (m.room.message) is empty");
            return(null);
        }
        LOG.trace(".buildCommunicationEntity(): Add the FHIR::Communication.Identifier (type = FHIR::Identifier) Set");
        Identifier communicationIdentifier = this.buildCommunicationIdentifier(roomMessage);
        if (communicationIdentifier == null) {
            LOG.debug(".buildCommunicationEntity(): Exit, Could not create an Identifier!!!");
            return (null);
        }
        LOG.trace(".buildCommunicationEntity(): Add Id value (from the m.room.message::event_id");
        if (!roomMessage.has("event_id")) {
            LOG.error(".buildCommunicationEntity(): Exit, Room Instant Message (m.room.message) --> -event_id- is empty");
        }
        CommunicationPC newCommunication = new CommunicationPC();
        newCommunication.setId(roomMessage.getString("event_id"));
        LOG.trace(".buildCommunicationEntity(): Add narrative of Communication Entity");
        Narrative communicationResourceNarrative = new Narrative();
        communicationResourceNarrative.setStatus(Narrative.NarrativeStatus.GENERATED);
        XhtmlNode elementDiv = new XhtmlNode(NodeType.Document);
        elementDiv.addDocType("xmlns=\"http://www.w3.org/1999/xhtml\"");
        elementDiv.addText("<p> A message generate on the Pegacorn::Communicate::RoomServer platform </p>");
        LOG.trace(".buildCommunicationEntity(): Adding Narrative, content added --> {}", elementDiv.getContent());
        communicationResourceNarrative.setDiv(elementDiv);
        newCommunication.setText(communicationResourceNarrative);
        LOG.trace(".buildCommunicationEntity(): Set the FHIR::Communication.CommunicationStatus to COMPLETED (we don't chain, yet)");
        // TODO : Add chaining in Communication entities.
        newCommunication.setStatus(Communication.CommunicationStatus.COMPLETED);
        LOG.trace(".buildCommunicationEntity(): Set the FHIR::Communication.CommunicationPriority to ROUTINE (we make no distinction - all are real-time)");
        newCommunication.setPriority(Communication.CommunicationPriority.ROUTINE);
        LOG.trace(".buildCommunicationEntity(): Set the FHIR::Communication.sent to when the person sent the message");
        Date sentDate;
        if (roomMessage.has("origin_server_ts")) {
            sentDate = new Date(roomMessage.getLong("origin_server_ts"));
        } else {
            sentDate = Date.from(Instant.now());
        }
        newCommunication.setSent(sentDate);
        LOG.trace(".buildCommunicationEntity(): Set the FHIR::Communication. Sender to the person who sent the message");
        if (roomMessage.has("sender")) {
            String sender = roomMessage.getString("sender");
            Reference senderRef = this.buildFHIRPractitionerReferenceFromMatrixUserID(sender);
            if( senderRef == null ){
                LOG.trace(".buildDefaultCommunicationMessage(): No Communication Sender Reference created");
            } else {
                newCommunication.setSender(senderRef);
            }
        }
        LOG.trace(".buildCommunicationEntity(): Set the FHIR::Communication.Subject to the appropriate FHIR element");
        Reference newGroupReference = buildFHIRGroupReferenceFromMatrixRoomID(roomMessage.getString("room_id"));
        if( newGroupReference != null ){
            newCommunication.setSubject(newGroupReference);
        }
        LOG.trace(".buildCommunicationEntity(): Set the FHIR::Communication.Recipient to the appropriate Category (Set)");
        newCommunication.setCategory(this.buildCommunicationCategory(roomMessage));
        LOG.trace(".buildCommunicationEntity(): Populate the FHIR::Communication.payload");
        ArrayList<Communication.CommunicationPayloadComponent> localPayloadList = new ArrayList<>();
        LOG.trace(".buildCommunicationEntity(): Creating empty CommunicationPayloadComponent");
        Communication.CommunicationPayloadComponent messagePayload = new Communication.CommunicationPayloadComponent();
        LOG.trace(".buildCommunicationEntity(): Extract the -content- from the Room Instant Message");
        JSONObject messageContent = roomMessage.getJSONObject("content");
        LOG.trace(".buildCommunicationEntity(): Create a FHIR::Communication.CommunicationPayloadComponent from the message content");
        messagePayload.setContent(new StringType(messageContent.toString()));
        LOG.trace(".buildCommunicationEntity(): Add the payload to the PayloadList attribute of the FHIR::Communication element");
        localPayloadList.add(messagePayload);
        newCommunication.setPayload(localPayloadList);
        LOG.trace(".buildCommunicationEntity(): Created Identifier --> {}", newCommunication);
        IParser fhirResourceParser = fhirContextHandle.newJsonParser();
        String newCommunicationString = fhirResourceParser.encodeResourceToString(newCommunication);
        return (newCommunicationString);
    }

    private Reference buildFHIRPractitionerReferenceFromMatrixUserID(String matrixUserID) {
        LOG.debug(".buildFHIRPractitionerReferenceFromMatrixUserID(): Entry, for Matrix User ID --> {}", matrixUserID);
        // Get the associated Reference from the RoomServer.RoomID ("room_id")
        if (matrixUserID.isEmpty()) {
            LOG.error(".buildFHIRPractitionerReferenceFromMatrixUserID(): Matrix User ID missing");
            return(null);
        }
        LOG.trace(".buildFHIRPractitionerReferenceFromMatrixUserID: attempting to retrieve Practitioner Identifier from ID Map");
        Identifier practitionerId;
        LOG.trace(".buildFHIRPractitionerReferenceFromMatrixUserID(): No Mapped Identifier, creating temporary one");
        practitionerId = new Identifier();
        // Set the FHIR::Identifier.Use to "TEMP" (this id is not guaranteed)
        practitionerId.setUse(Identifier.IdentifierUse.TEMP);
        // Set the FHIR::Identifier.System to Pegacorn (it's our ID we're creating)
        practitionerId.setSystem(defaultSystemReferences.getDefaultIdentifierSystemForCommunicateGroupServer());
        // Set the FHIR::Identifier.Value to the "sender" from the RoomServer system
        practitionerId.setValue(matrixUserID);
        LOG.trace(".buildSenderReference(): Created Identifier --> {}" + practitionerId);
        LOG.trace(".buildFHIRPractitionerReferenceFromMatrixUserID(): Creatig a Reference");
        // Create the empty FHIR::Reference element
        Reference newPractitionerReference = new Reference();
        // Add the FHIR::Identifier to the FHIR::Reference.Identifier
        newPractitionerReference.setIdentifier(practitionerId);
        // Set the FHIR::Reference.type to "Group"
        newPractitionerReference.setType("Practitioner");
        LOG.debug(".buildFHIRPractitionerReferenceFromMatrixUserID(): Exit, Created FHIR Practitioner Reference --> {}", newPractitionerReference);
        return (newPractitionerReference);
    }

    private Reference buildFHIRGroupReferenceFromMatrixRoomID(String roomID)
    {
        LOG.debug(".buildFHIRGroupReferenceFromMatrixRoomID(): Entry, for Matrix Room Instant Message --> {}", roomID);
        // Get the associated Reference from the RoomServer.RoomID ("room_id")
        if (roomID == null) {
            LOG.error(".buildFHIRGroupReferenceFromMatrixRoomID(): Exit, Matrix Room ID is null");
            return(null);
        }
        if (roomID.isEmpty()) {
            LOG.error(".buildFHIRGroupReferenceFromMatrixRoomID(): Exit, Matrix Room ID is empty");
            return(null);
        }
        // Create the empty FHIR::Reference element
        Reference localSubjectReference = new Reference();
        // Create an empty FHIR::Identifier element
        Identifier localSubjectIdentifier = new Identifier();
        // Set the FHIR::Identifier.Use to "SECONDARY" (this id is not guaranteed)
        localSubjectIdentifier.setUse(Identifier.IdentifierUse.SECONDARY);
        // Set the FHIR::Identifier.System to Pegacorn (it's our ID we're creating)
        localSubjectIdentifier.setSystem(defaultSystemReferences.getDefaultIdentifierSystemForCommunicateGroupServer());
        // Set the FHIR::Identifier.Value to the "sender" from the RoomServer system
        localSubjectIdentifier.setValue(roomID);
        // Add the FHIR::Identifier to the FHIR::Reference.Identifier
        localSubjectReference.setIdentifier(localSubjectIdentifier);
        // Set the FHIR::Reference.type to "Group"
        localSubjectReference.setType("Group");
        LOG.debug(".buildFHIRGroupReferenceFromMatrixRoomID(): Created new (Temporary) FHIR::Reference for the FHIR::Group --> " + localSubjectReference.toString());
        return (localSubjectReference);
    }

    private List<CodeableConcept> buildCommunicationCategory(JSONObject roomMessage)
    {
        LOG.debug(".buildCommunicationCategory(): for Message --> " + roomMessage);
        // Create an empty list of CodeableConcept elements
        List<CodeableConcept> newCommunicationCategoryList = new ArrayList<>();
        // Create the first CodeableConcept (to capture HL7 based category)
        CodeableConcept pegacornCanonicalCodeableConcept = new CodeableConcept();
        // Create a FHIR::Coding for the CodeableConcept
        Coding pegacornCanonicalCode = new Coding();
        // Set the FHIR::Coding.code to "notification" (from the standards)
        pegacornCanonicalCode.setCode("notification");
        // Set the FHIR::Coding.system that we obtained the code from
        pegacornCanonicalCode.setSystem("http://terminology.hl7.org/CodeSystem/communication-category");
        // Set the FHIR::Coding.version associated with the code we've used
        pegacornCanonicalCode.setVersion("4.0.1"); // TODO - this needs to be a DeploymentVariable
        // Set a display name (FHIR::Coding.display) - make it nice!
        pegacornCanonicalCode.setDisplay("Notification");
        // Create an empty set of FHIR::Coding elements (this is what CodeableConcept expects)
        List<Coding> localCodingList1 = new ArrayList<>();
        // Add the FHIR::Coding above to the List
        localCodingList1.add(pegacornCanonicalCode);
        // Add the list of Codings to the first FHIR::CodeableConcept
        pegacornCanonicalCodeableConcept.setCoding(localCodingList1);
        // Add some useful text to display about the FHIR::CodeableConcept
        pegacornCanonicalCodeableConcept.setText("HL7: Communication Category = Notification ");
        // Create the 2nd CodeableConcept (to capture Pegacorn/Matrix based category)
        CodeableConcept matrixBasedCadeableConcept = new CodeableConcept();
        // Create the 1st FHIR::Coding for the 2nd CodeableConcept
        Coding matrixBasedCode = new Coding();
        // Set the FHIR::Coding.code to the (Matrix) content type (msgtype) in the
        // message
        JSONObject localMessageContentType = roomMessage.getJSONObject("content");
        matrixBasedCode.setCode("Matrix::m.room.message::" + localMessageContentType.getString("msgtype"));
        // Set the FHIR::Coding.system to point to the Matrix standard(s)
        matrixBasedCode.setSystem("https://matrix.org/docs/spec/client_server/r0.6.0");
        // Set the FHIR::Coding.system to reference the version of the Matrix standard
        // being used
        matrixBasedCode.setVersion("0.6.0");
        // Set the FHIR::Coding.display to reflect the content type from the message
        matrixBasedCode.setDisplay("Matrix.org: Room Instant Message --> Matrix::m.room.message::" + localMessageContentType.getString("msgtype"));
        // Create an empty set of FHIR::Coding elements (again, this is what
        // CodeableConcept expects)
        List<Coding> localCodingList2 = new ArrayList<>();
        // Add the FHIR::Coding to this 2nd Coding list
        localCodingList2.add(matrixBasedCode);
        // Add the lost of Codings to he 2nd FHIR::CodeableConcept element
        matrixBasedCadeableConcept.setCoding(localCodingList2);
        // Add some useful text to display about the 2nd FHIR::CodeableConcept
        matrixBasedCadeableConcept.setText("Matrix::m.room.message::" + localMessageContentType.getString("msgtype"));
        // Add the 1st Codeable Concept to the final List<CodeableConcept>
        newCommunicationCategoryList.add(pegacornCanonicalCodeableConcept);
        // Add the 2nd Codeable Concept to the final List<CodeableConcept>
        newCommunicationCategoryList.add(matrixBasedCadeableConcept);
        LOG.debug(".buildSubjectReference(): LocalCommCatList (entry 0) --> " + newCommunicationCategoryList.get(0).toString());
        LOG.debug(".buildSubjectReference(): LocalCommCatList (entry 1) --> " + newCommunicationCategoryList.get(1).toString());
        // Return the List<CodeableConcept>
        return (newCommunicationCategoryList);
    }

    private Identifier buildCommunicationIdentifier(JSONObject roomMessage)
    {
        LOG.debug(".buildCommunicationIdentifier(): Entry, Room Instant Message (m.room.message) --> {}", roomMessage);
        if (roomMessage == null) {
            LOG.debug(".buildCommunicationIdentifier(): Exit, Room Instant Message (m.room.message) --> pointer is null");
            return(null);
        }
        if (roomMessage.isEmpty()) {
            LOG.debug(".buildCommunicationIdentifier(): Exit, Room Instant Message (m.room.message) --> message is empty");
            return(null);
        }
        LOG.trace(".buildCommunicationIdentifier(): Create the empty FHIR::Identifier element");
        Identifier newCommunicationIdentifier = new Identifier();
        LOG.trace(".buildCommunicationIdentifier(): Set the FHIR::Identifier.Use to -TEMP- (Ladon needs to analyse it before its -OFFICIAL-");
        newCommunicationIdentifier.setUse(Identifier.IdentifierUse.SECONDARY);
        LOG.trace(".buildCommunicationIdentifier(): Set the FHIR::Identifier.System to Pegacorn (it's our ID we're creating)");
        newCommunicationIdentifier.setSystem(defaultSystemReferences.getDefaultIdentifierSystemForCommunicateGroupServer());
        LOG.trace(".buildCommunicationIdentifier(): Check if there is an -event_id- in the RoomServer message for binding to the Identifier, message --> {}", roomMessage);
        if (!roomMessage.has("event_id")) {
            LOG.debug(".buildCommunicationIdentifier(): Exit, Room Instant Message (m.room.message) --> message does not contain an entity_id");
            return(null);
        }
        LOG.trace(".buildCommunicationIdentifier(): {}", roomMessage);
        String event_id = roomMessage.getString("event_id");
        LOG.trace(".buildCommunicationIdentifier(): Set the FHIR::Identifier.Value to the -event_id- from the RoomServer system --> {}");
        newCommunicationIdentifier.setValue(roomMessage.getString("event_id"));
        LOG.trace(".buildCommunicationIdentifier(): Create a FHIR::Period as a container for the valid message start/end times");
        Period lEventIDPeriod = new Period();
        LOG.trace(".buildCommunicationIdentifier(): Set the FHIR::Period.start value to the time the message was created/sent");
        Date messageDate;
        if (roomMessage.has("origin_server_ts")) {
            messageDate = new Date(roomMessage.getLong("origin_server_ts"));
        } else {
            messageDate = Date.from(Instant.now());
        }
        LOG.trace(".buildCommunicationIdentifier(): Set the FHIR::Identifier.period to created FHIR::Period (our messages have not expire point)");
        newCommunicationIdentifier.setPeriod(lEventIDPeriod);
        LOG.debug(".buildCommunicationIdentifier(): Created Identifier --> {}", newCommunicationIdentifier);
        return (newCommunicationIdentifier);
    }

    public UoW createIngresPlus2EgressUoW(String roomMessage){
        LOG.debug(".createIngresPlus2EgressUoW(): Entry, Room Instant Message (m.room.message) --> {}", roomMessage);
        UoW newUoW = this.createIngresPlusEgresUoW(roomMessage);
        LOG.trace(".createIngresPlus2EgressUoW(): Create Egress Payload");
        UoWPayload groupUoWEgressPayload = new UoWPayload();
        JSONObject roomMessageObject = new JSONObject(roomMessage);
        String fhirGroup = buildGroupEntityFromRoomMessageEvent(roomMessageObject);
        groupUoWEgressPayload.setPayload(fhirGroup);
        LOG.trace(".createIngresPlus2EgressUoW(): Create Egress Payload Type FDN");
        FDNToken newCommunicationPayloadType = new FDN();
        newCommunicationPayloadType.appendRDN(new RDN("FHIR", "Group"));
        groupUoWEgressPayload.setPayloadType(newCommunicationPayloadType);
        LOG.trace(".createIngresPlus2EgressUoW(): Populate the Egress Payload Set");
        UoWPayloadSet egressPayloadSet = newUoW.getUowEgressContent();
        egressPayloadSet.addPayloadElement(groupUoWEgressPayload);
        LOG.trace(".createIngresPlus2EgressUoW(): Add Payload Set to the UoW");
        newUoW.setUowEgressContent(egressPayloadSet);
        newUoW.setUowProcessingOutcome(UoWProcessingOutcomeEnum.UOW_OUTCOME_SUCCESS);
        LOG.debug(".createIngresPlus2EgressUoW(): Exit, UoW Created --> {}", newUoW);
        return(newUoW);
    }

    private String buildGroupEntityFromRoomMessageEvent(JSONObject roomMessage){
        LOG.debug(".buildGroupEntityFromRoomNameEvent() for Event --> " + roomMessage);
        if( roomMessage == null ){
            LOG.debug(".buildGroupEntityFromRoomNameEvent(): message is null");
            return(null);
        }
        if( roomMessage.isEmpty() ){
            LOG.debug(".buildGroupEntityFromRoomNameEvent(): message is empty");
            return(null);
        }
        if( !roomMessage.has("room_id") ){
            LOG.debug(".buildGroupEntityFromRoomNameEvent(): message has no room_id");
            return(null);
        }
        LOG.trace(".buildGroupEntityFromRoomNameEvent(): Create the empty FHIR::Group entity.");
        GroupPC newGroup = new GroupPC();
        LOG.trace(".buildGroupEntityFromRoomNameEvent(): Add the FHIR::Group.Identifier (type = FHIR::Identifier) Set");
        newGroup.addIdentifier(this.buildGroupIdentifier(roomMessage));
        LOG.trace(".buildGroupEntityFromRoomNameEvent(): Add a set of FHIR::Extensions to the FHIR::Group");
        Extension localRoomPriorityValue = new Extension();
        newGroup.setGroupPriority(50);
        newGroup.setActive(true);
        newGroup.setType(Group.GroupType.PRACTITIONER);
        newGroup.setActual(true);
        LOG.trace(".buildGroupEntityFromRoomNameEvent(): Set the .name of the FHIR::Group");
        newGroup.setName(roomMessage.getString("room_id"));
        LOG.trace(".buildGroupEntityFromRoomNameEvent(): Convert the FHIR::Group into a (JSON) String");
        IParser fhirResourceParser = fhirContextHandle.newJsonParser();
        String newGroupString = fhirResourceParser.encodeResourceToString(newGroup);
        LOG.debug(".buildGroupEntityFromRoomNameEvent(): Created Identifier --> " + newGroupString);
        return (newGroupString);
    }

    private Identifier buildGroupIdentifier(JSONObject roomMessage) {
        if( roomMessage == null ){
            LOG.debug(".buildGroupIdentifier(): message is null");
            return(null);
        }
        if( roomMessage.isEmpty() ){
            LOG.debug(".buildGroupIdentifier(): message is empty");
            return(null);
        }
        if( !roomMessage.has("room_id") ){
            LOG.debug(".buildGroupIdentifier(): message has no room_id");
            return(null);
        }
        String localRoomID = roomMessage.getString("room_id");
        if (localRoomID.isEmpty()) {
            LOG.debug("buildGroupIdentifier(): Room ID from RoomServer is Empty");
            return (null);
        }
        LOG.trace(".buildGroupIdentifier(): for Event --> " + localRoomID);
        Long localGroupAge;
        if (roomMessage.has("origin_server_ts")) {
            localGroupAge = roomMessage.getLong("origin_server_ts");
        } else {
            localGroupAge = Instant.now().getEpochSecond();
        }
        // Create the empty FHIR::Identifier element
        Identifier localResourceIdentifier = this.buildFHIRGroupIdentifierFromMatrixRoomID(localRoomID, localGroupAge);
        LOG.debug(".buildGroupIdentifier(): Created Identifier --> " + localResourceIdentifier.toString());
        return (localResourceIdentifier);
    }

    public Identifier buildFHIRGroupIdentifierFromMatrixRoomID(String roomID, Long creationTime) {
        LOG.debug("buildFHIRGroupIdentifierFromMatrixRoomID(): Entry, roomID --> {}, creationTime --> {}", roomID, creationTime);
        if ((roomID == null) || roomID.isEmpty()) {
            LOG.debug("buildFHIRPractitionerIdentifierFromMatrixUserID(): Exit, roomID is empty or null");
            return (null);
        }
        // Create an empty FHIR::Identifier element
        Identifier localGroupIdentifier = new Identifier();
        // Set the FHIR::Identifier.Use to "TEMP" (this id is not guaranteed)
        localGroupIdentifier.setUse(Identifier.IdentifierUse.TEMP);
        // Set the FHIR::Identifier.System to Pegacorn (it's our ID we're creating)
        localGroupIdentifier.setSystem(this.defaultSystemReferences.getDefaultIdentifierSystemForCommunicateGroupServer());
        // Set the FHIR::Identifier.Value to the "room id" from the RoomServer system
        localGroupIdentifier.setValue(roomID);
        // Create a FHIR::Period as a container for the valid message start/end times
        Period lEventIDPeriod = new Period();
        // Set the FHIR::Period.start value to the time the message was created/sent
        if (creationTime > 0) {
            lEventIDPeriod.setStart(new Date(creationTime));
        } else {
            lEventIDPeriod.setStart(new Date());
        }
        // Set the FHIR::Identifier.period to created FHIR::Period (our messages have
        // not expire point)
        localGroupIdentifier.setPeriod(lEventIDPeriod);
        return (localGroupIdentifier);
    }
}
