package ESMAssign;

import java.util.Date;


public class Sell extends Product {
	private Date soldOn;
	private double soldAt;
	private int beforeQuantity;
	private int outQuantity;
	private double netIncome; 

	public Sell() {
		super();
	}

	public Sell(Product product) {
		super();
		this.name = product.name;
		this.boughtAt = product.boughtAt;
		this.boughtOn = product.boughtOn;
		this.inQuantity = product.inQuantity;
		this.useBy = product.useBy;
	}

	public Date getSoldOn() {
		return soldOn;
	}

	public void setSoldOn(Date soldOn) {
		this.soldOn = soldOn;
	}

	public double getSoldAt() {
		return soldAt;
	}

	public void setSoldAt(double soldAt) {
		this.soldAt = soldAt;
	}

	public int getBeforeQuantity() {
		return beforeQuantity;
	}

	public void setBeforeQuantity(int beforeQuantity) {
		this.beforeQuantity = beforeQuantity;
	}
	
	
	public int getOutQuantity() {
		return outQuantity;
	}

	public void setOutQuantity(int outQuantity) {
		this.outQuantity = outQuantity;
	}
	
	public double getNetIncome() {
		return netIncome;
	}

	public void setNetIncome(double netIncome) {
		this.netIncome = netIncome;
	}
	
	
	
}
