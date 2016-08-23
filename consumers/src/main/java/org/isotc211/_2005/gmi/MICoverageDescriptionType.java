//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.09.20 at 09:34:23 PM PDT 
//


package org.isotc211._2005.gmi;

import org.isotc211.iso19139.d_2007_04_17.gmd.MDCoverageDescriptionType;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * Description: information about the content of a coverage, including the description of specific range elements - shortName: CCovDesc
 * 
 * <p>Java class for MI_CoverageDescription_Type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MI_CoverageDescription_Type">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.isotc211.org/2005/gmd}MD_CoverageDescription_Type">
 *       &lt;sequence>
 *         &lt;element name="rangeElementDescription" type="{http://www.isotc211.org/2005/gmi}MI_RangeElementDescription_PropertyType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MI_CoverageDescription_Type", propOrder = {
    "rangeElementDescription"
})
public class MICoverageDescriptionType
    extends MDCoverageDescriptionType
{

    protected List<MIRangeElementDescriptionPropertyType> rangeElementDescription;

    /**
     * Gets the value of the rangeElementDescription property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the rangeElementDescription property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRangeElementDescription().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MIRangeElementDescriptionPropertyType }
     * 
     * 
     */
    public List<MIRangeElementDescriptionPropertyType> getRangeElementDescription() {
        if (rangeElementDescription == null) {
            rangeElementDescription = new ArrayList<MIRangeElementDescriptionPropertyType>();
        }
        return this.rangeElementDescription;
    }

}