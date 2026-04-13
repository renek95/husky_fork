package org.projecthusky.cda.elga.models.radiology;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import org.apache.commons.lang3.StringUtils;
import org.projecthusky.cda.elga.generated.artdecor.base.HeaderParticipantAnsprechpartner;
import org.projecthusky.cda.elga.generated.artdecor.ems.EpimsEntryHospitalization;
import org.projecthusky.cda.elga.generated.artdecor.ems.EpimsHeaderRecordTarget;
import org.projecthusky.cda.elga.generated.artdecor.ems.EpimsPatient;
import org.projecthusky.cda.elga.generated.artdecor.ems.EpimsPatientRole;
import org.projecthusky.cda.elga.models.BaseDocument;
import org.projecthusky.cda.elga.models.PatientCdaAt;
import org.projecthusky.cda.elga.models.PractitionerCdaAt;
import org.projecthusky.cda.elga.models.ems.*;
import org.projecthusky.cda.elga.utils.DateTimeUtils;
import org.projecthusky.common.at.OrganizationAt;
import org.projecthusky.common.at.enums.ClassCode;
import org.projecthusky.common.at.enums.TypeCode;
import org.projecthusky.common.hl7cdar2.*;
import org.projecthusky.common.model.Code;
import org.projecthusky.common.model.Identificator;
import org.projecthusky.common.utils.time.DateTimes;

