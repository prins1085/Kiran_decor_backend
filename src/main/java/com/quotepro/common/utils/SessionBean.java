package com.quotepro.common.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class SessionBean {
	private String empcode;
	private String empname;
	private String designation;
	@SuppressWarnings("rawtypes")
	private Map permission;
	private List<?> permissionlist;
	private StringBuilder menu;
	private ArrayList<?> menulist;

	public String getEmpcode() {
		return empcode;
	}

	public void setEmpcode(String empcode) {
		this.empcode = empcode;
	}

	public String getEmpname() {
		return empname;
	}

	public void setEmpname(String empname) {
		this.empname = empname;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	@SuppressWarnings("rawtypes")
	public Map getPermission() {
		return permission;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void setPermission(List lst) {
        permission = new HashMap<String, String>();
        for (int i =0; i < lst.size(); i++) {
        	Object[] str2 = (Object[]) lst.get(i);
            permission.put(str2[0],str2[1] );
        }
    }

	public List<?> getPermissionlist() {
		return permissionlist;
	}

	public void setPermissionlist(List<?> permissionlist) {
		this.permissionlist = permissionlist;
	}
	
	public StringBuilder getMenu() {
		return menu;
	}

	public ArrayList<?> getMenulist() {
		return menulist;
	}

	public void setMenulist(ArrayList<?> menulist) {
		List<?> MainMenu = (List<?>) menulist.get(0);
		List<?> SubMenu = (List<?>) menulist.get(1);
		menu = new StringBuilder("");
		int count = 0;
		if (MainMenu != null) {
			for (int i = 0; i < MainMenu.size(); i++) {
				Object[] str1 = (Object[]) MainMenu.get(i);
				menu.append( "<li id='link" + str1[2] + "'>\n"
						+ (str1[1] == null ? "" : str1[1].toString() + str1[2] + "</a>") + "\n" );
				if (SubMenu != null) {
					count = 0;
					for (int j = 0; j < SubMenu.size(); j++) {
						Object[] str2 = (Object[]) SubMenu.get(j);
						if (str1[0] == str2[2]) {
							menu.append("<ul class=\"sub-menu\">\n");
							count = 1;
							break;
						}
					}
					for (int j = 0; j < SubMenu.size(); j++) {
						Object[] str2 = (Object[]) SubMenu.get(j);
						if (str1[0] == str2[2]) {
							menu.append( "<li id='link" + str2[1] + "'>\n"
									+ (str2[0] == null ? "" : str2[0].toString() + str2[1] + "</a></li>") + "\n" );
						}
					}
					if (count == 1) {
						menu.append(" </ul>\n");
					}

				}
				menu.append("</li>\n");
			}
		}
	}
}
