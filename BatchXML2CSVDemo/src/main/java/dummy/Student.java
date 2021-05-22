package dummy;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.stereotype.Component;

@Component
@XmlRootElement(name = "student")
public class Student {
	
	 private String name;
	 
	 private String emailAddress;
	 
	 private String purchasedPackage;

	public Student() {
		super();
	}

	public Student(String name, String emailAddress, String purchasedPackage) {
		this.name = name;
		this.emailAddress = emailAddress;
		this.purchasedPackage = purchasedPackage;
	}

	@XmlElement(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElement(name = "emailAddress")
	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	@XmlElement(name = "purchasedPackage")
	public String getPurchasedPackage() {
		return purchasedPackage;
	}

	public void setPurchasedPackage(String purchasedPackage) {
		this.purchasedPackage = purchasedPackage;
	}

	@Override
	public String toString() {
		return "Student [name=" + name + ", emailAddress=" + emailAddress + ", purchasedPackage=" + purchasedPackage
				+ "]";
	}
}
