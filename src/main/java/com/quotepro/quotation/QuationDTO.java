package com.quotepro.quotation;

import java.util.List;

public class QuationDTO {
	private String customerid, customer_name, cmobilenumber, architectname;

	public List<Curtain> curtains;
	public List<Blind> blinds;
	public List<Mattress> mattresses;
	public List<Sofa> sofas;

	public static class Sofa {
		public String sofaid;
		public String name;
		public String total;
		public String sofaSize;
		public String designPattern;
		public String pricePerFoot;
		public String transportationFee;
		public String leatherMeter;
		public String leatherPrice;
		public String fabricMeter;
		public String fabricPrice;
		public String sizeInFeet;
		
		public List<SofaRPTData> reportsData;

		public static class SofaRPTData {
			public String DESCRIPTION;
			public String QTY;
			public String RATE;
			public String TOTAL;
			public String DISCOUNT;
			public String FINAL_TOTAL;
			public String TYPE;
			public String QTYPE;

			public String getDESCRIPTION() {
				return DESCRIPTION;
			}

			public void setDESCRIPTION(String dESCRIPTION) {
				DESCRIPTION = dESCRIPTION;
			}

			public String getQTY() {
				return QTY;
			}

			public void setQTY(String qTY) {
				QTY = qTY;
			}

			public String getRATE() {
				return RATE;
			}

			public void setRATE(String rATE) {
				RATE = rATE;
			}

			public String getTOTAL() {
				return TOTAL;
			}

			public void setTOTAL(String tOTAL) {
				TOTAL = tOTAL;
			}

			public String getDISCOUNT() {
				return DISCOUNT;
			}

			public void setDISCOUNT(String dISCOUNT) {
				DISCOUNT = dISCOUNT;
			}

			public String getFINAL_TOTAL() {
				return FINAL_TOTAL;
			}

			public void setFINAL_TOTAL(String fINAL_TOTAL) {
				FINAL_TOTAL = fINAL_TOTAL;
			}

			public String getTYPE() {
				return TYPE;
			}

			public void setTYPE(String tYPE) {
				TYPE = tYPE;
			}

			public String getQTYPE() {
				return QTYPE;
			}

			public void setQTYPE(String qTYPE) {
				QTYPE = qTYPE;
			}
		}

		public List<SofaRPTData> getReportsData() {
			return reportsData;
		}

		public void setReportsData(List<SofaRPTData> reportsData) {
			this.reportsData = reportsData;
		}

		public String getSofaid() {
			return sofaid;
		}

		public void setSofaid(String sofaid) {
			this.sofaid = sofaid;
		}

		public String getItemname() {
			return name;
		}

		public void setItemname(String name) {
			this.name = name;
		}

		public String getTotal() {
			return total;
		}

		public void setTotal(String total) {
			this.total = total;
		}

		public String getSofaSize() {
			return sofaSize;
		}

		public void setSofaSize(String sofaSize) {
			this.sofaSize = sofaSize;
		}

		public String getDesignPattern() {
			return designPattern;
		}

		public void setDesignPattern(String designPattern) {
			this.designPattern = designPattern;
		}

		public String getPricePerFoot() {
			return pricePerFoot;
		}

		public void setPricePerFoot(String pricePerFoot) {
			this.pricePerFoot = pricePerFoot;
		}

		public String getTransportationFee() {
			return transportationFee;
		}

		public void setTransportationFee(String transportationFee) {
			this.transportationFee = transportationFee;
		}

		public String getLeatherMeter() {
			return leatherMeter;
		}

		public void setLeatherMeter(String leatherMeter) {
			this.leatherMeter = leatherMeter;
		}

		public String getLeatherPrice() {
			return leatherPrice;
		}

		public void setLeatherPrice(String leatherPrice) {
			this.leatherPrice = leatherPrice;
		}

		public String getFabricMeter() {
			return fabricMeter;
		}

		public void setFabricMeter(String fabricMeter) {
			this.fabricMeter = fabricMeter;
		}

		public String getFabricPrice() {
			return fabricPrice;
		}

		public void setFabricPrice(String fabricPrice) {
			this.fabricPrice = fabricPrice;
		}

