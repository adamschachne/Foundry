//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.09.20 at 09:34:23 PM PDT 
//


package org.isotc211._2005.gmi;

import org.isotc211._2005.gco.AbstractObjectType;
import org.isotc211.iso19139.d_2007_04_17.gmd.CICitationPropertyType;
import org.isotc211.iso19139.d_2007_04_17.gmd.MDProgressCodePropertyType;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * Description: Designations for the planning information related to meeting requirements - shortName: PlanId
 * 
 * <p>Java class for MI_Plan_Type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MI_Plan_Type">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.isotc211.org/2005/gco}AbstractObject_Type">
 *       &lt;sequence>
 *         &lt;element name="type" type="{http://www.isotc211.org/2005/gmi}MI_GeometryTypeCode_PropertyType" minOccurs="0"/>
 *         &lt;element name="status" type="{http://www.isotc211.org/2005/gmd}MD_ProgressCode_PropertyType"/>
 *         &lt;element name="citation" type="{http://www.isotc211.org/2005/gmd}CI_Citation_PropertyType"/>
 *         &lt;element name="operation" type="{http://www.isotc211.org/2005/gmi}MI_Operation_PropertyType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="satisfiedRequirement" type="{http://www.isotc211.org/2005/gmi}MI_Requirement_PropertyType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MI_Plan_Type", propOrder = {
    "type",
    "status",
    "citation",
    "operation",
    "satisfiedRequirement"
})
public class MIPlanType
    extends AbstractObjectType
{

    protected MIGeometryTypeCodePropertyType type;
    @XmlElement(required = true)
    protected MDProgressCodePropertyType status;
    @XmlElement(required = true)
    protected CICitationPropertyType citation;
    protected List<MIOperationPropertyType> operation;
    protected List<MIRequirementPropertyType> satisfiedRequirement;

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link MIGeometryTypeCodePropertyType }
     *     
     */
    public MIGeometryTypeCodePropertyType getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link MIGeometryTypeCodePropertyType }
     *     
     */
    public void setType(MIGeometryTypeCodePropertyType value) {
        this.type = value;
    }

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link MDProgressCodePropertyType }
     *     
     */
    public MDProgressCodePropertyType getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link MDProgressCodePropertyType }
     *     
     */
    public void setStatus(MDProgressCodePropertyType value) {
        this.status = value;
    }

    /**
     * Gets the value of the citation property.
     * 
     * @return
     *     possible object is
     *     {@link CICitationPropertyType }
     *     
     */
    public CICitationPropertyType getCitation() {
        return citation;
    }

    /**
     * Sets the value of the citation property.
     * 
     * @param value
     *     allowed object is
     *     {@link CICitationPropertyType }
     *     
     */
    public void setCitation(CICitationPropertyType value) {
        this.citation = value;
    }

    /**
     * Gets the value of the operation property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the operation property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOperation().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MIOperationPropertyType }
     * 
     * 
     */
    public List<MIOperationPropertyType> getOperation() {
        if (operation == null) {
            operation = new ArrayList<MIOperationPropertyType>();
        }
        return this.operation;
    }

    /**
     * Gets the value of the satisfiedRequirement property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the satisfiedRequirement property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSatisfiedRequirement().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MIRequirementPropertyType }
     * 
     * 
     */
    public List<MIRequirementPropertyType> getSatisfiedRequirement() {
        if (satisfiedRequirement == null) {
            satisfiedRequirement = new ArrayList<MIRequirementPropertyType>();
        }
        return this.satisfiedRequirement;
    }

}