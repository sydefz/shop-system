package ESMAssign;

import java.text.*;
import java.util.*;

public class Product {

	public String name;
	public Date boughtOn;
	public Date useBy;
	public double boughtAt;
	public int inQuantity;

	
	public Product() {
		DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		try {
			this.useBy = sdf.parse("01-01-1000");
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getBoughtOn() {
		return boughtOn;
	}

	public void setBoughtOn(Date boughtOn) {
		this.boughtOn = boughtOn;
	}

	public Date getUseBy() {
		return useBy;
	}

	public void setUseBy(Date useBy) {
		this.useBy = useBy;
		
	}

	public double getBoughtAt() {
		return boughtAt;
	}

	public void setBoughtAt(double boughtAt) {
		this.boughtAt = boughtAt;
	}

	public int getInQuantity() {
		return inQuantity;
	}

	public void setInQuantity(int inQuantity) {
		this.inQuantity = inQuantity;
	}

}