		public String getSizeInFeet() {
			return sizeInFeet;
		}

		public void setSizeInFeet(String sizeInFeet) {
			this.sizeInFeet = sizeInFeet;
		}

	}

	public static class Mattress {
		public String mattressid;
		public String name;
		public String total;
		public String company;
		public String width;
		public String height;
		public String displayHeight;
		public String pricePerUnit;
		public String transportationFee;
		public String discountPercentage;
		public String area;
		public String materialCost;
		public String discountAmount;
		public String discount;
		public String discountedMaterialCost;
		public String beforeDiscountPrice;
		public String afterDiscountPrice;
		public String product;
		public String size;
		
		

		public List<MattressRPTData> reportsData;

		public static class MattressRPTData {
			public String DESCRIPTION;
			public String QTY;
			public String RATE;
			public String TOTAL;
			public String DISCOUNT;
			public String FINAL_TOTAL;
			public String TYPE;
			public String QTYPE;

			public String getDESCRIPTION() {
				return DESCRIPTION;
			}

			public void setDESCRIPTION(String dESCRIPTION) {
				DESCRIPTION = dESCRIPTION;
			}

			public String getQTY() {
				return QTY;
			}

			public void setQTY(String qTY) {
				QTY = qTY;
			}

			public String getRATE() {
				return RATE;
			}

			public void setRATE(String rATE) {
				RATE = rATE;
			}

			public String getTOTAL() {
				return TOTAL;
			}

			public void setTOTAL(String tOTAL) {
				TOTAL = tOTAL;
			}

			public String getDISCOUNT() {
				return DISCOUNT;
			}

			public void setDISCOUNT(String dISCOUNT) {
				DISCOUNT = dISCOUNT;
			}

			public String getFINAL_TOTAL() {
				return FINAL_TOTAL;
			}

			public void setFINAL_TOTAL(String fINAL_TOTAL) {
				FINAL_TOTAL = fINAL_TOTAL;
			}

			public String getTYPE() {
				return TYPE;
			}

			public void setTYPE(String tYPE) {
				TYPE = tYPE;
			}

			public String getQTYPE() {
				return QTYPE;
			}

			public void setQTYPE(String qTYPE) {
				QTYPE = qTYPE;
			}
		}

		public List<MattressRPTData> getReportsData() {
			return reportsData;
		}

		public void setReportsData(List<MattressRPTData> reportsData) {
			this.reportsData = reportsData;
		}
		

		public String getProduct() {
			return product;
		}

		public void setProduct(String product) {
			this.product = product;
		}

		public String getSize() {
			return size;
		}

		public void setSize(String size) {
			this.size = size;
		}

		public String getBeforeDiscountPrice() {
			return beforeDiscountPrice;
		}

		public void setBeforeDiscountPrice(String beforeDiscountPrice) {
			this.beforeDiscountPrice = beforeDiscountPrice;
		}

		public String getAfterDiscountPrice() {
			return afterDiscountPrice;
		}

		public void setAfterDiscountPrice(String afterDiscountPrice) {
			this.afterDiscountPrice = afterDiscountPrice;
		}

		public String getMattressid() {
			return mattressid;
		}

		public void setMattressid(String mattressid) {
			this.mattressid = mattressid;
		}

		public String getItemname() {
			return name;
		}

		public void setItemname(String name) {
			this.name = name;
		}

		public String getTotal() {
			return total;
		}

		public void setTotal(String total) {
			this.total = total;
		}

		public String getCompany() {
			return company;
		}

		public void setCompany(String company) {
			this.company = company;
		}

		public String getWidth() {
			return width;
		}

		public void setWidth(String width) {
			this.width = width;
		}

		public String getHeight() {
			return height;
		}

		public void setHeight(String height) {
			this.height = height;
		}

		public String getDisplayHeight() {
			return displayHeight;
		}

		public void setDisplayHeight(String displayHeight) {
			this.displayHeight = displayHeight;
		}

		public String getPricePerUnit() {
			return pricePerUnit;
		}

