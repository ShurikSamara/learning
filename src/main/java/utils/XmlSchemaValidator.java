package utils;

import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;

/**
 * Utility class for validating XML against XML Schema (XSD).
 */
public class XmlSchemaValidator {

    /**
     * Validates an XML file against an XSD schema file.
     *
     * @param xsdSchemaPath - path to the XSD schema file
     * @param xmlFilePath - path to the XML file to validate
     * @throws XmlValidationException if validation fails or an error occurs
     */
    public static void validate(String xsdSchemaPath, String xmlFilePath) {
        File xmlFile = new File(xmlFilePath);
        File schemaFile = new File(xsdSchemaPath);

        // Check if files exist
        if (!xmlFile.exists()) {
            System.out.println("Error: XML file not found: " + xmlFilePath);
            throw new XmlValidationException("XML file not found: " + xmlFilePath);
        }

        if (!schemaFile.exists()) {
            System.out.println("Error: XSD schema file not found: " + xsdSchemaPath);
            throw new XmlValidationException("XSD schema file not found: " + xsdSchemaPath);
        }

        try {
            // Create schema factory with security features enabled
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

            // Set security features to prevent XXE attacks
            schemaFactory.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            schemaFactory.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");

            // Create schema and validator
            Schema schema = schemaFactory.newSchema(schemaFile);
            Validator validator = schema.newValidator();

            // Set security features for validator
            validator.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            validator.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");

            // Validate XML against schema
            validator.validate(new StreamSource(xmlFile));
            System.out.println("Info: XML document is valid");
        } catch (SAXException e) {
            System.out.println("Error: XML document is invalid: " + e.getMessage());
            throw new XmlValidationException("XML validation failed", e);
        } catch (IOException e) {
            System.out.println("Error: Error reading file: " + e.getMessage());
            throw new XmlValidationException("Error reading XML file", e);
        } catch (Exception e) {
            System.out.println("Error: Unexpected error during validation: " + e.getMessage());
            throw new XmlValidationException("Unexpected error during validation", e);
        }
    }

    /**
     * Validates an XML string against an XSD schema file.
     *
     * @param xsdSchemaPath - path to the XSD schema file
     * @param xmlString - XML string to validate
     * @throws XmlValidationException if validation fails or an error occurs
     */
    public static void validateXmlString(String xsdSchemaPath, String xmlString) {
        File schemaFile = new File(xsdSchemaPath);

        // Check if schema file exists
        if (!schemaFile.exists()) {
            System.out.println("Error: XSD schema file not found: " + xsdSchemaPath);
            throw new XmlValidationException("XSD schema file not found: " + xsdSchemaPath);
        }

        try {
            // Create schema factory with security features enabled
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

            // Set security features to prevent XXE attacks
            schemaFactory.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            schemaFactory.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");

            // Create schema and validator
            Schema schema = schemaFactory.newSchema(new StreamSource(xsdSchemaPath));
            Validator validator = schema.newValidator();

            // Set security features for validator
            validator.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            validator.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");

            // Validate XML string against schema
            validator.validate(new StreamSource(new StringReader(xmlString)));
            System.out.println("Info: XML string is valid");
        } catch (SAXException e) {
            System.out.println("Error: XML string is invalid: " + e.getMessage());
            throw new XmlValidationException("XML validation failed", e);
        } catch (IOException e) {
            System.out.println("Error: Error processing XML string: " + e.getMessage());
            throw new XmlValidationException("Error processing XML string", e);
        } catch (Exception e) {
            System.out.println("Error: Unexpected error during validation: " + e.getMessage());
            throw new XmlValidationException("Unexpected error during validation", e);
        }
    }

    /**
     * For backward compatibility with existing code.
     * 
     * @param pathToXsdSchema - path to the XSD schema file
     * @param pathToXmlFile - path to the XML file to validate
     * @throws XmlValidationException if validation fails or an error occurs
     */
    public static void xmlSchemaValidator(String pathToXsdSchema, String pathToXmlFile) {
        validate(pathToXsdSchema, pathToXmlFile);
    }

    /**
     * For backward compatibility with existing code.
     * 
     * @param pathToXsdSchema - path to the XSD schema file
     * @param stringXml - XML string to validate
     * @throws XmlValidationException if validation fails or an error occurs
     */
    public static void xmlSchemaValidatorForStringXml(String pathToXsdSchema, String stringXml) {
        validateXmlString(pathToXsdSchema, stringXml);
    }
}
