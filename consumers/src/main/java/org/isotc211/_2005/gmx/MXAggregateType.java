//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.09.20 at 09:34:23 PM PDT 
//


package org.isotc211._2005.gmx;

import org.isotc211.iso19139.d_2007_04_17.gmd.AbstractDSAggregateType;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for MX_Aggregate_Type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MX_Aggregate_Type">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.isotc211.org/2005/gmd}AbstractDS_Aggregate_Type">
 *       &lt;sequence>
 *         &lt;element name="aggregateCatalogue" type="{http://www.isotc211.org/2005/gmx}CT_Catalogue_PropertyType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="aggregateFile" type="{http://www.isotc211.org/2005/gmx}MX_SupportFile_PropertyType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MX_Aggregate_Type", propOrder = {
    "aggregateCatalogue",
    "aggregateFile"
})
public class MXAggregateType
    extends AbstractDSAggregateType
{

    protected List<CTCataloguePropertyType> aggregateCatalogue;
    protected List<MXSupportFilePropertyType> aggregateFile;

    /**
     * Gets the value of the aggregateCatalogue property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the aggregateCatalogue property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAggregateCatalogue().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CTCataloguePropertyType }
     * 
     * 
     */
    public List<CTCataloguePropertyType> getAggregateCatalogue() {
        if (aggregateCatalogue == null) {
            aggregateCatalogue = new ArrayList<CTCataloguePropertyType>();
        }
        return this.aggregateCatalogue;
    }

    /**
     * Gets the value of the aggregateFile property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the aggregateFile property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAggregateFile().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MXSupportFilePropertyType }
     * 
     * 
     */
    public List<MXSupportFilePropertyType> getAggregateFile() {
        if (aggregateFile == null) {
            aggregateFile = new ArrayList<MXSupportFilePropertyType>();
        }
        return this.aggregateFile;
    }

}