		public void setPricePerUnit(String pricePerUnit) {
			this.pricePerUnit = pricePerUnit;
		}

		public String getTransportationFee() {
			return transportationFee;
		}

		public void setTransportationFee(String transportationFee) {
			this.transportationFee = transportationFee;
		}

		public String getDiscountPercentage() {
			return discountPercentage;
		}

		public void setDiscountPercentage(String discountPercentage) {
			this.discountPercentage = discountPercentage;
		}

		public String getArea() {
			return area;
		}

		public void setArea(String area) {
			this.area = area;
		}

		public String getMaterialCost() {
			return materialCost;
		}

		public void setMaterialCost(String materialCost) {
			this.materialCost = materialCost;
		}

		public String getDiscountAmount() {
			return discountAmount;
		}

		public void setDiscountAmount(String discountAmount) {
			this.discountAmount = discountAmount;
		}

		public String getDiscount() {
			return discount;
		}

		public void setDiscount(String discount) {
			this.discount = discount;
		}

		public String getDiscountedMaterialCost() {
			return discountedMaterialCost;
		}

		public void setDiscountedMaterialCost(String discountedMaterialCost) {
			this.discountedMaterialCost = discountedMaterialCost;
		}

	}

	public static class Blind {
		public String blindid;
		public String name;
		public String total;
		public String blindType;
		public String width;
		public String height;
		public String perSqFeet;
		public String perMeter;
		public String channelPerSqFeet;
		public String fittingCost;
		public String dimoutPerMeter;
		public String discountPercentage;
		public String discount;
		public String totalSqFeet;
		public String numberOfParts;
		public String totalMeters;
		public String channelSqFeet;
		public String channelCost;
		public String fabricCost;
		public String dimoutMeters;
		public String dimoutCost;
		public String totalCost;
		public String discountedTotal;
		public String beforeDiscountPrice;
		public String afterDiscountPrice;

		public List<BlindRPTData> reportsData;

		public static class BlindRPTData {
			public String DESCRIPTION;
			public String QTY;
			public String RATE;
			public String TOTAL;
			public String DISCOUNT;
			public String FINAL_TOTAL;
			public String TYPE;
			public String QTYPE;

			public String getDESCRIPTION() {
				return DESCRIPTION;
			}

			public void setDESCRIPTION(String dESCRIPTION) {
				DESCRIPTION = dESCRIPTION;
			}

			public String getQTY() {
				return QTY;
			}

			public void setQTY(String qTY) {
				QTY = qTY;
			}

			public String getRATE() {
				return RATE;
			}

			public void setRATE(String rATE) {
				RATE = rATE;
			}

			public String getTOTAL() {
				return TOTAL;
			}

			public void setTOTAL(String tOTAL) {
				TOTAL = tOTAL;
			}

			public String getDISCOUNT() {
				return DISCOUNT;
			}

			public void setDISCOUNT(String dISCOUNT) {
				DISCOUNT = dISCOUNT;
			}

			public String getFINAL_TOTAL() {
				return FINAL_TOTAL;
			}

			public void setFINAL_TOTAL(String fINAL_TOTAL) {
				FINAL_TOTAL = fINAL_TOTAL;
			}

			public String getTYPE() {
				return TYPE;
			}

			public void setTYPE(String tYPE) {
				TYPE = tYPE;
			}

			public String getQTYPE() {
				return QTYPE;
			}

			public void setQTYPE(String qTYPE) {
				QTYPE = qTYPE;
			}
		}

		public List<BlindRPTData> getReportsData() {
			return reportsData;
		}

		public void setReportsData(List<BlindRPTData> reportsData) {
			this.reportsData = reportsData;
		}

		public String getBeforeDiscountPrice() {
			return beforeDiscountPrice;
		}

		public void setBeforeDiscountPrice(String beforeDiscountPrice) {
			this.beforeDiscountPrice = beforeDiscountPrice;
		}

		public String getAfterDiscountPrice() {
			return afterDiscountPrice;
		}

		public void setAfterDiscountPrice(String afterDiscountPrice) {
			this.afterDiscountPrice = afterDiscountPrice;
		}

		public String getBlindid() {
			return blindid;
		}

