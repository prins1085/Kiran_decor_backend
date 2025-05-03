package com.quotepro.quotation;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.quotepro.common.enumeration.CommonEnum;
import com.quotepro.common.enumeration.ToastrAction;
import com.quotepro.common.utils.Common;
import com.quotepro.common.utils.DBUtils;
import com.quotepro.common.utils.DbModel;
import com.quotepro.common.utils.ListToJSONConverter;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceException;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class QuationServiceImpl implements QuationSerive {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private EntityManagerFactory emf;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private DbModel dbModel;

	@Autowired
	DBUtils dbUtils;

	@Autowired
	ListToJSONConverter listToJSONConverter;

	// ============================= Grid ======================== //
	@Override
	public ResponseEntity<Object> Grid() {
		EntityManager manager = null;
		try {
			manager = emf.createEntityManager();
			String query = " select c.customer_id, c.customer_name, c.mobile_number, c.architect_name, coalesce(curt.total_curtain, 0) as total_curtain, "
					+ " coalesce(b.total_blind, 0) as total_blind, coalesce(matt.total_mattress, 0) as total_mattress, coalesce(s.total_sofa, 0) as total_sofa, "
					+ " coalesce(curt.total_curtain, 0) + coalesce(b.total_blind, 0) + coalesce(matt.total_mattress, 0) + coalesce(s.total_sofa, 0) as grand_total "
					+ " from customers c left join ( select customer_id, SUM(total) as total_curtain from curtain group by customer_id) curt on c.customer_id = curt.customer_id "
					+ " left join ( select customer_id, SUM(total) as total_blind from blind group by customer_id) b on c.customer_id = b.customer_id "
					+ " left join ( select customer_id, SUM(total) as total_mattress from mattress group by customer_id) matt on c.customer_id = matt.customer_id "
					+ " left join ( select customer_id, SUM(total) as total_sofa from sofa group by customer_id) s on c.customer_id = s.customer_id ";
			List<?> gridlist = dbUtils.getListKeyValuePairAndList(query, manager, true);
			return ResponseEntity.ok(gridlist);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonEnum.ERROR_OCCURED_WHILE_RETRIWING_DATA.getValue(),
					HttpStatus.BAD_REQUEST);
		} finally {
			if (manager != null) {
				manager.clear();
				manager.close();
			}
		}
	}

	@Override
	public <T> Object save(QuationDTO dto) {
		EntityManager manager = null;
		EntityTransaction transaction = null;
		HashMap<String, Object> responMap = new HashMap<String, Object>();
		boolean flag = true;
		boolean weftFlag = true;
		try {

			String customerid = Common.checkNullAndEmpty(dto.getCustomerid()) ? dto.getCustomerid() : null;
			manager = emf.createEntityManager();
			String desc = CommonEnum.SAVE_SUCCESS.getValue();

			if (manager.isOpen() && manager != null) {
				transaction = manager.getTransaction();
				if (transaction != null) {
					Integer curtainid = Integer.parseInt(dbUtils.getMaxId("curtain", "curtainid", null, manager));
					Integer blindid = Integer.parseInt(dbUtils.getMaxId("blind", "blindid", null, manager));
					Integer mattressid = Integer.parseInt(dbUtils.getMaxId("mattress", "mattressid", null, manager));
					Integer sofaid = Integer.parseInt(dbUtils.getMaxId("sofa", "sofaid", null, manager));

					transaction.begin();
					if (Common.checkNullAndEmpty(customerid)) {/* Edit */
						desc = CommonEnum.UPDATE_SUCCESS.getValue();

						String updateQuery = "UPDATE customers SET CUSTOMER_NAME=:CUSTOMER_NAME,MOBILE_NUMBER=:MOBILE_NUMBER,ARCHITECT_NAME=:ARCHITECT_NAME  WHERE CUSTOMER_ID = "
								+ customerid;
						manager.createNativeQuery(updateQuery).setParameter("CUSTOMER_NAME", dto.getCustomer_name())

								.setParameter("MOBILE_NUMBER",
										Common.checkNullAndEmpty(dto.getCmobilenumber()) ? dto.getCmobilenumber()
												: null)
								.setParameter("ARCHITECT_NAME",
										Common.checkNullAndEmpty(dto.getArchitectname()) ? dto.getArchitectname()
												: null)
								.executeUpdate();

						String deleteCurtainQuery = "DELETE FROM curtain WHERE CUSTOMER_ID = " + customerid;
						String deleteBlindQuery = "DELETE FROM blind WHERE CUSTOMER_ID =" + customerid;
						String deleteMattressQuery = "DELETE FROM mattress WHERE CUSTOMER_ID =" + customerid;
						String deleteSofaQuery = "DELETE FROM sofa WHERE CUSTOMER_ID =" + customerid;

						manager.createNativeQuery(deleteCurtainQuery).executeUpdate();
						manager.createNativeQuery(deleteBlindQuery).executeUpdate();
						manager.createNativeQuery(deleteMattressQuery).executeUpdate();
						manager.createNativeQuery(deleteSofaQuery).executeUpdate();

					} else { /* Insert */
						customerid = dbUtils.getMaxId("customers", "customer_id", null, manager);

						String insertquery = "INSERT INTO customers(customer_id,customer_name,mobile_number,architect_name) "
								+ "VALUES(:customer_id,:customer_name,:mobile_number,:architect_name)";
						manager.createNativeQuery(insertquery).setParameter("customer_id", Integer.parseInt(customerid))
								.setParameter("customer_name", dto.getCustomer_name())
								.setParameter("mobile_number", dto.getCmobilenumber())
								.setParameter("architect_name", dto.getArchitectname()).executeUpdate();
					}

					// ========================== Insert Curtain ===================//
					List<QuationDTO.Curtain> curtList = dto.getCurtains();
					if (curtList.size() > 0) {
						String itemname, total, width, height, perMeter, labor, channelPerFeet, dimoutPerMeter,
								sheerPerMeter, sheerHeight, sheerDiscountPercentage, panelMeters, panelPerMeter,
								channelType, motorPrice, remotePrice, fittingCost, materialDiscountPercentage,
								weightDoriPerMeter, discount, beforeDiscountPrice, afterDiscountPrice, sheerWidth,
								labor2, channelPerFeet2, channelType2, motorPrice2, remotePrice2, fittingCost2;

						StringBuilder insertString = new StringBuilder();
						StringBuilder values = new StringBuilder();
						insertString.append(
								"INSERT INTO curtain (curtainid, customer_id,  itemname, total, width, height, perMeter , labor, channelPerFeet, dimoutPerMeter, sheerPerMeter, sheerHeight, sheerDiscountPercentage, panelMeters, panelPerMeter, channelType, motorPrice, remotePrice, fittingCost, materialDiscountPercentage, weightDoriPerMeter, discount,beforeDiscountPrice,afterDiscountPrice,sheerWidth,labor2,channelPerFeet2,channelType2,motorPrice2,remotePrice2,fittingCost2 ) VALUES ");
						for (int j = 0; j < curtList.size(); j++) {
							itemname = total = width = height = perMeter = labor = channelPerFeet = dimoutPerMeter = sheerPerMeter = sheerHeight = sheerDiscountPercentage = panelMeters = panelPerMeter = channelType = motorPrice = remotePrice = fittingCost = materialDiscountPercentage = weightDoriPerMeter = discount = beforeDiscountPrice = afterDiscountPrice = sheerWidth = labor2 = channelPerFeet2 = channelType2 = motorPrice2 = remotePrice2 = fittingCost2 = null;
							values.append(values.length() == 0 ? "" : ",");
							itemname = Common.checkNullAndEmpty(curtList.get(j).getItemname())
									? curtList.get(j).getItemname()
									: null;
							height = Common.checkNullAndEmpty(curtList.get(j).getHeight()) ? curtList.get(j).getHeight()
									: null;
							total = Common.checkNullAndEmpty(curtList.get(j).getTotal()) ? curtList.get(j).getTotal()
									: null;
							perMeter = Common.checkNullAndEmpty(curtList.get(j).getPerMeter())
									? curtList.get(j).getPerMeter()
									: null;
							width = Common.checkNullAndEmpty(curtList.get(j).getWidth()) ? curtList.get(j).getWidth()
									: null;

							labor = Common.checkNullAndEmpty(curtList.get(j).getLabor()) ? curtList.get(j).getLabor()
									: null;
							channelPerFeet = Common.checkNullAndEmpty(curtList.get(j).getChannelPerFeet())
									? curtList.get(j).getChannelPerFeet()
									: null;
							dimoutPerMeter = Common.checkNullAndEmpty(curtList.get(j).getDimoutPerMeter())
									? curtList.get(j).getDimoutPerMeter()
									: null;
							sheerPerMeter = Common.checkNullAndEmpty(curtList.get(j).getSheerPerMeter())
									? curtList.get(j).getSheerPerMeter()
									: null;
							sheerHeight = Common.checkNullAndEmpty(curtList.get(j).getSheerHeight())
									? curtList.get(j).getSheerHeight()
									: null;
							sheerDiscountPercentage = Common
									.checkNullAndEmpty(curtList.get(j).getSheerDiscountPercentage())
											? curtList.get(j).getSheerDiscountPercentage()
											: null;
							panelMeters = Common.checkNullAndEmpty(curtList.get(j).getPanelMeters())
									? curtList.get(j).getPanelMeters()
									: null;
							panelPerMeter = Common.checkNullAndEmpty(curtList.get(j).getPanelPerMeter())
									? curtList.get(j).getPanelPerMeter()
									: null;
							channelType = Common.checkNullAndEmpty(curtList.get(j).getChannelType())
									? curtList.get(j).getChannelType()
									: null;
							motorPrice = Common.checkNullAndEmpty(curtList.get(j).getMotorPrice())
									? curtList.get(j).getMotorPrice()
									: null;
							remotePrice = Common.checkNullAndEmpty(curtList.get(j).getRemotePrice())
									? curtList.get(j).getRemotePrice()
									: null;
							fittingCost = Common.checkNullAndEmpty(curtList.get(j).getFittingCost())
									? curtList.get(j).getFittingCost()
									: null;
							materialDiscountPercentage = Common
									.checkNullAndEmpty(curtList.get(j).getMaterialDiscountPercentage())
											? curtList.get(j).getMaterialDiscountPercentage()
											: null;
							weightDoriPerMeter = Common.checkNullAndEmpty(curtList.get(j).getWeightDoriPerMeter())
									? curtList.get(j).getWeightDoriPerMeter()
									: null;
							discount = Common.checkNullAndEmpty(curtList.get(j).getDiscount())
									? curtList.get(j).getDiscount()
									: null;
							afterDiscountPrice = Common.checkNullAndEmpty(curtList.get(j).getAfterDiscountPrice())
									? curtList.get(j).getAfterDiscountPrice()
									: null;
							beforeDiscountPrice = Common.checkNullAndEmpty(curtList.get(j).getBeforeDiscountPrice())
									? curtList.get(j).getBeforeDiscountPrice()
									: null;

							sheerWidth = Common.checkNullAndEmpty(curtList.get(j).getSheerWidth())
									? curtList.get(j).getSheerWidth()
									: null;
							labor2 = Common.checkNullAndEmpty(curtList.get(j).getLabor2()) ? curtList.get(j).getLabor2()
									: null;

							channelPerFeet2 = Common.checkNullAndEmpty(curtList.get(j).getChannelPerFeet2())
									? curtList.get(j).getChannelPerFeet2()
									: null;

							channelType2 = Common.checkNullAndEmpty(curtList.get(j).getChannelType2())
									? curtList.get(j).getChannelType2()
									: null;

							motorPrice2 = Common.checkNullAndEmpty(curtList.get(j).getMotorPrice2())
									? curtList.get(j).getMotorPrice2()
									: null;

							remotePrice2 = Common.checkNullAndEmpty(curtList.get(j).getRemotePrice2())
									? curtList.get(j).getRemotePrice2()
									: null;

							fittingCost2 = Common.checkNullAndEmpty(curtList.get(j).getFittingCost2())
									? curtList.get(j).getFittingCost2()
									: null;

							values.append("(" + curtainid + ", " + customerid + ", '" + itemname + "', " + total + ", "
									+ width + ", " + height + ", " + perMeter + ", " + labor + " , " + channelPerFeet
									+ ", " + dimoutPerMeter + ", " + sheerPerMeter + ", " + sheerHeight + ", "
									+ sheerDiscountPercentage + ", " + panelMeters + ", " + panelPerMeter + ", '"
									+ channelType + "', " + motorPrice + ", " + remotePrice + ", " + fittingCost + ", "
									+ materialDiscountPercentage + ", " + weightDoriPerMeter + ", " + discount + " , "
									+ beforeDiscountPrice + "," + afterDiscountPrice + " , " + sheerWidth + "," + labor2
									+ "," + channelPerFeet2 + "," + channelType2 + "," + motorPrice2 + ","
									+ remotePrice2 + "," + fittingCost2 + ")");

							curtainid++;

							if (itemname == null && height == null && total == null && perMeter == null) {
								flag = false;
							}
						}

						if (flag) {
							insertString.append(values);
							manager.createNativeQuery(insertString.toString()).executeUpdate();
						}
					}

					// ========================== Insert Blind ===================//
					List<QuationDTO.Blind> blindList = dto.getBlinds();
					if (blindList.size() > 0) {
						String itemname, total, blind_type, width, height, per_sq_feet, per_meter, channel_per_sq_feet,
								fitting_cost, dimout_per_meter, discount_percentage, discount, total_sq_feet,
								number_of_parts, total_meters, channel_sq_feet, channel_cost, fabric_cost,
								dimout_meters, dimout_cost, total_cost, discounted_total, beforeDiscountPrice,
								afterDiscountPrice;

						StringBuilder insertString = new StringBuilder();
						StringBuilder values = new StringBuilder();
						insertString.append(
								"INSERT INTO blind (blindid, itemname, total, blind_type, width, height, per_sq_feet, per_meter, channel_per_sq_feet, fitting_cost, dimout_per_meter, discount_percentage, discount, total_sq_feet, number_of_parts, total_meters, channel_sq_feet, channel_cost, fabric_cost, dimout_meters, dimout_cost, total_cost, discounted_total, customer_id ,beforeDiscountPrice,afterDiscountPrice) VALUES ");
						for (int j = 0; j < blindList.size(); j++) {
							itemname = total = blind_type = width = height = per_sq_feet = per_meter = channel_per_sq_feet = fitting_cost = dimout_per_meter = discount_percentage = discount = total_sq_feet = number_of_parts = total_meters = channel_sq_feet = channel_cost = fabric_cost = dimout_meters = dimout_cost = total_cost = discounted_total = beforeDiscountPrice = afterDiscountPrice = null;

							values.append(values.length() == 0 ? "" : ",");
							itemname = Common.checkNullAndEmpty(blindList.get(j).getName()) ? blindList.get(j).getName()
									: null;
							total = Common.checkNullAndEmpty(blindList.get(j).getTotal()) ? blindList.get(j).getTotal()
									: null;
							blind_type = Common.checkNullAndEmpty(blindList.get(j).getBlindType())
									? blindList.get(j).getBlindType()
									: null;
							width = Common.checkNullAndEmpty(blindList.get(j).getWidth()) ? blindList.get(j).getWidth()
									: null;
							height = Common.checkNullAndEmpty(blindList.get(j).getHeight())
									? blindList.get(j).getHeight()
									: null;
							per_sq_feet = Common.checkNullAndEmpty(blindList.get(j).getPerSqFeet())
									? blindList.get(j).getPerSqFeet()
									: null;
							per_meter = Common.checkNullAndEmpty(blindList.get(j).getPerMeter())
									? blindList.get(j).getPerMeter()
									: null;
							channel_per_sq_feet = Common.checkNullAndEmpty(blindList.get(j).getChannelPerSqFeet())
									? blindList.get(j).getChannelPerSqFeet()
									: null;
							fitting_cost = Common.checkNullAndEmpty(blindList.get(j).getFittingCost())
									? blindList.get(j).getFittingCost()
									: null;
							dimout_per_meter = Common.checkNullAndEmpty(blindList.get(j).getDimoutPerMeter())
									? blindList.get(j).getDimoutPerMeter()
									: null;
							discount_percentage = Common.checkNullAndEmpty(blindList.get(j).getDiscountPercentage())
									? blindList.get(j).getDiscountPercentage()
									: null;
							discount = Common.checkNullAndEmpty(blindList.get(j).getDiscount())
									? blindList.get(j).getDiscount()
									: null;
							total_sq_feet = Common.checkNullAndEmpty(blindList.get(j).getTotalSqFeet())
									? blindList.get(j).getTotalSqFeet()
									: null;
							number_of_parts = Common.checkNullAndEmpty(blindList.get(j).getNumberOfParts())
									? blindList.get(j).getNumberOfParts()
									: null;
							total_meters = Common.checkNullAndEmpty(blindList.get(j).getTotalMeters())
									? blindList.get(j).getTotalMeters()
									: null;
							channel_sq_feet = Common.checkNullAndEmpty(blindList.get(j).getChannelSqFeet())
									? blindList.get(j).getChannelSqFeet()
									: null;
							channel_cost = Common.checkNullAndEmpty(blindList.get(j).getChannelCost())
									? blindList.get(j).getChannelCost()
									: null;
							fabric_cost = Common.checkNullAndEmpty(blindList.get(j).getFabricCost())
									? blindList.get(j).getFabricCost()
									: null;
							dimout_meters = Common.checkNullAndEmpty(blindList.get(j).getDimoutMeters())
									? blindList.get(j).getDimoutMeters()
									: null;
							dimout_cost = Common.checkNullAndEmpty(blindList.get(j).getDimoutCost())
									? blindList.get(j).getDimoutCost()
									: null;
							total_cost = Common.checkNullAndEmpty(blindList.get(j).getTotalCost())
									? blindList.get(j).getTotalCost()
									: null;
							discounted_total = Common.checkNullAndEmpty(blindList.get(j).getDiscountedTotal())
									? blindList.get(j).getDiscountedTotal()
									: null;

							afterDiscountPrice = Common.checkNullAndEmpty(curtList.get(j).getAfterDiscountPrice())
									? curtList.get(j).getAfterDiscountPrice()
									: null;
							beforeDiscountPrice = Common.checkNullAndEmpty(curtList.get(j).getBeforeDiscountPrice())
									? curtList.get(j).getBeforeDiscountPrice()
									: null;

							values.append("(" + blindid + ", '" + itemname + "' , " + total + ", '" + blind_type + "', "
									+ width + ", " + height + ", " + per_sq_feet + ", " + per_meter + ", "
									+ channel_per_sq_feet + ", " + fitting_cost + ", " + dimout_per_meter + ", "
									+ discount_percentage + ", " + discount + ", " + total_sq_feet + ", "
									+ number_of_parts + ", " + total_meters + ", " + channel_sq_feet + ", "
									+ channel_cost + ", " + fabric_cost + ", " + dimout_meters + ", " + dimout_cost
									+ ", " + total_cost + ", " + discounted_total + ", " + customerid + " , "
									+ beforeDiscountPrice + "," + afterDiscountPrice + ")");

							blindid++;

							if (itemname == null && height == null && total == null) {
								flag = false;
							}
						}

						if (flag) {
							insertString.append(values);
							manager.createNativeQuery(insertString.toString()).executeUpdate();
						}
					}

					// ========================== Insert Mattress ===================//
					List<QuationDTO.Mattress> mattList = dto.getMattresses();
					if (mattList.size() > 0) {

						String itemname, total, company, width, height, displayHeight, pricePerUnit, transportationFee,
								discountPercentage, area, materialCost, discountAmount, discount,
								discountedMaterialCost, beforeDiscountPrice, afterDiscountPrice;

						StringBuilder insertString = new StringBuilder();
						StringBuilder values = new StringBuilder();
						insertString.append(
								"INSERT INTO mattress (mattressid, itemname, total, company, width, height, displayHeight, pricePerUnit, transportationFee, discountPercentage, area, materialCost, discountAmount, discount, discountedMaterialCost, customer_id , beforeDiscountPrice,afterDiscountPrice ) VALUES ");
						for (int j = 0; j < mattList.size(); j++) {
							itemname = total = company = width = height = displayHeight = pricePerUnit = transportationFee = discountPercentage = area = materialCost = discountAmount = discount = discountedMaterialCost = beforeDiscountPrice = afterDiscountPrice = null;

							values.append(values.length() == 0 ? "" : ",");
							itemname = Common.checkNullAndEmpty(mattList.get(j).getItemname())
									? mattList.get(j).getItemname()
									: null;
							total = Common.checkNullAndEmpty(mattList.get(j).getTotal()) ? mattList.get(j).getTotal()
									: null;
							company = Common.checkNullAndEmpty(mattList.get(j).getCompany())
									? mattList.get(j).getCompany()
									: null;
							width = Common.checkNullAndEmpty(mattList.get(j).getWidth()) ? mattList.get(j).getWidth()
									: null;
							height = Common.checkNullAndEmpty(mattList.get(j).getHeight()) ? mattList.get(j).getHeight()
									: null;
							displayHeight = Common.checkNullAndEmpty(mattList.get(j).getDisplayHeight())
									? mattList.get(j).getDisplayHeight()
									: null;
							pricePerUnit = Common.checkNullAndEmpty(mattList.get(j).getPricePerUnit())
									? mattList.get(j).getPricePerUnit()
									: null;
							transportationFee = Common.checkNullAndEmpty(mattList.get(j).getTransportationFee())
									? mattList.get(j).getTransportationFee()
									: null;
							discountPercentage = Common.checkNullAndEmpty(mattList.get(j).getDiscountPercentage())
									? mattList.get(j).getDiscountPercentage()
									: null;
							area = Common.checkNullAndEmpty(mattList.get(j).getArea()) ? mattList.get(j).getArea()
									: null;
							materialCost = Common.checkNullAndEmpty(mattList.get(j).getMaterialCost())
									? mattList.get(j).getMaterialCost()
									: null;
							discountAmount = Common.checkNullAndEmpty(mattList.get(j).getDiscountAmount())
									? mattList.get(j).getDiscountAmount()
									: null;
							discount = Common.checkNullAndEmpty(mattList.get(j).getDiscount())
									? mattList.get(j).getDiscount()
									: null;
							discountedMaterialCost = Common
									.checkNullAndEmpty(mattList.get(j).getDiscountedMaterialCost())
											? mattList.get(j).getDiscountedMaterialCost()
											: null;
							afterDiscountPrice = Common.checkNullAndEmpty(curtList.get(j).getAfterDiscountPrice())
									? curtList.get(j).getAfterDiscountPrice()
									: null;
							beforeDiscountPrice = Common.checkNullAndEmpty(curtList.get(j).getBeforeDiscountPrice())
									? curtList.get(j).getBeforeDiscountPrice()
									: null;

							values.append("(").append(mattressid).append(", '").append(itemname).append("', '")
									.append(total).append("', '").append(company).append("', ").append(width)
									.append(", ").append(height).append(", ").append(displayHeight).append(", ")
									.append(pricePerUnit).append(", ").append(transportationFee).append(", ")
									.append(discountPercentage).append(", ").append(area).append(", ")
									.append(materialCost).append(", ").append(discountAmount).append(", ")
									.append(discount).append(", ").append(discountedMaterialCost).append(", ")
									.append(customerid).append(", ").append(beforeDiscountPrice).append(", ")
									.append(afterDiscountPrice).append(")");

							mattressid++;

							if (itemname == null && height == null && total == null) {
								flag = false;
							}
						}

						if (flag) {
							insertString.append(values);
							manager.createNativeQuery(insertString.toString()).executeUpdate();
						}
					}

					// ========================== Insert Sofa ===================//
					List<QuationDTO.Sofa> sofList = dto.getSofas();
					if (sofList.size() > 0) {

						String itemname, total, sofaSize, designPattern, pricePerFoot, transportationFee, leatherMeter,
								leatherPrice, fabricMeter, fabricPrice, sizeInFeet;

						StringBuilder insertString = new StringBuilder();
						StringBuilder values = new StringBuilder();
						insertString.append(
								"INSERT INTO sofa (sofaid, itemname, total, sofaSize, designPattern, pricePerFoot, transportationFee, leatherMeter, leatherPrice, fabricMeter, fabricPrice, sizeInFeet, customer_id) VALUES ");

						for (int j = 0; j < sofList.size(); j++) {
							itemname = total = sofaSize = designPattern = pricePerFoot = transportationFee = leatherMeter = leatherPrice = fabricMeter = fabricPrice = sizeInFeet = null;
							values.append(values.length() == 0 ? "" : ",");

							itemname = Common.checkNullAndEmpty(sofList.get(j).getItemname())
									? sofList.get(j).getItemname()
									: "NULL";
							total = Common.checkNullAndEmpty(sofList.get(j).getTotal()) ? sofList.get(j).getTotal()
									: "NULL";
							sofaSize = Common.checkNullAndEmpty(sofList.get(j).getSofaSize())
									? sofList.get(j).getSofaSize()
									: "NULL";
							designPattern = Common.checkNullAndEmpty(sofList.get(j).getDesignPattern())
									? sofList.get(j).getDesignPattern()
									: "NULL";
							pricePerFoot = Common.checkNullAndEmpty(sofList.get(j).getPricePerFoot())
									? sofList.get(j).getPricePerFoot()
									: "NULL";
							transportationFee = Common.checkNullAndEmpty(sofList.get(j).getTransportationFee())
									? sofList.get(j).getTransportationFee()
									: "NULL";
							leatherMeter = Common.checkNullAndEmpty(sofList.get(j).getLeatherMeter())
									? sofList.get(j).getLeatherMeter()
									: "NULL";
							leatherPrice = Common.checkNullAndEmpty(sofList.get(j).getLeatherPrice())
									? sofList.get(j).getLeatherPrice()
									: "NULL";
							fabricMeter = Common.checkNullAndEmpty(sofList.get(j).getFabricMeter())
									? sofList.get(j).getFabricMeter()
									: "NULL";
							fabricPrice = Common.checkNullAndEmpty(sofList.get(j).getFabricPrice())
									? sofList.get(j).getFabricPrice()
									: "NULL";
							sizeInFeet = Common.checkNullAndEmpty(sofList.get(j).getSizeInFeet())
									? sofList.get(j).getSizeInFeet()
									: "NULL";

							values.append("(").append(sofaid).append(", ").append("'").append(itemname).append("', ")
									.append(total).append(", ").append(sofaSize).append(", ").append(designPattern)
									.append(", ").append(pricePerFoot).append(", ").append(transportationFee)
									.append(", ").append(leatherMeter).append(", ").append(leatherPrice).append(", ")
									.append(fabricMeter).append(", ").append(fabricPrice).append(", ")
									.append(sizeInFeet).append(", ").append(customerid).append(")");

							sofaid++;

							if (itemname == null && total == null) {
								flag = false;
							}
						}

						if (flag) {
							insertString.append(values);
							manager.createNativeQuery(insertString.toString()).executeUpdate();
						}
					}

					transaction.commit();

					Common.fillResponseMap(responMap, false, CommonEnum.SUCCESS.getValue(), desc,
							ToastrAction.SUCCESS.getValue());
					return new ResponseEntity<>(responMap, HttpStatus.OK);

				} else {
					Common.fillResponseMap(responMap, true, CommonEnum.ERROR.getValue(),
							CommonEnum.UNABLE_TO_ACQUIRE_TRANSACTION.getValue(), ToastrAction.ERROR.getValue());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responMap);
				}
			}

		} catch (PersistenceException ex) {
			Common.throwException(ex, null, transaction, responMap, null, emf, request);
		} catch (Exception e) {
			Common.throwException(null, e, transaction, responMap, null, emf, request);
		} finally {
			if (manager != null) {
				manager.clear();
				manager.close();
			}
		}

		Common.fillResponseMap(responMap, true, CommonEnum.ERROR.getValue(),
				CommonEnum.INTERNAL_SERVER_ERROR.getValue(), ToastrAction.ERROR.getValue());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responMap);
	}

	// ========================= Edit (Get Data) ===========================//
	@Override
	public ResponseEntity<Object> getQualityData(String customer_id) {
		EntityManager manager = null;
		HashMap<String, Object> responseMap = new HashMap<>();
		try {
			manager = emf.createEntityManager();
			if (Common.checkNullAndEmpty(customer_id)) {
				Session session = emf.createEntityManager().unwrap(Session.class);
				Session session1 = emf.createEntityManager().unwrap(Session.class);
				Session session2 = emf.createEntityManager().unwrap(Session.class);
				Session session3 = emf.createEntityManager().unwrap(Session.class);
				Session session4 = emf.createEntityManager().unwrap(Session.class);
				String query = "select * from customers c  where c.customer_id =" + customer_id;
				List<?> list = ListToJSONConverter.queryRunner(session, query);

				String queryCurtain = "SELECT * FROM curtain C  WHERE CUSTOMER_ID = " + customer_id;
				String queryBlind = "SELECT "
						+ "    blindid AS blindid, itemname AS name, total AS total, blind_type AS blindType, width AS width, height AS height, per_sq_feet AS perSqFeet, per_meter AS perMeter,"
						+ "    channel_per_sq_feet AS channelPerSqFeet, fitting_cost AS fittingCost, dimout_per_meter AS dimoutPerMeter, discount_percentage AS discountPercentage, discount AS discount,"
						+ "    total_sq_feet AS totalSqFeet, number_of_parts AS numberOfParts, total_meters AS totalMeters, channel_sq_feet AS channelSqFeet,"
						+ "    channel_cost AS channelCost, fabric_cost AS fabricCost, dimout_meters AS dimoutMeters,"
						+ "    dimout_cost AS dimoutCost, total_cost AS totalCost, discounted_total AS discountedTotal, beforeDiscountPrice, afterDiscountPrice"
						+ " FROM " + "    blind b  WHERE CUSTOMER_ID = " + customer_id;
				String queryMattress = " SELECT * FROM mattress M WHERE CUSTOMER_ID = " + customer_id;
				String querySofa = " SELECT * FROM sofa S WHERE CUSTOMER_ID = " + customer_id;

				List<?> Curtain_list = ListToJSONConverter.queryRunner(session1, queryCurtain);
				List<?> Blind_list = ListToJSONConverter.queryRunner(session2, queryBlind);
				List<?> Mattress_list = ListToJSONConverter.queryRunner(session3, queryMattress);
				List<?> Sofa_list = ListToJSONConverter.queryRunner(session4, querySofa);

				if (list.size() > 0) {
					responseMap.put("iserror", "N");
					responseMap.put("data", list);
					responseMap.put("curtains", Curtain_list);
					responseMap.put("blinds", Blind_list);
					responseMap.put("mattresses", Mattress_list);
					responseMap.put("sofas", Sofa_list);
					return ResponseEntity.ok(responseMap);
				} else {
					Common.fillResponseMap(responseMap, true, CommonEnum.ERROR.getValue(),
							CommonEnum.NO_DATA_FOUND.getValue(), ToastrAction.ERROR.getValue());
					return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
				}
			} else {
				Common.fillResponseMap(responseMap, true, CommonEnum.ERROR.getValue(),
						CommonEnum.INVALID_USER.getValue(), ToastrAction.ERROR.getValue());
				return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			logger.error("{}", e);
		} finally {
			if (manager != null) {
				manager.clear();
				manager.close();
			}
		}
		Common.fillResponseMap(responseMap, true, CommonEnum.ERROR.getValue(),
				CommonEnum.ERROR_OCCURED_WHILE_RETRIWING_DATA.getValue(), ToastrAction.ERROR.getValue());
		return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
	}

	// ============================ Delete ===========================//
	@Override
	public <T> Object deleteData(String customer_id) {
		EntityManager manager = null;
		EntityTransaction transaction = null;
		Map<String, Object> map = new HashMap<String, Object>();

		try {
			customer_id = Common.checkNullAndEmpty(customer_id) ? (customer_id) : null;
			manager = emf.createEntityManager();
			transaction = manager.getTransaction();
			if (transaction != null) {

				transaction.begin();
				String deleteQuery = "DELETE FROM customers WHERE customer_id = " + customer_id;
				manager.createNativeQuery(deleteQuery).executeUpdate();

				String deleteCurtainQuery = "DELETE FROM curtain WHERE customer_id = " + customer_id;
				manager.createNativeQuery(deleteCurtainQuery).executeUpdate();

				String deleteBlindQuery = "DELETE FROM blind WHERE customer_id = " + customer_id;
				manager.createNativeQuery(deleteBlindQuery).executeUpdate();

				String deleteMattressQuery = "DELETE FROM mattress WHERE customer_id = " + customer_id;
				manager.createNativeQuery(deleteMattressQuery).executeUpdate();

				String deleteSofaQuery = "DELETE FROM sofa WHERE customer_id = " + customer_id;
				manager.createNativeQuery(deleteSofaQuery).executeUpdate();

				transaction.commit();

				Common.fillResponseMap(map, false, CommonEnum.SUCCESS.getValue(), CommonEnum.DELETE_SUCCESS.getValue(),
						ToastrAction.SUCCESS.getValue());
				return ResponseEntity.ok(map);

			} else {
				logger.error(CommonEnum.UNABLE_TO_ACQUIRE_TRANSACTION.getValue());
				Common.fillResponseMap(map, true, CommonEnum.ERROR.getValue(),
						CommonEnum.ERROR_OCCURED_WHILE_DELETING_DATA.getValue(), ToastrAction.ERROR.getValue());
				return ResponseEntity.ok(map);
			}

		} catch (PersistenceException ex) {
			ex.printStackTrace();
			Common.throwException(ex, null, transaction, map, null, emf, request);
		} catch (Exception e) {
			e.printStackTrace();
			Common.throwException(null, e, transaction, map, null, emf, request);
		} finally {
			if (null != manager) {
				manager.clear();
				manager.close();
			}
		}
		Common.fillResponseMap(map, true, CommonEnum.ERROR.getValue(), CommonEnum.INTERNAL_SERVER_ERROR.getValue(),
				ToastrAction.ERROR.getValue());
		return ResponseEntity.ok(map);

	}
}
