//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.09.20 at 09:34:23 PM PDT 
//


package org.isotc211._2005.gmi;

import org.isotc211._2005.gco.AbstractObjectType;
import org.isotc211._2005.gco.CharacterStringPropertyType;
import org.isotc211.iso19139.d_2007_04_17.gmd.CICitationPropertyType;
import org.isotc211.iso19139.d_2007_04_17.gmd.MDIdentifierPropertyType;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * Description: Comprehensive information about the procedure(s), process(es) and algorithm(s) applied in the process step - shortName: Procsg
 * 
 * <p>Java class for LE_Processing_Type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="LE_Processing_Type">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.isotc211.org/2005/gco}AbstractObject_Type">
 *       &lt;sequence>
 *         &lt;element name="identifier" type="{http://www.isotc211.org/2005/gmd}MD_Identifier_PropertyType"/>
 *         &lt;element name="softwareReference" type="{http://www.isotc211.org/2005/gmd}CI_Citation_PropertyType" minOccurs="0"/>
 *         &lt;element name="procedureDescription" type="{http://www.isotc211.org/2005/gco}CharacterString_PropertyType" minOccurs="0"/>
 *         &lt;element name="documentation" type="{http://www.isotc211.org/2005/gmd}CI_Citation_PropertyType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="runTimeParameters" type="{http://www.isotc211.org/2005/gco}CharacterString_PropertyType" minOccurs="0"/>
 *         &lt;element name="algorithm" type="{http://www.isotc211.org/2005/gmi}LE_Algorithm_PropertyType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LE_Processing_Type", propOrder = {
    "identifier",
    "softwareReference",
    "procedureDescription",
    "documentation",
    "runTimeParameters",
    "algorithm"
})
public class LEProcessingType
    extends AbstractObjectType
{

    @XmlElement(required = true)
    protected MDIdentifierPropertyType identifier;
    protected CICitationPropertyType softwareReference;
    protected CharacterStringPropertyType procedureDescription;
    protected List<CICitationPropertyType> documentation;
    protected CharacterStringPropertyType runTimeParameters;
    protected List<LEAlgorithmPropertyType> algorithm;

    /**
     * Gets the value of the identifier property.
     * 
     * @return
     *     possible object is
     *     {@link MDIdentifierPropertyType }
     *     
     */
    public MDIdentifierPropertyType getIdentifier() {
        return identifier;
    }

    /**
     * Sets the value of the identifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link MDIdentifierPropertyType }
     *     
     */
    public void setIdentifier(MDIdentifierPropertyType value) {
        this.identifier = value;
    }

    /**
     * Gets the value of the softwareReference property.
     * 
     * @return
     *     possible object is
     *     {@link CICitationPropertyType }
     *     
     */
    public CICitationPropertyType getSoftwareReference() {
        return softwareReference;
    }

    /**
     * Sets the value of the softwareReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link CICitationPropertyType }
     *     
     */
    public void setSoftwareReference(CICitationPropertyType value) {
        this.softwareReference = value;
    }

    /**
     * Gets the value of the procedureDescription property.
     * 
     * @return
     *     possible object is
     *     {@link CharacterStringPropertyType }
     *     
     */
    public CharacterStringPropertyType getProcedureDescription() {
        return procedureDescription;
    }

    /**
     * Sets the value of the procedureDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link CharacterStringPropertyType }
     *     
     */
    public void setProcedureDescription(CharacterStringPropertyType value) {
        this.procedureDescription = value;
    }

    /**
     * Gets the value of the documentation property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the documentation property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDocumentation().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CICitationPropertyType }
     * 
     * 
     */
    public List<CICitationPropertyType> getDocumentation() {
        if (documentation == null) {
            documentation = new ArrayList<CICitationPropertyType>();
        }
        return this.documentation;
    }

    /**
     * Gets the value of the runTimeParameters property.
     * 
     * @return
     *     possible object is
     *     {@link CharacterStringPropertyType }
     *     
     */
    public CharacterStringPropertyType getRunTimeParameters() {
        return runTimeParameters;
    }

    /**
     * Sets the value of the runTimeParameters property.
     * 
     * @param value
     *     allowed object is
     *     {@link CharacterStringPropertyType }
     *     
     */
    public void setRunTimeParameters(CharacterStringPropertyType value) {
        this.runTimeParameters = value;
    }

    /**
     * Gets the value of the algorithm property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the algorithm property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAlgorithm().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LEAlgorithmPropertyType }
     * 
     * 
     */
    public List<LEAlgorithmPropertyType> getAlgorithm() {
        if (algorithm == null) {
            algorithm = new ArrayList<LEAlgorithmPropertyType>();
        }
        return this.algorithm;
    }

}