		public void setBlindid(String blindid) {
			this.blindid = blindid;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getTotal() {
			return total;
		}

		public void setTotal(String total) {
			this.total = total;
		}

		public String getBlindType() {
			return blindType;
		}

		public void setBlindType(String blindType) {
			this.blindType = blindType;
		}

		public String getWidth() {
			return width;
		}

		public void setWidth(String width) {
			this.width = width;
		}

		public String getHeight() {
			return height;
		}

		public void setHeight(String height) {
			this.height = height;
		}

		public String getPerSqFeet() {
			return perSqFeet;
		}

		public void setPerSqFeet(String perSqFeet) {
			this.perSqFeet = perSqFeet;
		}

		public String getPerMeter() {
			return perMeter;
		}

		public void setPerMeter(String perMeter) {
			this.perMeter = perMeter;
		}

		public String getChannelPerSqFeet() {
			return channelPerSqFeet;
		}

		public void setChannelPerSqFeet(String channelPerSqFeet) {
			this.channelPerSqFeet = channelPerSqFeet;
		}

		public String getFittingCost() {
			return fittingCost;
		}

		public void setFittingCost(String fittingCost) {
			this.fittingCost = fittingCost;
		}

		public String getDimoutPerMeter() {
			return dimoutPerMeter;
		}

		public void setDimoutPerMeter(String dimoutPerMeter) {
			this.dimoutPerMeter = dimoutPerMeter;
		}

		public String getDiscountPercentage() {
			return discountPercentage;
		}

		public void setDiscountPercentage(String discountPercentage) {
			this.discountPercentage = discountPercentage;
		}

		public String getDiscount() {
			return discount;
		}

		public void setDiscount(String discount) {
			this.discount = discount;
		}

		public String getTotalSqFeet() {
			return totalSqFeet;
		}

		public void setTotalSqFeet(String totalSqFeet) {
			this.totalSqFeet = totalSqFeet;
		}

		public String getNumberOfParts() {
			return numberOfParts;
		}

		public void setNumberOfParts(String numberOfParts) {
			this.numberOfParts = numberOfParts;
		}

		public String getTotalMeters() {
			return totalMeters;
		}

		public void setTotalMeters(String totalMeters) {
			this.totalMeters = totalMeters;
		}

		public String getChannelSqFeet() {
			return channelSqFeet;
		}

		public void setChannelSqFeet(String channelSqFeet) {
			this.channelSqFeet = channelSqFeet;
		}

		public String getChannelCost() {
			return channelCost;
		}

		public void setChannelCost(String channelCost) {
			this.channelCost = channelCost;
		}

		public String getFabricCost() {
			return fabricCost;
		}

		public void setFabricCost(String fabricCost) {
			this.fabricCost = fabricCost;
		}

		public String getDimoutMeters() {
			return dimoutMeters;
		}

		public void setDimoutMeters(String dimoutMeters) {
			this.dimoutMeters = dimoutMeters;
		}

		public String getDimoutCost() {
			return dimoutCost;
		}

		public void setDimoutCost(String dimoutCost) {
			this.dimoutCost = dimoutCost;
		}

		public String getTotalCost() {
			return totalCost;
		}

		public void setTotalCost(String totalCost) {
			this.totalCost = totalCost;
		}

		public String getDiscountedTotal() {
			return discountedTotal;
		}

		public void setDiscountedTotal(String discountedTotal) {
			this.discountedTotal = discountedTotal;
		}

	}

	public static class Curtain {
		public String curtainid;
		public String name;
		public String total;
		public String width;
		public String height;
		public String perMeter;
		public String labor;
		public String channelPerFeet;
		public String dimoutPerMeter;
		public String sheerPerMeter;
		public String sheerHeight;
		public String sheerDiscountPercentage;
		public String panelMeters;
		public String panelPerMeter;
		public String channelType;
		public String motorPrice;
		public String remotePrice;
		public String fittingCost;
		public String materialDiscountPercentage;
		public String weightDoriPerMeter;
		public String discount;
		public String beforeDiscountPrice;
		public String afterDiscountPrice;
		public String sheerWidth;
		public String labor2;
		public String channelPerFeet2;
		public String channelType2;
		public String motorPrice2;
		public String remotePrice2;
		public String fittingCost2;
		public List<CurtainRPTData> reportsData;