import java.io.StringWriter;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class Radiology extends BaseDocument {

    private Identificator docId;
    private Identificator setId;
    private int version;
    private TypeCode code;
    private ClassCode classCode;
    private String title;
    private ZonedDateTime creationTime;
    private PatientCdaAt patient;
    private List<PractitionerCdaAt> authors;
    private ZonedDateTime authorTime;
    private ZonedDateTime legalAuthenticatorTime;
    private OrganizationAt custodian;
    private InformationRecipient informationRecipient;
    private PractitionerCdaAt legalAuthenticator;
    private List<HeaderParticipantAnsprechpartner> participants;

    private String serviceCode;
    private String serviceDescription;
    private String requirement;
    private String anamnese;
    private String indication;
    private String clinicalPresentation;
    private String investigation;
    private String previousInvestigations;
    private String previousFindings;
    private String complications;
    private String findings;
    private String summary;
    private String impression;
    private String conclusions;
    private String recommendation;
    private String addendum;

    private Float doseInMsv;
    private Float biradsVal;

    private ZonedDateTime startImaging;
    private ZonedDateTime stopImaging;
    private ZonedDateTime startValidityPeriod;
    private ZonedDateTime stopValidityPeriod;
    private ZonedDateTime startPossibleInfection;
    private ZonedDateTime stopPossibleInfection;
    private ZonedDateTime startPhysicianVisit;
    private ZonedDateTime stopPhysicianVisit;
    private PractitionerCdaAt reportingPhysician;
    private Identificator parentDocument;
    private CaseIdentification caseIdentification;
    private Identificator gdaId;
    private ZonedDateTime hospitalisation;
    private ClinicalManifestation clinicalManifestation;
    private Care care;
    private ActivityArea activitArea;
    private POCDMT000040Component1 componentOf;
    private ArrayList<POCDMT000040DocumentationOf> documentationsOf;

    public Identificator getDocId() {
        return docId;
    }

    public void setDocId(Identificator docId) {
        this.docId = docId;
    }

    public Identificator getSetId() {
        return setId;
    }

    public void setSetId(Identificator setId) {
        this.setId = setId;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public TypeCode getCode() {
        return code;
    }

    public void setCode(TypeCode code) {
        this.code = code;
    }

    public ClassCode getClassCode() {
        return classCode;
    }

    public void setClassCode(ClassCode classCode) {
        this.classCode = classCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ZonedDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(ZonedDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public PatientCdaAt getPatient() {
        return patient;
    }

    public void setPatient(PatientCdaAt patient) {
        this.patient = patient;
    }

    public List<PractitionerCdaAt> getAuthors() {
        return authors;
    }

    public void setAuthors(List<PractitionerCdaAt> authors) {
        this.authors = authors;
    }

    public ZonedDateTime getAuthorTime() {
        return authorTime;
    }

    public void setAuthorTime(ZonedDateTime authorTime) {
        this.authorTime = authorTime;
    }

    public ZonedDateTime getLegalAuthenticatorTime() {
        return legalAuthenticatorTime;
    }

    public void setLegalAuthenticatorTime(ZonedDateTime legalAuthenticatorTime) {
        this.legalAuthenticatorTime = legalAuthenticatorTime;
    }

    public OrganizationAt getCustodian() {
        return custodian;
    }

    public void setCustodian(OrganizationAt custodian) {
        this.custodian = custodian;
    }

    public InformationRecipient getInformationRecipient() {
        return informationRecipient;
    }

    public void setInformationRecipient(InformationRecipient informationRecipient) {
        this.informationRecipient = informationRecipient;
    }

    public PractitionerCdaAt getLegalAuthenticator() {
        return legalAuthenticator;
    }

    public void setLegalAuthenticator(PractitionerCdaAt legalAuthenticator) {
        this.legalAuthenticator = legalAuthenticator;
    }

    public ZonedDateTime getStartImaging() {
        return startImaging;
    }

    public void setStartImaging(ZonedDateTime startImaging) {
        this.startImaging = startImaging;
    }

    public ZonedDateTime getStopImaging() {
        return stopImaging;
    }

    public void setStopImaging(ZonedDateTime stopImaging) {
        this.stopImaging = stopImaging;
    }

    public ZonedDateTime getStartValidityPeriod() {
        return startValidityPeriod;
    }

    public void setStartValidityPeriod(ZonedDateTime startValidityPeriod) {
        this.startValidityPeriod = startValidityPeriod;
    }

    public ZonedDateTime getStopValidityPeriod() {
        return stopValidityPeriod;
    }

    public void setStopValidityPeriod(ZonedDateTime stopValidityPeriod) {
        this.stopValidityPeriod = stopValidityPeriod;
    }

    public ZonedDateTime getStartPossibleInfection() {
        return startPossibleInfection;
    }

    public void setStartPossibleInfection(ZonedDateTime startPossibleInfection) {
        this.startPossibleInfection = startPossibleInfection;
    }

    public ZonedDateTime getStopPossibleInfection() {
        return stopPossibleInfection;
    }

    public void setStopPossibleInfection(ZonedDateTime stopPossibleInfection) {
        this.stopPossibleInfection = stopPossibleInfection;
    }

    public ZonedDateTime getStartPhysicianVisit() {
        return startPhysicianVisit;
    }

    public void setStartPhysicianVisit(ZonedDateTime startPhysicianVisit) {
        this.startPhysicianVisit = startPhysicianVisit;
    }

    public ZonedDateTime getStopPhysicianVisit() {
        return stopPhysicianVisit;
    }

    public void setStopPhysicianVisit(ZonedDateTime stopPhysicianVisit) {
        this.stopPhysicianVisit = stopPhysicianVisit;
    }

    public PractitionerCdaAt getReportingPhysician() {
        return reportingPhysician;
    }

    public void setReportingPhysician(PractitionerCdaAt reportingPhysician) {
        this.reportingPhysician = reportingPhysician;
    }

    public Identificator getParentDocument() {
        return parentDocument;
    }

    public void setParentDocument(Identificator parentDocument) {
        this.parentDocument = parentDocument;
    }

    public Identificator getGdaId() {
        return gdaId;
    }

    public void setGdaId(Identificator gdaId) {
        this.gdaId = gdaId;
    }

    public ZonedDateTime getHospitalisation() {
        return hospitalisation;
    }

    public void setHospitalisation(ZonedDateTime hospitalisation) {
        this.hospitalisation = hospitalisation;
    }

    public CaseIdentification getCaseIdentification() {
        return caseIdentification;
    }

    public void setCaseIdentification(CaseIdentification caseIdentification) {
        this.caseIdentification = caseIdentification;
    }

    public ClinicalManifestation getClinicalManifestation() {
        return clinicalManifestation;
    }

    public void setClinicalManifestation(ClinicalManifestation clinicalManifestation) {
        this.clinicalManifestation = clinicalManifestation;
    }

    public Care getCare() {
        return care;
    }

    public void setCare(Care care) {
        this.care = care;
    }

    public ActivityArea getActivitArea() {
        return activitArea;
    }

    public void setActivitArea(ActivityArea activitArea) {
        this.activitArea = activitArea;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getServiceDescription() {
        return serviceDescription;
    }

    public void setServiceDescription(String serviceDescription) {
        this.serviceDescription = serviceDescription;
    }

    public String getRequirement() {
        return requirement;
    }

    public void setRequirement(String requirement) {
        this.requirement = requirement;
    }

    public String getAnamnese() {
        return anamnese;
    }

    public void setAnamnese(String anamnese) {
        this.anamnese = anamnese;
    }

    public String getIndication() {
        return indication;
    }

    public void setIndication(String indication) {
        this.indication = indication;
    }

    public String getClinicalPresentation() {
        return clinicalPresentation;
    }

    public void setClinicalPresentation(String clinicalPresentation) {
        this.clinicalPresentation = clinicalPresentation;
    }

    public String getInvestigation() {
        return investigation;
    }

    public void setInvestigation(String investigation) {
        this.investigation = investigation;
    }

    public String getPreviousInvestigations() {
        return previousInvestigations;
    }

    public void setPreviousInvestigations(String previousInvestigations) {
        this.previousInvestigations = previousInvestigations;
    }

    public String getPreviousFindings() {
        return previousFindings;
    }

    public void setPreviousFindings(String previousFindings) {
        this.previousFindings = previousFindings;
    }

    public String getComplications() {
        return complications;
    }

    public void setComplications(String complications) {
        this.complications = complications;
    }

    public String getFindings() {
        return findings;
    }

    public void setFindings(String findings) {
        this.findings = findings;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getImpression() {
        return impression;
    }

    public void setImpression(String impression) {
        this.impression = impression;
    }

    public String getConclusions() {
        return conclusions;
    }

    public void setConclusions(String conclusions) {
        this.conclusions = conclusions;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }

    public String getAddendum() {
        return addendum;
    }

    public void setAddendum(String addendum) {
        this.addendum = addendum;
    }

    public Float getDoseInMsv() {
        return doseInMsv;
    }

    public void setDoseInMsv(Float doseInMsv) {
        this.doseInMsv = doseInMsv;
    }

    public Float getBiradsVal() {
        return biradsVal;
    }

    public void setBiradsVal(Float biradsVal) {
        this.biradsVal = biradsVal;
    }

    protected POCDMT000040DocumentationOf getAtcdabbrHeaderDocumentationOfServiceEvent(
            ZonedDateTime start,
            ZonedDateTime stop,
            String codeValue,
            String displayName
    ) {
        //TODO ersetzen durch Logik für die Untersuchung
        POCDMT000040DocumentationOf documentationOfServiceEvent = new POCDMT000040DocumentationOf();
        POCDMT000040ServiceEvent serviceEvent = new POCDMT000040ServiceEvent();

        Code serviceEventCode = new Code();
        serviceEventCode.setCode(codeValue);
        serviceEventCode.setCodeSystem("1.2.40.0.34.5.38");
        serviceEventCode.setCodeSystemName("APPC");
        serviceEventCode.setDisplayName(displayName);

        serviceEvent.setCode(serviceEventCode.getHl7CdaR2Ce());
        serviceEvent.setEffectiveTime(DateTimeUtils.createIvlts(start, stop));

        documentationOfServiceEvent.setServiceEvent(serviceEvent);

        return documentationOfServiceEvent;
    }

    protected POCDMT000040RelatedDocument getAtcdabbrHeaderDocumentReplacementRelatedDocument() {
        POCDMT000040RelatedDocument relatedDocument = new POCDMT000040RelatedDocument();
        relatedDocument.setTypeCode(XActRelationshipDocument.RPLC);
        relatedDocument.setParentDocument(new POCDMT000040ParentDocument());
        relatedDocument.getParentDocument().getId().add(this.parentDocument.getHl7CdaR2Ii());
        return relatedDocument;
    }

    protected POCDMT000040StructuredBody getHl7CdaR2Pocdmt000040StructuredBody() {
        POCDMT000040StructuredBody structuredBody = new POCDMT000040StructuredBody();
        structuredBody.getClassCode().add("DOCBODY");
        structuredBody.getMoodCode().add("EVN");

        if (this.caseIdentification != null) {
            POCDMT000040Component3 comp3 = new POCDMT000040Component3();
            comp3.setSection(getSection());
            structuredBody.getComponent().add(comp3);
        }

        return structuredBody;
    }

    protected EpimsSectionEmssectionRadiology getSection() {
        if (this.caseIdentification == null) {
            return null;
        }

        EpimsSectionEmssectionRadiology section = new EpimsSectionEmssectionRadiology();
        section.setTitle(new ST("Befund Bildgebende Diagnostik"));
        section.getEntry().clear();
        section.getEntry().add(this.caseIdentification.getEpimsEntryCaseIdenticationArzt());

        if (this.hospitalisation != null) {
            section.getEntry().add(getHospitalisationEntry());
        }

        if (this.clinicalManifestation != null) {
            section.getEntry().add(this.clinicalManifestation.getEpimsEntryProblemConcernEntry());
        }

        return section;
    }

    public EpimsDocumentRadiology getDocument() {
        EpimsDocumentRadiology cda = new EpimsDocumentRadiology();

        addHeader(cda);

        cda.setEffectiveTime(DateTimes.toDatetimeTs(creationTime, creationTime.getZone()));

        if (code == null) {
            code = TypeCode.DIAGNOSTIC_IMAGING_STUDY;
        }
        cda.setCode(createHl7CodeFromTypeCode(code));
        cda.setTitle(new ST(title != null ? title : "Befund bildgebende Diagnostik"));

        if (patient != null) {
            cda.getRecordTarget().clear();
            cda.getRecordTarget().add(patient.getHeaderRecordTarget());
        }

        if (this.getAuthors() != null && !this.getAuthors().isEmpty()) {
            for (PractitionerCdaAt author : getAuthors()) {
                cda.getAuthor().add(author.getAtcdabbrHeaderAuthor(authorTime));
            }
        }

        if (custodian != null) {
            cda.setCustodian(this.custodian.createHeaderCustodian());
        }

        if (informationRecipient != null) {
            cda.setHl7InformationRecipient(informationRecipient.getPOCDMT000040InformationRecipient());
        }

        if (legalAuthenticator != null && this.getLegalAuthenticatorTime() != null) {
            cda.setLegalAuthenticator(legalAuthenticator.getHeaderLegalAuthenticator(this.getLegalAuthenticatorTime()));
        }

        if (participants != null && !participants.isEmpty()) {
            cda.getParticipant().addAll(participants);
        }

        cda.getDocumentationOf().add(getAtcdabbrHeaderDocumentationOfServiceEvent(this.startImaging, this.stopImaging, this.serviceCode, this.serviceDescription));
        cda.setComponentOf(componentOf);

        if (this.getParentDocument() != null && this.getParentDocument().getRoot() != null) {
            cda.getRelatedDocument().add(getAtcdabbrHeaderDocumentReplacementRelatedDocument());
        }

        POCDMT000040Component2 comp2 = new POCDMT000040Component2();
        comp2.setStructuredBody(getHl7CdaR2Pocdmt000040StructuredBodyRadiology());
        cda.setComponent(comp2);

        return cda;
    }

    public POCDMT000040Entry getHospitalisationEntry() {
        POCDMT000040Entry entry = new POCDMT000040Entry();
        EpimsEntryHospitalization hospitalizationEntryAct = new EpimsEntryHospitalization();
        hospitalizationEntryAct.setEffectiveTime(new IVLTS(DateTimes.toDatetimeTs(this.hospitalisation, ZoneId.systemDefault()).getValue()));
        entry.setAct(hospitalizationEntryAct);
        return entry;
    }

    private void addHeader(EpimsDocumentRadiology cda) {
        cda.setHl7Id(getDocId().getHl7CdaR2Ii());

        cda.setSetId(getSetId().getHl7CdaR2Ii());
        cda.setHl7VersionNumber(new INT(this.version));
    }

    /**
     * Marshalles a Arztmeldung CDA document to a {@link String}.
     *
     * @return the XML representation of the {@code clinicalDocument}.
     * @throws JAXBException if an error was encountered while creating the
     *                       marshaller.
     */
    public String marshall() throws JAXBException {
        final var jaxbContext = JAXBContext.newInstance(EpimsDocumentRadiology.class, EpimsHeaderRecordTarget.class,
                EpimsPatientRole.class, EpimsPatient.class);
        final var marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true); // Remove the marshaller XML declaration

        final var writer = new StringWriter();
        writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"); // This is required by the CCE spec
        writer.write("<?xml-stylesheet type=\"text/xsl\" href=\"ELGA_Stylesheet_v1.0.xsl\"?>\n");
        marshaller.marshal(this.getDocument(), writer);

        String cda = writer.toString();
        cda = cda.replace("xsi:type=\"epimsHeaderRecordTarget\" ", "");
        return cda;
    }

    public void addParticipant(HeaderParticipantAnsprechpartner participant) {
        if (participants == null) {
            participants = new ArrayList<>();
        }
        participants.add(participant);
    }

    public void setComponentOf(POCDMT000040Component1 componentOf) {
        this.componentOf = componentOf;
    }

    public POCDMT000040Component1 getComponentOf() {
        return componentOf;
    }

    public void addDocumentationOf(POCDMT000040DocumentationOf documentationOf) {
        if (this.documentationsOf == null) {
            this.documentationsOf = new ArrayList<>();
        }
        this.documentationsOf.add(documentationOf);
    }

    protected POCDMT000040StructuredBody getHl7CdaR2Pocdmt000040StructuredBodyRadiology() {
        POCDMT000040StructuredBody structuredBody = new POCDMT000040StructuredBody();
        structuredBody.getClassCode().add("DOCBODY");
        structuredBody.getMoodCode().add("EVN");

        String requirement = StringUtils.isNotEmpty(this.requirement) ? this.requirement : "Anforderung wird nicht bekannt gegeben";
        structuredBody.getComponent().add(createComp3FreeText(new Requirement(), requirement, "Anforderung"));

        String anamnese = StringUtils.isNotEmpty(this.anamnese) ? this.anamnese : "Anamnese wird nicht bekannt gegeben";
        structuredBody.getComponent().add(createComp3FreeText(new Anamnese(), anamnese, "Anamnese"));

        if (StringUtils.isNotEmpty(this.indication)) {
            structuredBody.getComponent().add(createComp3FreeText(new Indication(), indication, "Indikation"));
        }

        if (StringUtils.isNotEmpty(this.clinicalPresentation)) {
            structuredBody.getComponent().add(createComp3FreeText(new ClinicalPresentation(), clinicalPresentation, "Patientenstatus / Patientenangaben"));
        }

        if (StringUtils.isNotEmpty(this.investigation)) {
            String effectiveTime = DateTimes.toDatetimeTs(this.startImaging, ZoneId.systemDefault()).getValue();

            //MAMO
//            this.investigation = "Technik: Siemens Magnetom; Untersuchungsparameter: axial gewichtete T1, T2-gewichtete TSE- und ultraschnelle GRE-Sequenzen vor und nach iv. Applikation von Clariscan, Substraktion, dynamische Kontrastmittelauswertung; axiale EPI-gewichtete Diffusionssequenz";

            if (this.doseInMsv != null) {
                structuredBody.getComponent().add(createComp3WithDoseTable(new Investigation(this.doseInMsv, effectiveTime), investigation, "Aktuelle Untersuchung", this.doseInMsv));
            } else {
                structuredBody.getComponent().add(createComp3FreeText(new Investigation(this.doseInMsv, effectiveTime), investigation, "Aktuelle Untersuchung"));
            }
        }

        if (StringUtils.isNotEmpty(this.previousInvestigations)) {
            structuredBody.getComponent().add(createComp3FreeText(new PreviousInvestigation(), previousInvestigations, "Frühere Untersuchungen"));
        }

        if (StringUtils.isNotEmpty(this.previousFindings)) {
            structuredBody.getComponent().add(createComp3FreeText(new PreviousFindings(), previousFindings, "Frühere Befunde"));
        }

        if (StringUtils.isNotEmpty(this.complications)) {
            structuredBody.getComponent().add(createComp3FreeText(new Complications(), complications, "Komplikationen"));
        }

        if (StringUtils.isNotEmpty(this.findings)) {
            if (this.biradsVal != null) {
//                this.findings = "Dichter fibrozystisch transformierter Drüsenkörper bilateral, keine Läsionen mit malignitätstypischer Kontrastmittelkinetik und keine soliden expansiven Läsionen nachweisbar. Die Thoraxwand unauffällig, in den mitdargestellten Anteil der Axillen keine pathologisch vergrößerten Lymphknoten nachweisbar.";

                Findings findingObj = new Findings();
                findingObj.addClassificationTable();
                findingObj.addClassificationRow("BI-RADS", "birads", this.biradsVal.toString());

                String effectiveTime = DateTimes.toDatetimeTs(this.startImaging, ZoneId.systemDefault()).getValue();
                findingObj.addFindingEntry("finding-1");
                findingObj.addBiradsEntry("II.AC.b.1", effectiveTime);
                structuredBody.getComponent().add(createComp3WithMammo(findingObj, findings, "Befund", this.biradsVal));
            } else {
                structuredBody.getComponent().add(createComp3FreeText(new Findings(), findings, "Befund"));
            }
        }

        if (StringUtils.isNotEmpty(this.summary)) {
            structuredBody.getComponent().add(createComp3FreeText(new Summary(), summary, "Zusammenfassung / Ergebnis"));
        }

        if (StringUtils.isNotEmpty(this.impression)) {
            structuredBody.getComponent().add(createComp3FreeText(new Impression(), impression, "Verdachtsdiagnose"));
        }

        if (StringUtils.isNotEmpty(this.conclusions)) {
            structuredBody.getComponent().add(createComp3FreeText(new Conclusion(), conclusions, "Schlussfolgerung"));
        }

        if (StringUtils.isNotEmpty(this.recommendation)) {
            structuredBody.getComponent().add(createComp3FreeText(new Recomendation(), recommendation, "Schlussfolgerung"));
        }

        if (StringUtils.isNotEmpty(this.addendum)) {
            structuredBody.getComponent().add(createComp3FreeText(new Addendum(), addendum, "Addendum"));
        }

        //TODO Brieftext Allgemeiner Leitfaden
        //TODO Abschließende Bemerkungen Allgemeiner Leitfaden
        //TODO Schlüsselbilder -> Nachfragen

        return structuredBody;
    }

    private CE createHl7CodeFromTypeCode(TypeCode typeCode) {
        ObjectFactory factory = new ObjectFactory();
        CE retVal = factory.createCE();
        assert retVal != null;
        retVal.setCode(typeCode.getCodeValue());
        retVal.setCodeSystem(typeCode.getCodeSystemId());
        retVal.setCodeSystemName(typeCode.getCodeSystemName());
        retVal.setDisplayName(typeCode.getDisplayName());
        return retVal;
    }
}
