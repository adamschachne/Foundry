//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.09.20 at 09:34:23 PM PDT 
//


package org.isotc211._2005.srv;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for SV_PlatformSpecificServiceSpecification_Type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SV_PlatformSpecificServiceSpecification_Type">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.isotc211.org/2005/srv}AbstractSV_AbstractServiceSpecification_Type">
 *       &lt;sequence>
 *         &lt;element name="DCP" type="{http://www.isotc211.org/2005/srv}DCPList_PropertyType"/>
 *         &lt;element name="typeSpec" type="{http://www.isotc211.org/2005/srv}SV_PlatformNeutralServiceSpecification_PropertyType"/>
 *         &lt;element name="implementation" type="{http://www.isotc211.org/2005/srv}SV_Service_PropertyType" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SV_PlatformSpecificServiceSpecification_Type", propOrder = {
    "dcp",
    "typeSpec",
    "implementation"
})
public class SVPlatformSpecificServiceSpecificationType
    extends AbstractSVAbstractServiceSpecificationType
{

    @XmlElement(name = "DCP", required = true)
    protected DCPListPropertyType dcp;
    @XmlElement(required = true)
    protected SVPlatformNeutralServiceSpecificationPropertyType typeSpec;
    @XmlElement(required = true)
    protected List<SVServicePropertyType> implementation;

    /**
     * Gets the value of the dcp property.
     * 
     * @return
     *     possible object is
     *     {@link DCPListPropertyType }
     *     
     */
    public DCPListPropertyType getDCP() {
        return dcp;
    }

    /**
     * Sets the value of the dcp property.
     * 
     * @param value
     *     allowed object is
     *     {@link DCPListPropertyType }
     *     
     */
    public void setDCP(DCPListPropertyType value) {
        this.dcp = value;
    }

    /**
     * Gets the value of the typeSpec property.
     * 
     * @return
     *     possible object is
     *     {@link SVPlatformNeutralServiceSpecificationPropertyType }
     *     
     */
    public SVPlatformNeutralServiceSpecificationPropertyType getTypeSpec() {
        return typeSpec;
    }

    /**
     * Sets the value of the typeSpec property.
     * 
     * @param value
     *     allowed object is
     *     {@link SVPlatformNeutralServiceSpecificationPropertyType }
     *     
     */
    public void setTypeSpec(SVPlatformNeutralServiceSpecificationPropertyType value) {
        this.typeSpec = value;
    }

    /**
     * Gets the value of the implementation property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the implementation property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getImplementation().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SVServicePropertyType }
     * 
     * 
     */
    public List<SVServicePropertyType> getImplementation() {
        if (implementation == null) {
            implementation = new ArrayList<SVServicePropertyType>();
        }
        return this.implementation;
    }

}
