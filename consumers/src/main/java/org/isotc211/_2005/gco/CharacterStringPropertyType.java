//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.09.20 at 09:34:23 PM PDT 
//


package org.isotc211._2005.gco;

import org.isotc211._2005.gmx.AnchorType;
import org.isotc211._2005.gmx.FileNameType;
import org.isotc211._2005.gmx.MimeFileTypeType;
import org.isotc211._2005.srv.SVOperationModelType;
import org.isotc211._2005.srv.SVParameterDirectionType;
import org.isotc211.iso19139.d_2007_04_17.gmd.*;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for CharacterString_PropertyType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CharacterString_PropertyType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence minOccurs="0">
 *         &lt;element ref="{http://www.isotc211.org/2005/gco}CharacterString"/>
 *       &lt;/sequence>
 *       &lt;attribute ref="{http://www.isotc211.org/2005/gco}nilReason"/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CharacterString_PropertyType", propOrder = {
    "characterString"
})
@XmlSeeAlso({
    PTFreeTextPropertyType.class
})
public class CharacterStringPropertyType {

    @XmlElementRef(name = "CharacterString", namespace = "http://www.isotc211.org/2005/gco", type = JAXBElement.class, required = false)
    protected JAXBElement<?> characterString;
    @XmlAttribute(name = "nilReason", namespace = "http://www.isotc211.org/2005/gco")
    protected List<String> nilReason;

    /**
     * Gets the value of the characterString property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link MimeFileTypeType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CodeListValueType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CodeListValueType }{@code >}
     *     {@link JAXBElement }{@code <}{@link SVOperationModelType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CodeListValueType }{@code >}
     *     {@link JAXBElement }{@code <}{@link FileNameType }{@code >}
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     {@link JAXBElement }{@code <}{@link CodeListValueType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CodeListValueType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CodeListValueType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CodeListValueType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CodeListValueType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CodeListValueType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CodeListValueType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CodeListValueType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CodeListValueType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CodeListValueType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CodeListValueType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CodeListValueType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CodeListValueType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CodeListValueType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CodeListValueType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CodeListValueType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CodeListValueType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CodeListValueType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CodeListValueType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CodeListValueType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CodeListValueType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CodeListValueType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CodeListValueType }{@code >}
     *     {@link JAXBElement }{@code <}{@link LocalisedCharacterStringType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CodeListValueType }{@code >}
     *     {@link JAXBElement }{@code <}{@link MDObligationCodeType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CodeListValueType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CodeListValueType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CodeListValueType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CodeListValueType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CodeListValueType }{@code >}
     *     {@link JAXBElement }{@code <}{@link SVParameterDirectionType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CodeListValueType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CodeListValueType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CodeListValueType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CodeListValueType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CodeListValueType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CodeListValueType }{@code >}
     *     {@link JAXBElement }{@code <}{@link MDTopicCategoryCodeType }{@code >}
     *     {@link JAXBElement }{@code <}{@link MDPixelOrientationCodeType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AnchorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CodeListValueType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CodeListValueType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CodeListValueType }{@code >}
     *     
     */
    public JAXBElement<?> getCharacterString() {
        return characterString;
    }

    /**
     * Sets the value of the characterString property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link MimeFileTypeType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CodeListValueType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CodeListValueType }{@code >}
     *     {@link JAXBElement }{@code <}{@link SVOperationModelType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CodeListValueType }{@code >}
     *     {@link JAXBElement }{@code <}{@link FileNameType }{@code >}
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     {@link JAXBElement }{@code <}{@link CodeListValueType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CodeListValueType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CodeListValueType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CodeListValueType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CodeListValueType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CodeListValueType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CodeListValueType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CodeListValueType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CodeListValueType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CodeListValueType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CodeListValueType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CodeListValueType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CodeListValueType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CodeListValueType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CodeListValueType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CodeListValueType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CodeListValueType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CodeListValueType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CodeListValueType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CodeListValueType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CodeListValueType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CodeListValueType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CodeListValueType }{@code >}
     *     {@link JAXBElement }{@code <}{@link LocalisedCharacterStringType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CodeListValueType }{@code >}
     *     {@link JAXBElement }{@code <}{@link MDObligationCodeType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CodeListValueType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CodeListValueType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CodeListValueType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CodeListValueType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CodeListValueType }{@code >}
     *     {@link JAXBElement }{@code <}{@link SVParameterDirectionType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CodeListValueType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CodeListValueType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CodeListValueType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CodeListValueType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CodeListValueType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CodeListValueType }{@code >}
     *     {@link JAXBElement }{@code <}{@link MDTopicCategoryCodeType }{@code >}
     *     {@link JAXBElement }{@code <}{@link MDPixelOrientationCodeType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AnchorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CodeListValueType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CodeListValueType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CodeListValueType }{@code >}
     *     
     */
    public void setCharacterString(JAXBElement<?> value) {
        this.characterString = value;
    }

    /**
     * Gets the value of the nilReason property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the nilReason property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNilReason().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getNilReason() {
        if (nilReason == null) {
            nilReason = new ArrayList<String>();
        }
        return this.nilReason;
    }

}
