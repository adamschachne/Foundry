//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.09.20 at 09:34:23 PM PDT 
//


package org.isotc211._2005.gmx;

import org.opengis.gml.v_3_2_1.DictionaryType;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Constraints: - 1) metadataProperty.card = 0 - 2) dictionaryEntry.card = 0
 * 
 * <p>Java class for CodeListDictionary_Type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CodeListDictionary_Type">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.opengis.net/gml/3.2}DictionaryType">
 *       &lt;sequence>
 *         &lt;element name="codeEntry" type="{http://www.isotc211.org/2005/gmx}CodeDefinition_PropertyType" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CodeListDictionary_Type", propOrder = {
    "codeEntry"
})
@XmlSeeAlso({
    MLCodeListDictionaryType.class
})
public class CodeListDictionaryType
    extends DictionaryType
{

    @XmlElement(required = true)
    protected List<CodeDefinitionPropertyType> codeEntry;

    /**
     * Gets the value of the codeEntry property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the codeEntry property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCodeEntry().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CodeDefinitionPropertyType }
     * 
     * 
     */
    public List<CodeDefinitionPropertyType> getCodeEntry() {
        if (codeEntry == null) {
            codeEntry = new ArrayList<CodeDefinitionPropertyType>();
        }
        return this.codeEntry;
    }

}