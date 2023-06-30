package org.apache.beam.examples;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "books")
@XmlType(propOrder = {"name", "author", "address"})
public class Book{

    private String name;
    @XmlElement(name = "name")
    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    private String author;
    @XmlElement(name = "author")
    public String getAuthor ()
    {
        return author;
    }

    public void setAuthor (String author)
    {
        this.author = author;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [name= "+name+"]";
    }

    private Address address;

    @XmlElement(name = "address")
    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

}