		public static class CurtainRPTData {
			public String DESCRIPTION;
			public String QTY;
			public String RATE;
			public String TOTAL;
			public String DISCOUNT;
			public String FINAL_TOTAL;
			public String TYPE;
			public String QTYPE;

			public String getDESCRIPTION() {
				return DESCRIPTION;
			}

			public void setDESCRIPTION(String dESCRIPTION) {
				DESCRIPTION = dESCRIPTION;
			}

			public String getQTY() {
				return QTY;
			}

			public void setQTY(String qTY) {
				QTY = qTY;
			}

			public String getRATE() {
				return RATE;
			}

			public void setRATE(String rATE) {
				RATE = rATE;
			}

			public String getTOTAL() {
				return TOTAL;
			}

			public void setTOTAL(String tOTAL) {
				TOTAL = tOTAL;
			}

			public String getDISCOUNT() {
				return DISCOUNT;
			}

			public void setDISCOUNT(String dISCOUNT) {
				DISCOUNT = dISCOUNT;
			}

			public String getFINAL_TOTAL() {
				return FINAL_TOTAL;
			}

			public void setFINAL_TOTAL(String fINAL_TOTAL) {
				FINAL_TOTAL = fINAL_TOTAL;
			}

			public String getTYPE() {
				return TYPE;
			}

			public void setTYPE(String tYPE) {
				TYPE = tYPE;
			}

			public String getQTYPE() {
				return QTYPE;
			}

			public void setQTYPE(String qTYPE) {
				QTYPE = qTYPE;
			}
		}

		public List<CurtainRPTData> getReportsData() {
			return reportsData;
		}

