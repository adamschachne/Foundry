//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.09.20 at 09:34:23 PM PDT 
//


package org.isotc211._2005.srv;

import org.isotc211._2005.gco.AbstractObjectType;
import org.isotc211._2005.gco.MemberNamePropertyType;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for SV_Operation_Type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SV_Operation_Type">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.isotc211.org/2005/gco}AbstractObject_Type">
 *       &lt;sequence>
 *         &lt;element name="operationName" type="{http://www.isotc211.org/2005/gco}MemberName_PropertyType"/>
 *         &lt;element name="dependsOn" type="{http://www.isotc211.org/2005/srv}SV_Operation_PropertyType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="parameter" type="{http://www.isotc211.org/2005/srv}SV_Parameter_PropertyType"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SV_Operation_Type", propOrder = {
    "operationName",
    "dependsOn",
    "parameter"
})
public class SVOperationType
    extends AbstractObjectType
{

    @XmlElement(required = true)
    protected MemberNamePropertyType operationName;
    protected List<SVOperationPropertyType> dependsOn;
    @XmlElement(required = true)
    protected SVParameterPropertyType parameter;

    /**
     * Gets the value of the operationName property.
     * 
     * @return
     *     possible object is
     *     {@link MemberNamePropertyType }
     *     
     */
    public MemberNamePropertyType getOperationName() {
        return operationName;
    }

    /**
     * Sets the value of the operationName property.
     * 
     * @param value
     *     allowed object is
     *     {@link MemberNamePropertyType }
     *     
     */
    public void setOperationName(MemberNamePropertyType value) {
        this.operationName = value;
    }

    /**
     * Gets the value of the dependsOn property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the dependsOn property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDependsOn().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SVOperationPropertyType }
     * 
     * 
     */
    public List<SVOperationPropertyType> getDependsOn() {
        if (dependsOn == null) {
            dependsOn = new ArrayList<SVOperationPropertyType>();
        }
        return this.dependsOn;
    }

    /**
     * Gets the value of the parameter property.
     * 
     * @return
     *     possible object is
     *     {@link SVParameterPropertyType }
     *     
     */
    public SVParameterPropertyType getParameter() {
        return parameter;
    }

    /**
     * Sets the value of the parameter property.
     * 
     * @param value
     *     allowed object is
     *     {@link SVParameterPropertyType }
     *     
     */
    public void setParameter(SVParameterPropertyType value) {
        this.parameter = value;
    }

}
