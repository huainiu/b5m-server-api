package com.b5m.service.hbase.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class PricePerDay implements Serializable {
	private static final long serialVersionUID = 1L;
	private Date date;
	private BigDecimal price;

	public PricePerDay() {
	}

	public PricePerDay(Date date, String price) {
		super();
		this.date = date;
		this.price = new BigDecimal(price);
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

}