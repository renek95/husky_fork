package org.projecthusky.communication.at;

import ca.uhn.hl7v2.model.v25.datatype.CX;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.CXiAssigningAuthority;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.ReferenceId;

import java.util.Objects;

@XmlAccessorType()
@XmlType(name = "ExtendedReferenceId", propOrder = {"id", "assigningAuthority", "idTypeCode", "assigningFacility"})
public class ExtendedReferenceId extends ReferenceId {
    public ExtendedReferenceId() {
        super();
    }

    public ExtendedReferenceId(CX cx) {
        super(cx);
    }

    public ExtendedReferenceId(String id, CXiAssigningAuthority assigningAuthority, String idTypeCode, CXiAssigningAuthority assigningFacility) {
        super(id, assigningAuthority, idTypeCode);
        setAssigningFacility(assigningFacility);
    }

    public CXiAssigningAuthority getAssigningFacility() {
        var assigningFacility = new CXiAssigningAuthority(getHapiObject().getCx6_AssigningFacility());
        return assigningFacility.isEmpty() ? null : assigningFacility;
    }

    public void setAssigningFacility(CXiAssigningAuthority assigningFacility) {
        setAssigningAuthority(assigningFacility, getHapiObject().getCx6_AssigningFacility());
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) return false;
        if (getClass() != o.getClass()) return false;
        var that = (ExtendedReferenceId) o;
        return Objects.equals(getAssigningFacility(), that.getAssigningFacility());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getAssigningFacility());
    }

    @Override
    public String toString() {
        return "ExtendedReferenceId(" +
                "id=" + getId() +
                ", assigningAuthority=" + getAssigningAuthority() +
                ", idTypeCode=" + getIdTypeCode() +
                ", assigningFacility=" + getAssigningFacility() +
                ')';
    }
}
