package org.apache.beam.examples;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
@XmlType(propOrder = {"street", "city", "country"})
public class Address {
    private String street;
    @XmlElement(name = "street")
    public String getStreet ()
    {
        return street;
    }

    public void setStreet (String street)
    {
        this.street = street;
    }

    private String city;
    @XmlElement(name = "city")
    public String getCity ()
    {
        return this.city;
    }

    public void setCity (String city)
    {
        this.city = city;
    }

    private String country;
    @XmlElement(name = "country")
    public String getCountry ()
    {
        return this.country;
    }

    public void setCountry (String country)
    {
        this.country = country;
    }
}
