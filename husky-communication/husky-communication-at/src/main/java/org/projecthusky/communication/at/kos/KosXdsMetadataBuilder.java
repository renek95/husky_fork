package org.projecthusky.communication.at.kos;

import org.openehealth.ipf.commons.ihe.xds.core.metadata.AvailabilityStatus;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Hl7v2Based;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.LocalizedString;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Timestamp;
import org.projecthusky.common.at.AuthorAt;
import org.projecthusky.common.at.enums.*;
import org.projecthusky.common.at.utils.XdsMetadataUtilAt;
import org.projecthusky.common.communication.DocumentMetadata;
import org.projecthusky.common.model.Code;
import org.projecthusky.common.model.Identificator;
import org.projecthusky.communication.at.ExtendedReferenceId;

import java.time.ZoneOffset;

/** Builds the fixed ELGA XDS-I.b metadata for a native DICOM KOS manifest. */
public final class KosXdsMetadataBuilder {
    private static final String OWN_DOCUMENT_SET_ID_TYPE_CODE = "urn:elga:iti:xds:2014:ownDocument_setId";
    private static final String ACCESSION_NUMBER_TYPE_CODE = "urn:ihe:iti:xds:2013:accession";
    private static final String CODED_LANGUAGE = "de-AT";
    private static final String CONFIDENTIALITY_CODE = "N";
    private static final String CONFIDENTIALITY_CODE_SCHEME = "2.16.840.1.113883.5.25";
    private static final String CONFIDENTIALITY_CODE_DISPLAY_NAME = "normal";

    private KosXdsMetadataBuilder() { }

    /**
     * @param author the document author; must be a real person. The ELGA fallback to
     *               Modality+Manufacturer+ManufacturerModelName as author when no performing
     *               physician is known (KOS Implementierungsleitfaden 5.1.1.2) is not implemented.
     */
    public static DocumentMetadata build(KosDocument kos, Identificator patientId, Identificator sourcePatientId,
                                          Code appc, String organizationOid,
                                          HealthcareFacilityTypeCode healthcareFacilityTypeCode,
                                          PracticeSettingCode practiceSettingCode, AuthorAt author,
                                          String homeCommunityId) {
        if (appc == null) throw new IllegalArgumentException("ELGA requires at least one APPC eventCodeList entry");
        if (kos.study().accessionNumber() == null || kos.study().accessionNumber().isBlank()) {
            throw new IllegalArgumentException(
                    "ELGA requires an accessionNumber referenceIdList entry (KOS Implementierungsleitfaden); RAD-68 would be rejected otherwise");
        }

        var metadata = new DocumentMetadata();
        metadata.setAvailabilityStatus(AvailabilityStatus.APPROVED);
        metadata.setDestinationPatientId(patientId); metadata.setSourcePatientId(sourcePatientId);
        metadata.setCodedLanguage(CODED_LANGUAGE);
        metadata.setClassCode(ClassCode.KEY_IMAGES_DOCUMENT_RADIOLOGY.getCode());
        metadata.setTypeCode(TypeCode.KEY_IMAGES_DOCUMENT_RADIOLOGY.getCode());
        metadata.setFormatCode(FormatCode.IHE_KOS_DOCUMENT.getCode());
        metadata.setMimeType("application/dicom");
        metadata.setCreationTime(kos.study().dateTime().atZone(ZoneOffset.UTC));
        metadata.setTitle(appc.getDisplayName());
        metadata.setUniqueId(kos.kosSopInstanceUid());
        metadata.getDocumentEntry().setHomeCommunityId(homeCommunityId);
        metadata.getDocumentEntry().getEventCodeList().add(org.projecthusky.common.utils.XdsMetadataUtil.convertEhcCodeToCode(appc));

        metadata.getDocumentEntry().setServiceStartTime(new Timestamp(kos.study().dateTime().atZone(ZoneOffset.UTC), null));

        org.openehealth.ipf.commons.ihe.xds.core.metadata.Code facilityTypeCode =
                new org.openehealth.ipf.commons.ihe.xds.core.metadata.Code();
        facilityTypeCode.setCode(healthcareFacilityTypeCode.getCodeValue());
        facilityTypeCode.setSchemeName(healthcareFacilityTypeCode.getCodeSystemId());
        facilityTypeCode.setDisplayName(new LocalizedString(healthcareFacilityTypeCode.getDisplayNameAt(LanguageCode.GERMAN_AT)));
        metadata.getDocumentEntry().setHealthcareFacilityTypeCode(facilityTypeCode);

        org.openehealth.ipf.commons.ihe.xds.core.metadata.Code practiceSettingCodeValue =
                new org.openehealth.ipf.commons.ihe.xds.core.metadata.Code();
        practiceSettingCodeValue.setCode(practiceSettingCode.getCodeValue());
        practiceSettingCodeValue.setSchemeName(practiceSettingCode.getCodeSystemId());
        practiceSettingCodeValue.setDisplayName(new LocalizedString(practiceSettingCode.getDisplayNameAt(LanguageCode.GERMAN_AT)));
        metadata.getDocumentEntry().setPracticeSettingCode(practiceSettingCodeValue);

        metadata.addAuthor(author);

        org.openehealth.ipf.commons.ihe.xds.core.metadata.Code confidentialityCode =
                new org.openehealth.ipf.commons.ihe.xds.core.metadata.Code();
        confidentialityCode.setCode(CONFIDENTIALITY_CODE);
        confidentialityCode.setSchemeName(CONFIDENTIALITY_CODE_SCHEME);
        confidentialityCode.setDisplayName(new LocalizedString(CONFIDENTIALITY_CODE_DISPLAY_NAME));
        metadata.getDocumentEntry().getConfidentialityCodes().add(confidentialityCode);

        metadata.getDocumentEntry().getReferenceIdList().add(referenceId(
                new Identificator(organizationOid, kos.study().instanceUid()), OWN_DOCUMENT_SET_ID_TYPE_CODE, homeCommunityId));
        metadata.getDocumentEntry().getReferenceIdList().add(referenceId(
                new Identificator(organizationOid, kos.study().accessionNumber()), ACCESSION_NUMBER_TYPE_CODE, null));

        return metadata;
    }

    private static ExtendedReferenceId referenceId(Identificator id, String identifierTypeCode, String homeCommunityId) {
        return Hl7v2Based.parse(
                XdsMetadataUtilAt.createCxi(id, identifierTypeCode, homeCommunityId),
                ExtendedReferenceId.class);
    }
}
