package com.yaz.alind.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Formula;

@Entity
@Table(name="alind_t_board_division_details")
public class BoardDivisionDetails {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="board_division_id", unique = true, nullable = false)
	private int boardDivisionId;
	@Column(name="board_division_name")
	private String boardDivisionName;
	@Column(name="board_division_address")
	private String boardDivisionAddress;
	@Column(name="other_details")
	private String otherDetails;
	@Column(name="railway_zone")
	private String railwayZone;
	@Column(name = "is_active",columnDefinition="Decimal(11) default '0'")
	private int isActive;
	@Formula("nullif(concat(railway_zone,'/',' ',board_division_name),' ')") // railway_zone and board_division_name are column names
	private String zoneDivisionName;
	@Transient
	private int slNo;
	
	
	public int getBoardDivisionId() {
		return boardDivisionId;
	}
	public void setBoardDivisionId(int boardDivisionId) {
		this.boardDivisionId = boardDivisionId;
	}
	public String getBoardDivisionName() {
		return boardDivisionName;
	}
	public void setBoardDivisionName(String boardDivisionName) {
		this.boardDivisionName = boardDivisionName;
	}
	public String getBoardDivisionAddress() {
		return boardDivisionAddress;
	}
	public void setBoardDivisionAddress(String boardDivisionAddress) {
		this.boardDivisionAddress = boardDivisionAddress;
	}
	public String getOtherDetails() {
		return otherDetails;
	}
	public void setOtherDetails(String otherDetails) {
		this.otherDetails = otherDetails;
	}
	public String getRailwayZone() {
		return railwayZone;
	}
	public void setRailwayZone(String railwayZone) {
		this.railwayZone = railwayZone;
	}
	public int getIsActive() {
		return isActive;
	}
	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}
	public String getZoneDivisionName() {
		return zoneDivisionName;
	}
	public void setZoneDivisionName(String zoneDivisionName) {
		this.zoneDivisionName = zoneDivisionName;
	}
	public int getSlNo() {
		return slNo;
	}
	public void setSlNo(int slNo) {
		this.slNo = slNo;
	}
	
	
	
}
