package com.quotepro.common.enumeration;

public enum CommonEnum {
	
	TASK_ADD ("add"),
    TASK_EDIT("edit"),
    TASK_DELETE ("delete"),
    TASK_SEARCH ("search"),
    TASK_PRINT  ("print"),
    TASK_VIEW ("view"),
	AUDIT_CREATED("Created"), 
	AUDIT_UPDATED("Updated"),

	AUDIT_DELETED("Deleted"), 
	AUDIT_EXPORTED("Exported"),
	
	AUDIT_PRINTED("Printed"), 
	AUDIT_IMPORTED("Imported"),
	
	AUDIT_CREATED_CODE(1), 
	AUDIT_UPDATED_CODE(2),

	AUDIT_DELETED_CODE(3), 
	AUDIT_EXPORTED_CODE(4),
	
	AUDIT_PRINTED_CODE(5), 
	AUDIT_IMPORTED_CODE(6),
	
	NOTIFY_CREATED("1"), 
	NOTIFY_UPDATED("2"),
	
	NOTIFY_DELETED("3"), 
	NOTIFY_EXPORTED("4"),
	
	NOTIFY_PRINTED("5"), 
	NOTIFY_IMPORTED("6"),

	ALREADY_EXISTS("%s %s already exists."), 

	SAVE_SUCCESS("Data Saved Successfully"), 
	
	UPDATE_SUCCESS("Data Updated Successfully"), 

	THEME_SAVE_SUCCESS("Theme setting saved successfully"), 

	DELETE_SUCCESS("Data Deleted successfully"),

	NO_DATA_FOUND("No Data Found"), 

	ERROR_OCCURED_WHILE_SAVING_DATA("Error occured while saving data"),	

	ERROR_OCCURED_WHILE_DELETING_DATA("Error occured while deleting data"),

	INACTIVE_DELETE_MESSAGE("Inactive data cannot be deleted"),

	UNABLE_TO_ACQUIRE_TRANSACTION("Unable to acquire transaction."),

	ERROR_OCCURED_WHILE_RETRIWING_DATA("Error occured while retrieving data."),	

	SOMETHING_WENT_WRONG("something went wrong please try again"),	

	INVALID_USER("Malfunctioned user request."),

	ACCESS_FORBIDDEN("You're not allowed to perform the operation"),

	ERROR("Error"),
	
	INFO("Info"),

	INVALID_INPUT("Invalid Input"),

	PARAMETER_MISSING("Parameter is missing"),

	DUPLICATE("Duplicate Data."),

	SUCCESS("Success."),

	SAVE_ERROR_MESSAGE("Error occured while persisting data."),	

	INTERNAL_SERVER_ERROR("Internal Server Error."),
	DEFAULT_ERROR("Something went wrong. Please contact to administrator."),

	SQL_SERVER_ERROR("Internal SQL exception generated."),

	REQUIRED("Required Field."),

	WARNING("Warning"),
	
	SEPRATOR("-"),
	
	QUOTATION_SORTNAME("QT"),
	CPO_SORTNAME("PO"),
	VPO_SORTNAME("VPO"),
	VEENDORINQUIRY_SORTNAME("VIQ"),
	INQUIRY_SORTNAME("IQ"),
	DC_SORTNAME("DC"),
	HD_SORTNAME("HD"),
	OP_SORTNAME("OP");

	private String value;
	private int intvalue;

	CommonEnum(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return this.value;
	}

	

	CommonEnum(int intvalue) {
		this.intvalue = intvalue;
	}
	
	public int getIntvalue() {
		return this.intvalue;
	}


	
}