		public void setReportsData(List<CurtainRPTData> reportsData) {
			this.reportsData = reportsData;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getSheerWidth() {
			return sheerWidth;
		}

		public void setSheerWidth(String sheerWidth) {
			this.sheerWidth = sheerWidth;
		}

		public String getLabor2() {
			return labor2;
		}

		public void setLabor2(String labor2) {
			this.labor2 = labor2;
		}

		public String getChannelPerFeet2() {
			return channelPerFeet2;
		}

		public void setChannelPerFeet2(String channelPerFeet2) {
			this.channelPerFeet2 = channelPerFeet2;
		}

		public String getChannelType2() {
			return channelType2;
		}

		public void setChannelType2(String channelType2) {
			this.channelType2 = channelType2;
		}

		public String getMotorPrice2() {
			return motorPrice2;
		}

		public void setMotorPrice2(String motorPrice2) {
			this.motorPrice2 = motorPrice2;
		}

		public String getRemotePrice2() {
			return remotePrice2;
		}

		public void setRemotePrice2(String remotePrice2) {
			this.remotePrice2 = remotePrice2;
		}

		public String getFittingCost2() {
			return fittingCost2;
		}

		public void setFittingCost2(String fittingCost2) {
			this.fittingCost2 = fittingCost2;
		}

		public String getBeforeDiscountPrice() {
			return beforeDiscountPrice;
		}

		public void setBeforeDiscountPrice(String beforeDiscountPrice) {
			this.beforeDiscountPrice = beforeDiscountPrice;
		}

		public String getAfterDiscountPrice() {
			return afterDiscountPrice;
		}

		public void setAfterDiscountPrice(String afterDiscountPrice) {
			this.afterDiscountPrice = afterDiscountPrice;
		}

		public String getCurtainid() {
			return curtainid;
		}

		public void setCurtainid(String curtainid) {
			this.curtainid = curtainid;
		}

		public String getItemname() {
			return name;
		}

		public void setItemname(String name) {
			this.name = name;
		}

		public String getTotal() {
			return total;
		}

		public void setTotal(String total) {
			this.total = total;
		}

		public String getWidth() {
			return width;
		}

		public void setWidth(String width) {
			this.width = width;
		}

		public String getHeight() {
			return height;
		}

		public void setHeight(String height) {
			this.height = height;
		}

		public String getPerMeter() {
			return perMeter;
		}

		public void setPerMeter(String perMeter) {
			this.perMeter = perMeter;
		}

		public String getLabor() {
			return labor;
		}

		public void setLabor(String labor) {
			this.labor = labor;
		}

		public String getChannelPerFeet() {
			return channelPerFeet;
		}

		public void setChannelPerFeet(String channelPerFeet) {
			this.channelPerFeet = channelPerFeet;
		}

		public String getDimoutPerMeter() {
			return dimoutPerMeter;
		}

		public void setDimoutPerMeter(String dimoutPerMeter) {
			this.dimoutPerMeter = dimoutPerMeter;
		}

		public String getSheerPerMeter() {
			return sheerPerMeter;
		}

		public void setSheerPerMeter(String sheerPerMeter) {
			this.sheerPerMeter = sheerPerMeter;
		}

		public String getSheerHeight() {
			return sheerHeight;
		}

		public void setSheerHeight(String sheerHeight) {
			this.sheerHeight = sheerHeight;
		}

		public String getSheerDiscountPercentage() {
			return sheerDiscountPercentage;
		}

		public void setSheerDiscountPercentage(String sheerDiscountPercentage) {
			this.sheerDiscountPercentage = sheerDiscountPercentage;
		}

		public String getPanelMeters() {
			return panelMeters;
		}

		public void setPanelMeters(String panelMeters) {
			this.panelMeters = panelMeters;
		}

		public String getPanelPerMeter() {
			return panelPerMeter;
		}

		public void setPanelPerMeter(String panelPerMeter) {
			this.panelPerMeter = panelPerMeter;
		}

		public String getChannelType() {
			return channelType;
		}

		public void setChannelType(String channelType) {
			this.channelType = channelType;
		}

		public String getMotorPrice() {
			return motorPrice;
		}

		public void setMotorPrice(String motorPrice) {
			this.motorPrice = motorPrice;
		}

		public String getRemotePrice() {
			return remotePrice;
		}

		public void setRemotePrice(String remotePrice) {
			this.remotePrice = remotePrice;
		}

		public String getFittingCost() {
			return fittingCost;
		}

		public void setFittingCost(String fittingCost) {
			this.fittingCost = fittingCost;
		}

		public String getMaterialDiscountPercentage() {
			return materialDiscountPercentage;
		}

		public void setMaterialDiscountPercentage(String materialDiscountPercentage) {
			this.materialDiscountPercentage = materialDiscountPercentage;
		}

		public String getWeightDoriPerMeter() {
			return weightDoriPerMeter;
		}

		public void setWeightDoriPerMeter(String weightDoriPerMeter) {
			this.weightDoriPerMeter = weightDoriPerMeter;
		}

		public String getDiscount() {
			return discount;
		}

		public void setDiscount(String discount) {
			this.discount = discount;
		}

	}

	public String getCustomerid() {
		return customerid;
	}

	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}

	public String getCmobilenumber() {
		return cmobilenumber;
	}

	public void setCmobilenumber(String cmobilenumber) {
		this.cmobilenumber = cmobilenumber;
	}

	public String getArchitectname() {
		return architectname;
	}

	public void setArchitectname(String architectname) {
		this.architectname = architectname;
	}

	public List<Curtain> getCurtains() {
		return curtains;
	}

	public void setCurtains(List<Curtain> curtains) {
		this.curtains = curtains;
	}

	public String getCustomer_name() {
		return customer_name;
	}

	public void setCustomer_name(String customer_name) {
		this.customer_name = customer_name;
	}

	public List<Blind> getBlinds() {
		return blinds;
	}

	public void setBlinds(List<Blind> blinds) {
		this.blinds = blinds;
	}

	public List<Mattress> getMattresses() {
		return mattresses;
	}

	public void setMattresses(List<Mattress> mattresses) {
		this.mattresses = mattresses;
	}

	public List<Sofa> getSofas() {
		return sofas;
	}

	public void setSofas(List<Sofa> sofas) {
		this.sofas = sofas;
	}

}
